package br.com.drv.gamereviewer.entities.log;

import java.time.LocalDateTime;

/**
 * Representa uma linha de log de início de partida
 * @author Diego Venancio
 *
 */
public class GameStart extends GameEvent {

	// setando tipo para evitar comparação com instanceof para saber de é início
	// ou fim do jogo
	public GameStart(LocalDateTime timestamp, String gameId) {
		super(timestamp, gameId);
	}
}
