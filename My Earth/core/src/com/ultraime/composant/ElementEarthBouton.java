package com.ultraime.composant;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ultraime.database.ElementEarth;

public class ElementEarthBouton {

	public ElementEarth elementEarth;
	public Bouton bouton;
   
	/**
	 * @param elementEarth
	 * @param bouton
	 */
	public ElementEarthBouton(ElementEarth elementEarth, Bouton bouton) {
		super();
		this.elementEarth = elementEarth;
		this.bouton = bouton;
	}

	public void render(final SpriteBatch batch) {
		bouton.render(batch);
	}

}
