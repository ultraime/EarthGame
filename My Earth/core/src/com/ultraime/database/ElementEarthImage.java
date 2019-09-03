package com.ultraime.database;

import java.io.Serializable;

/**
 * @author Ultraime
 *
 */
public class ElementEarthImage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * position de l'image.
	 */
	public int x, y;

	/**
	 * Tuile de l'image.
	 */
	public int idTuile;

	/**
	 * false = pas de collision true = collision
	 */
	public boolean isCollision = false;

	/**
	 * le layers ou doit être affiché l'objet !
	 */
	public String layerCible;

	public ElementEarthImage() {

	}

	public ElementEarthImage(final ElementEarthImage elementEarthImagesOld) {
		this.x = new Integer(elementEarthImagesOld.x);
		this.y = new Integer(elementEarthImagesOld.y);
		this.idTuile = elementEarthImagesOld.idTuile;
		this.isCollision = elementEarthImagesOld.isCollision;
		this.layerCible = elementEarthImagesOld.layerCible;
	}

}
