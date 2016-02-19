package br.com.drv.gamereviewer.entities.match;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Uma partida do jogo
 * @author Diego Venancio
 *
 */
public class Match {

	private String id;

	private LocalDateTime startDateTime;
	
	private LocalDateTime endDateTime;
	
	private LinkedHashMap<String,Player> players;
	
	public Match(LocalDateTime datetime, String id) {
		setId(id);
		setStartDatetime(datetime);
		setPlayers(new LinkedHashMap<String, Player>());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public LocalDateTime getStartDatetime() {
		return startDateTime;
	}

	public void setStartDatetime(LocalDateTime datetime) {
		this.startDateTime = datetime;
	}

	public LinkedHashMap<String, Player> getPlayers() {
		return players;
	}

	public void setPlayers(LinkedHashMap<String, Player> players) {
		this.players = players;
	}

	public LocalDateTime getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(LocalDateTime endDateTime) {
		this.endDateTime = endDateTime;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
