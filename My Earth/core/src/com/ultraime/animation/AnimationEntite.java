package com.ultraime.animation;

import java.io.Serializable;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ultraime.game.utile.Image;
import com.ultraime.game.utile.VariableCommune;

public class AnimationEntite implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public AnimationManager animationDormir;

	public AnimationEntite() {
		animationDormir = new AnimationManager(Image.getImage(VariableCommune.ANIMATION_DORMIR), 32, 32, 0.150f);
	}

	public void renderAnimationDormir(final SpriteBatch batch, final float x, final float y) {
		animationDormir.render(batch, x, y, 0);
	}
}
