package com.ultraime.database.entite;

import java.io.Serializable;

public class ElementGenere implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Nom de l'élément
	 */
	public String nom;
	
	/**
	 * nombre d'élément généré
	 */
	public int nbGenere;

	public ElementGenere(final String elem) {
		this.nom = elem;
	}
}
