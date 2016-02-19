package br.com.drv.gamereviewer.services;

import java.time.LocalDateTime;
import java.util.List;

import br.com.drv.gamereviewer.entities.log.LogLine;
import br.com.drv.gamereviewer.entities.match.Gun;
import br.com.drv.gamereviewer.entities.match.Match;
import br.com.drv.gamereviewer.entities.match.Streak;

/**
 * Serviços para a criação do report sobre todas as partidas encontradas em um log
 * @author Diego Venancio
 *
 */
public interface LogReviewerService {

	/**
	 * Verifica o arquivo de log, faz o parse do Log e chama gerReport para construir o report
	 * @param fileName  Caminho para o arquivo
	 * @return o report feito
	 */
	List<Match> reviewLog(String fileName);

	/**
	 * A partir dos objetos já extraídos do Log, gera o report
	 * @param matches  Objetos reprensentando as partidas, com todos os dados da partida dentro.
	 * @return o report feito
	 */
	StringBuilder getReport(List<Match> matches);

	/**
	 * Faz o parse da linha de log e retorna um objeto que a representa
	 * 
	 * @param logLine
	 *            String com a linha do log sendo lida
	 * @return Objeto representando linha de log ou null se não reconhecer a linha
	 */
	LogLine parseLogLine(String logLine);

	/**
	 * Verifica lista de armas usadas pelo jogador e retorna a mais usada
	 * @param gunsUsed  lista de armas usadas
	 * @return a arma mais usada
	 */
	Gun pickFavoriteGun(List<Gun> gunsUsed);
	
	/**
	 * Verifica se na lista de mortes há 5 num intervalo de 1 min
	 * @param kills  lista de mortes
	 * @return  número de sequências de 5 mortes em um minuto
	 */
	int getAwardsForKillStreak(List<LocalDateTime> kills);
	
	/**
	 * Busca entre todos os jogadores qual teve a maior sequência de assassinatos, sem morrer
	 * @param match o jogo onde estão os jogadores e listas de mortes
	 * @return Streak com o maior número de mortes e qual jogador a fez
	 */
	public Streak getMaxStreak(Match match);
}