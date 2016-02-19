package br.com.drv.gamereviewer.entities.log;

import java.time.LocalDateTime;

/**
 * Representa uma linha de log de morte
 * @author Diego Venancio
 *
 */
public class KillLine extends LogLine {

	private String killerId;

	private String killedId;

	// Meio pelo qual o player foi morto. Arma em caso de outro player ou causa ambiental em caso de ambiente.
	private String mediumId;

	public KillLine(LocalDateTime timestamp, String killerId, String killedId, String gunId) {
		setDatetime(timestamp);
		setKilledId(killedId);
		setKillerId(killerId);
		setMediumId(gunId);
	}

	public String getKillerId() {
		return killerId;
	}

	public void setKillerId(String killerId) {
		this.killerId = killerId;
	}

	public String getKilledId() {
		return killedId;
	}

	public void setKilledId(String killedId) {
		this.killedId = killedId;
	}

	public String getMediumId() {
		return mediumId;
	}

	public void setMediumId(String gunId) {
		this.mediumId = gunId;
	}

}
