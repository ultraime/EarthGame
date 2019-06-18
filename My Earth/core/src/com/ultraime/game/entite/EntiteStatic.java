package com.ultraime.game.entite;

public class EntiteStatic extends Entite {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int largeur;
	private int hauteur;

	public EntiteStatic(float x, float y, int largeur, int hauteur) {
		super(x, y);
		this.largeur = largeur;
		this.hauteur = hauteur;
	}

	public int getLargeur() {
		return largeur;
	}

	public void setLargeur(int largeur) {
		this.largeur = largeur;
	}

	public int getHauteur() {
		return hauteur;
	}

	public void setHauteur(int hauteur) {
		this.hauteur = hauteur;
	}

}
