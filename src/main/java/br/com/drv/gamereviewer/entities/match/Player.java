package br.com.drv.gamereviewer.entities.match;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Um jogador da partida.
 * @author Diego Venancio
 *
 */
public class Player {

	private String name;

	private List<LocalDateTime> kills;

	private List<LocalDateTime> deaths;

	private List<Gun> gunsUsed;

	public Player(String name) {
		setName(name);
		kills = new ArrayList<LocalDateTime>();
		deaths = new ArrayList<LocalDateTime>();
		gunsUsed = new ArrayList<Gun>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<LocalDateTime> getKills() {
		return kills;
	}

	public void setKills(List<LocalDateTime> kills) {
		this.kills = kills;
	}

	public List<LocalDateTime> getDeaths() {
		return deaths;
	}

	public void setDeaths(List<LocalDateTime> deaths) {
		this.deaths = deaths;
	}


	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public String printRankingLine(Player p) {

		return String.format("Player: %-12s Kills: %-5s Deaths: %-5s%n", getName(), getKills().size(),
				getDeaths().size());

	}

	public List<Gun> getGunsUsed() {
		return gunsUsed;
	}

	public void setGunsUsed(List<Gun> gunsUsed) {
		this.gunsUsed = gunsUsed;
	}
}
