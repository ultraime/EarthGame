package com.ultraime.game.entite;

import com.ultraime.animation.AnimationManager;
import com.ultraime.game.utile.VariableCommune;

public class EntiteAnimal extends EntiteVivante {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int typeUnite;

	public EntiteAnimal(float x, float y, float radius, final TypeEntiteVivante typeEntite) {
		super(x, y, radius, typeEntite);
	}

	@Override
	public void creerAnimation() {
		if (typeEntiteEnum == TypeEntiteVivante.POULE) {
			this.typeUnite = VariableCommune.PERSO1;
		}
		this.animationManager = new AnimationManager(typeEntiteEnum, typeUnite);
		initAnimationEtat();

	}

}
