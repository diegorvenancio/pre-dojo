package br.com.drv.gamereviewer.entities.log;

import java.time.LocalDateTime;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Representa uma linha de log qualquer
 * @author Diego Venancio
 *
 */
public abstract class LogLine {

	private LocalDateTime datetime;
	
	
	public LocalDateTime getDatetime() {
		return datetime;
	}

	public void setDatetime(LocalDateTime datetime) {
		this.datetime = datetime;
	}
	
	public String toString()
	{
	  return ToStringBuilder.reflectionToString(this);
	}
}
