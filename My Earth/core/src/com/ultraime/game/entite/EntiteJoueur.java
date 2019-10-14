package com.ultraime.game.entite;

import com.ultraime.animation.AnimationManager;
import com.ultraime.game.utile.VariableCommune;

/**
 * @author Ultraime
 *
 */
public class EntiteJoueur extends EntiteVivante {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Pour les animations.
	 * c'est comme un sous type
	 */
	private int typeUnite;

	/**
	 * @param x
	 * @param y
	 * @param radius
	 * @param typeEntite
	 */

	public EntiteJoueur(float x, float y, float radius, final TypeEntiteVivante typeEntite) {
		super(x, y, radius, typeEntite);
		
	}

	@Override
	public void creerAnimation() {
		if (typeEntiteEnum == TypeEntiteVivante.PERSONNAGE) {
			this.typeUnite = VariableCommune.PERSO1;
		}
		this.animationManager = new AnimationManager(typeEntiteEnum, typeUnite);
		initAnimationEtat();

	}

}
