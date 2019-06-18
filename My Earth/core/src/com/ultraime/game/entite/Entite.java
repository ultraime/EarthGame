package com.ultraime.game.entite;

import java.io.Serializable;

public class Entite implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static int ID = 0;
	public int id;
	public float x, y;

	public Entite(float x, float y) {
		super();
		this.id = ID++;
		this.x = x;
		this.y = y;
	}

}
