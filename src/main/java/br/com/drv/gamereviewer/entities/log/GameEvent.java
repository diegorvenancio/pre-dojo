package br.com.drv.gamereviewer.entities.log;

import java.time.LocalDateTime;

/**
 * Representa uma linha de início ou fim de partida
 * @author Diego Venancio
 *
 */
public abstract class GameEvent extends LogLine {

	// id do jogo
	private String gameId;
	
	public GameEvent(LocalDateTime timestamp, String gameId) {

		setGameId(gameId);
		setDatetime(timestamp);
	}
	
	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

}
