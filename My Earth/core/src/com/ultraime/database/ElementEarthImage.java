package com.ultraime.database;

import java.io.Serializable;

/**
 * @author Ultraime
 *
 */
public class ElementEarthImage implements Serializable {

	/**
	 * position de l'image.
	 */
	public int x, y;

	/**
	 * Tuile de l'image.
	 */
	public int idTuile;

	public boolean isCollision = false;

}
