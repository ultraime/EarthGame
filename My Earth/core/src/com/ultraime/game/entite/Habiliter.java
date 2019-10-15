package com.ultraime.game.entite;

import java.io.Serializable;

import com.ultraime.database.base.Base;
import com.ultraime.game.entite.EntiteVivante.TypeEntiteVivante;

public class Habiliter implements Serializable {

	/**
	 * Pour les stats de base
	 */
	public TypeEntiteVivante type;

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
		energie = new int[3];
		satiete = new int[3];
		hydratation = new int[3];
	}

	public Habiliter(final TypeEntiteVivante type) {
		this();
		this.type = type;
		initStatAvecHabiliter(Base.getInstance().getReferenceHabiliter(type));
	}

	public Habiliter(final Habiliter habiliter) {
		this();
		this.type = habiliter.type;
		initStatAvecHabiliter(habiliter);

	}

	private void initStatAvecHabiliter(final Habiliter habiliter) {
		sante[GAIN] = new Integer(habiliter.sante[GAIN]);
		sante[ACTUEL] = new Integer(habiliter.sante[ACTUEL]);
		sante[MAX] = new Integer(habiliter.sante[MAX]);

		energie[GAIN] = new Integer(habiliter.energie[GAIN]);
		energie[ACTUEL] = new Integer(habiliter.energie[ACTUEL]);
		energie[MAX] = new Integer(habiliter.energie[MAX]);

		satiete[GAIN] = new Integer(habiliter.satiete[GAIN]);
		satiete[ACTUEL] = new Integer(habiliter.satiete[ACTUEL]);
		satiete[MAX] = new Integer(habiliter.satiete[MAX]);

		hydratation[GAIN] = new Integer(habiliter.hydratation[GAIN]);
		hydratation[ACTUEL] = new Integer(habiliter.hydratation[ACTUEL]);
		hydratation[MAX] = new Integer(habiliter.hydratation[MAX]);

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
