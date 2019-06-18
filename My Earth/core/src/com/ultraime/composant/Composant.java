package com.ultraime.composant;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Composant {
	protected SpriteBatch batch;

	public abstract boolean isClique(float x, float y);

	public abstract boolean isOver(float x, float y);

	public abstract void touchUP(int x, int y);

	public abstract void render();

	public abstract void resetSelector();

}
