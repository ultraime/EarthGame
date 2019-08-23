package com.ultraime.game.utile;

public class Calcul {

	public static float arrondirFloat(float f) {
		f = f * 1000;
		f = Math.round(f);
		return f / 1000;
	}

	public static int random(final int min, final int max) {
		return min + (int) (Math.random() * ((max - min) + 1));
	}

}
