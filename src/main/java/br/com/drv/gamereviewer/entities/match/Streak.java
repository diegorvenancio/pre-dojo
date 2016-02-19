package br.com.drv.gamereviewer.entities.match;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Representa uma sequência de assassinatos, com seu executor e número de mortes
 * @author Diego Venancio
 *
 */
public class Streak {

	private String name;
	
	private Long numberOfKills;

	public Streak(String streakOwnerName, Long streakSize) {

		setName(streakOwnerName);
		setNumberOfKills(streakSize);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getNumberOfKills() {
		return numberOfKills;
	}

	public void setNumberOfKills(Long numberOfKills) {
		this.numberOfKills = numberOfKills;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
