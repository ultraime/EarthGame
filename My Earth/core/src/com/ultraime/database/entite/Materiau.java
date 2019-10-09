package com.ultraime.database.entite;

import java.io.Serializable;

public class Materiau implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Materiau() {
		this.nombreActuel = 0;
	}

	public Materiau(Materiau materiau) {
		this.nom = materiau.nom;
		this.nombreActuel = new Integer(materiau.nombreActuel);
		this.nombreRequis = new Integer(materiau.nombreRequis);
	}

	/**
	 * Nom du matérieau requis
	 */
	public String nom;

	/**
	 * Nombre d'élément requis pour être validé
	 */
	public int nombreRequis;

	/**
	 * Nombre d'élément actuellement présent
	 */
	public int nombreActuel;

}
