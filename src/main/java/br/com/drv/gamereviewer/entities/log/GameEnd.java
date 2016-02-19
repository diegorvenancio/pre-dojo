package br.com.drv.gamereviewer.entities.log;

import java.time.LocalDateTime;

/**
 * Representa a linha de fim de uma partida
 * @author Diego Venancio
 *
 */
public class GameEnd extends GameEvent {

	public GameEnd(LocalDateTime timestamp, String gameId) {
		super(timestamp, gameId);
	}
}
