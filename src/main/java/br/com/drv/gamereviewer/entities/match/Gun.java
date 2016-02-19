package br.com.drv.gamereviewer.entities.match;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Uma arma usada por qualquer jogador
 * @author Diego Venancio
 *
 */
public class Gun {

	private String name;

	public Gun(String name) {
		setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
