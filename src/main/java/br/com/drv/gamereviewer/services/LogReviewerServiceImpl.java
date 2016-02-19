package br.com.drv.gamereviewer.services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import br.com.drv.gamereviewer.entities.log.GameEnd;
import br.com.drv.gamereviewer.entities.log.GameStart;
import br.com.drv.gamereviewer.entities.log.KillLine;
import br.com.drv.gamereviewer.entities.log.LogLine;
import br.com.drv.gamereviewer.entities.match.Gun;
import br.com.drv.gamereviewer.entities.match.Match;
import br.com.drv.gamereviewer.entities.match.Player;
import br.com.drv.gamereviewer.entities.match.Streak;
import br.com.drv.gamereviewer.exceptions.GameReviewerException;

/**
 * Implementa os serviços para geração do report das partidas
 * @author Diego Venancio
 *
 */
public class LogReviewerServiceImpl implements LogReviewerService {

	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

	// id do jogador para mortes causadas pelo ambiente
	private static final String WORLD = "<WORLD>";

	// patterns para parse do log
	private static Pattern gameStart = Pattern.compile("^(.{19}) - New match (\\d+) has started");
	private static Pattern gameEnd = Pattern.compile("^(.{19}) - Match (\\d+) has ended");
	private static Pattern kill = Pattern.compile("^(.{19}) - (.*) killed (.*) using (.*)");
	private static Pattern death = Pattern.compile("^(.{19}) - " + WORLD + " killed (.*) by (.*)");

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.com.drv.gamereviewer.services.LogReviewerService#reviewLog(java.lang.
	 * String)
	 */
	@Override
	public List<Match> reviewLog(String fileName) {

		List<Match> matches = new ArrayList<>();
		Match currentMatch = null;
		HashMap<String, Player> currentPlayers = null;

		try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {

			for (String newLine; (newLine = reader.readLine()) != null && !newLine.trim().isEmpty();) {

				LogLine logLine = parseLogLine(newLine);

				if (logLine instanceof GameStart) {

					currentMatch = new Match(logLine.getDatetime(), ((GameStart) logLine).getGameId());
					matches.add(currentMatch);
					currentPlayers = currentMatch.getPlayers();

				} else if (logLine instanceof KillLine) {

					if (currentMatch == null || currentPlayers == null) {
						throw new GameReviewerException("Linha de morte encontrada sem jogo iniciado: ");
					}

					KillLine kl = (KillLine) logLine;

					// se killer não é <WORLD>, registra kill
					if (kl.getKillerId() != null) {

						Player currKiller = currentPlayers.get(kl.getKillerId());

						if (currKiller == null) {
							currKiller = new Player(kl.getKillerId());
							currentPlayers.put(kl.getKillerId(), currKiller);
						}
						currKiller.getKills().add(kl.getDatetime());

						// registra arma usada
						currKiller.getGunsUsed().add(new Gun(kl.getMediumId()));
					}

					// registra death
					Player currKilled = currentPlayers.get(kl.getKilledId());

					if (currKilled == null) {
						currKilled = new Player(kl.getKilledId());
						currentPlayers.put(kl.getKilledId(), currKilled);
					}
					currKilled.getDeaths().add(kl.getDatetime());

				} else if (logLine instanceof GameEnd) {

					currentMatch.setEndDateTime(logLine.getDatetime());

					currentMatch = null;
					currentPlayers = null;
				}
			}
		} catch (IOException e) {

			throw new GameReviewerException("Erro ao acessar arquivo de log. ", e);
		}

		return matches;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.com.drv.gamereviewer.services.LogReviewerService#getReport(java.util.
	 * List)
	 */
	@Override
	public StringBuilder getReport(List<Match> matches) {

		StringBuilder sb = new StringBuilder();

		for (Match match : matches) {

			sb.append(String.format("MATCH #%s RESULTS: %n", match.getId()));
			sb.append(String.format("start time: %1$tT %n%n", match.getStartDatetime()));

			List<Player> orderedList = match
					.getPlayers().entrySet().stream().sorted((p1, p2) -> Integer
							.compare(p2.getValue().getKills().size(), p1.getValue().getKills().size()))
					.map(e -> e.getValue()).collect(Collectors.toList());

			// encontra maior streak de mortes sem morrer entre todos os
			// jogadores
			Streak streak = getMaxStreak(match);

			int i = 1;
			for (Player player : orderedList) {

				sb.append("#" + i + "   " + player.printRankingLine(player));

				// se for o vencedor, imprima a arma mais usada
				if (i++ == 1) {

					Gun favGun = pickFavoriteGun(player.getGunsUsed());
					if (favGun != null) {
						sb.append(String.format("        Favorite Gun: %s%n", favGun.getName()));
					}

				}

				// verificar award para jogador que não morreu nesta partida
				if (player.getDeaths().size() == 0) {

					sb.append(String.format("        IMMORTAL AWARD: no deaths on this match!%n"));
				}

				// verifica se este é o jogador com maior streak
				if (streak != null && player.getName().equals(streak.getName())) {

					sb.append(String.format("        LONGEST STREAK AWARD: %d kill(s) with no deaths.%n",
							streak.getNumberOfKills()));
				}

				// verificar award para jogador que matou 5 em 1 minuto
				int awards = getAwardsForKillStreak(player.getKills());
				if (awards > 0) {
					sb.append(String.format("        5 KILLS IN ONE MINUTE AWARD x %d %n", awards));
				}

				sb.append(String.format("%n"));
			}

			sb.append(String.format("end time: %1$tT %n", match.getEndDateTime()));
			sb.append(String.format("%n%n", match.getId()));
		}

		return sb;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.com.drv.gamereviewer.services.LogReviewerService#parseLogLine(java.
	 * lang.String)
	 */
	@Override
	public LogLine parseLogLine(String logLine) {

		// verificando se é uma linha de início de jogo
		Matcher matcher = gameStart.matcher(logLine);

		if (matcher.matches()) {

			return new GameStart(LocalDateTime.parse(matcher.group(1), formatter), matcher.group(2));
		}

		// verificando se é uma linha de fim de jogo
		matcher = gameEnd.matcher(logLine);

		if (matcher.matches()) {

			return new GameEnd(LocalDateTime.parse(matcher.group(1), formatter), matcher.group(2));
		}

		// verificando se é uma linha onde um jogador mata outro
		matcher = kill.matcher(logLine);

		if (matcher.matches()) {

			return new KillLine(LocalDateTime.parse(matcher.group(1), formatter), matcher.group(2), matcher.group(3),
					matcher.group(4));
		}

		// verificando se é uma linha onde um jogador mata outro
		matcher = death.matcher(logLine);

		if (matcher.matches()) {

			return new KillLine(LocalDateTime.parse(matcher.group(1), formatter), null, matcher.group(2),
					matcher.group(3));
		}

		// se não reconheceu a linha, retorne null
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.com.drv.gamereviewer.services.LogReviewerService#pickFavoriteGun(java.
	 * util.List)
	 */
	public Gun pickFavoriteGun(List<Gun> gunsUsed) {

		Optional<Entry<String, Long>> favGun = gunsUsed.stream().map(e -> e.getName())
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting())).entrySet().stream()
				.sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue())).findFirst();

		return favGun.isPresent() ? new Gun(favGun.get().getKey()) : null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.drv.gamereviewer.services.LogReviewerService#
	 * getAwardsForKillStreak(java.util.List)
	 */
	public int getAwardsForKillStreak(List<LocalDateTime> kills) {

		int result = 0;

		for (int i = 0; i < kills.size() - 1; i++) {

			LocalDateTime currKill = kills.get(i);

			// conta quantas mortes em um minuto a partir da morte atual
			long deathsOnMinute = kills.stream()
					.filter(e -> ((e.isEqual(currKill) || e.isAfter(currKill)) && e.isBefore(currKill.plusMinutes(1))))
					.count();

			if (deathsOnMinute >= 5) {
				result++;
				// pula as mortes já contabilizadas neste award
				i += 4;
			}

		}

		return result;
	}

	/**
	 * @param players
	 * @return
	 */
	public Streak getMaxStreak(Match match) {

		LinkedHashMap<String, Player> players = match.getPlayers();

		String streakOwnerName = null;
		long streakSize = 0;

		for (Player player : players.values()) {

			List<LocalDateTime> deaths = player.getDeaths();
			List<LocalDateTime> kills = player.getKills();
			LocalDateTime gameStart = match.getStartDatetime();
			LocalDateTime gameEnd = match.getEndDateTime();

			// verificar quantos kills existem entre as mortes, começando com o
			// início do jogo e
			// terminando com o fim do jogo

			deaths.add(0, gameStart);
			deaths.add(gameEnd);

			for (int i = 0; i < deaths.size() - 1; i++) {

				// calcula a streak atual
				final int j = i;
				long currStreakSize = kills.stream()
						.filter(e -> e.isAfter(deaths.get(j)) && e.isBefore(deaths.get(j + 1))).count();
				
				if (currStreakSize > streakSize) {
					streakSize = currStreakSize;
					streakOwnerName = player.getName();
				}
			}

		}

		return new Streak(streakOwnerName, streakSize);
	}

}
