package com.ultraime.database;

import java.io.Serializable;

public class ElementGenere implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Nom de l'élément généré
	 */
	public String nom;

	public ElementGenere(final String elem) {
		this.nom = elem;
	}
}
