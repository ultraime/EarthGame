package com.ultraime.game.entite;

import java.io.Serializable;

public class Habiliter implements Serializable {

	public transient static final int GAIN = 0;
	public transient static final int ACTUEL = 1;
	public transient static final int MAX = 2;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// niveau des métiers
	public int construction = 10;

	// stat de base
	public float vitesse = 4f;// 3f

	public int sante[];
	public int energie[];
	public int satiete[];
	public int hydratation[];

	public Habiliter() {
		sante = new int[3];
		sante[GAIN] = -1;
		sante[ACTUEL] = 100;
		sante[MAX] = 100;

		energie = new int[3];
		energie[GAIN] = -1;
		energie[ACTUEL] = 960;
		energie[MAX] = 960;

		satiete = new int[3];
		satiete[GAIN] = -1;
		satiete[ACTUEL] = 600;
		satiete[MAX] = 600;

		hydratation = new int[3];
		hydratation[GAIN] = -1;
		hydratation[ACTUEL] = 300;
		hydratation[MAX] = 720;
	}

	/**
	 * diminue toute les stats
	 */
	public void gererStats() {
		gererGain(energie);
		gererGain(hydratation);
		gererGain(satiete);
	}

	public void gererGain(int[] stat) {
		stat[ACTUEL] = stat[ACTUEL] + stat[GAIN];
		if (stat[ACTUEL] < 0) {
			stat[ACTUEL] = 0;
		} else if (stat[ACTUEL] > stat[MAX]) {
			stat[ACTUEL] = stat[MAX];
		}
	}

}
