package com.ultraime.game.entite;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ultraime.animation.AnimationManager;
import com.ultraime.game.metier.WorldService;
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
	private TypeEntiteVivante typeEntiteEnum;
	private int typeUnite;

	/**
	 * @param x
	 * @param y
	 * @param radius
	 * @param typeEntite
	 */

	public EntiteJoueur(float x, float y, float radius, final TypeEntiteVivante typeEntite) {
		super(x, y, radius);
		this.typeEntiteEnum = typeEntite;
		creerAnimation();
	}

	@Override
	protected void creerAnimation() {
		if (typeEntiteEnum == TypeEntiteVivante.PERSONNAGE) {
			this.typeUnite = VariableCommune.PERSO1;
		}
		this.animationManager = new AnimationManager(typeEntiteEnum, typeUnite);

	}

	public void render(final SpriteBatch batch) {
		final float posX = this.x * WorldService.MULTIPLICATEUR;
		final float posY = this.y * WorldService.MULTIPLICATEUR;
		this.animationManager.render(batch, posX, posY, this.direction);
	}

}
