package com.ultraime.game.ecran;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Ecran implements InputProcessor {
	public SpriteBatch batch;
	public EcranManager ecranManager;

	/**
	 * Appelée dès que l'écran est appelé.
	 * 
	 * @param inputMultiplexer
	 */
	public abstract void changerEcran(final InputMultiplexer inputMultiplexer);

	public abstract void create(EcranManager ecranManager);

	public abstract void render();

	public abstract void dispose();

}
