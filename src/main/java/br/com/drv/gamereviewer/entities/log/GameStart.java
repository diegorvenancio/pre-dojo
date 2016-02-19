package br.com.drv.gamereviewer.entities.log;

import java.time.LocalDateTime;

/**
 * Representa uma linha de log de in�cio de partida
 * @author Diego Venancio
 *
 */
public class GameStart extends GameEvent {

	// setando tipo para evitar compara��o com instanceof para saber de � in�cio
	// ou fim do jogo
	public GameStart(LocalDateTime timestamp, String gameId) {
		super(timestamp, gameId);
	}
}
