package com.ultraime.game.metier.travail.action;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.ultraime.database.base.Base;
import com.ultraime.game.entite.EntiteVivante;
import com.ultraime.game.entite.EntiteVivante.Etat;
import com.ultraime.game.entite.Habiliter;
import com.ultraime.game.metier.Temps;
import com.ultraime.game.utile.Calcul;

public class AEAttend extends ActionEntite {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Temps tempsFinAttente;

	public boolean isRengenEnergie = false;

	public AEAttend(int priorite) {
		super(priorite);
	}

	/**
	 * @param priorite
	 * @param tempAttente
	 * @param isRengenEnergie
	 */
	public AEAttend(int priorite, int tempAttente, boolean isRengenEnergie) {
		super(priorite);
		this.isRengenEnergie = isRengenEnergie;
		this.tempsFinAttente = new Temps(Base.getInstance().getTemps());
		tempsFinAttente.addMinute(tempAttente);
	}

	/**
	 * call by initAction
	 * 
	 * @param body
	 */
	private void initFinalAction(final Body body) {
		if (isRengenEnergie) {
			final EntiteVivante entiteVivante = (EntiteVivante) body.getUserData();
			entiteVivante.habiliter.energie[Habiliter.GAIN] = 2;
			if (entiteVivante.isSurLit()) {
				entiteVivante.etat = Etat.DORT;
			}
		}
	}

	public void initAction(final World world, final Body body, final int tempAttente) {
		this.tempsFinAttente = new Temps(Base.getInstance().getTemps());
		tempsFinAttente.addMinute(tempAttente);
	}

	@Override
	public void initAction(final World world, final Body body) {
		if (tempsFinAttente == null) {
			initAction(world, body, Calcul.random(10, 20));
		}
		initFinalAction(body);
	}

	@Override
	public boolean doAction(Body body, World world, World worldAffichage) {
		boolean isActionEnd = false;
		final EntiteVivante entiteVivante = (EntiteVivante) body.getUserData();
		isActionEnd = isActionEnd(entiteVivante);

		return isActionEnd;
	}

	private boolean isActionEnd(final EntiteVivante entiteVivante) {
		boolean isActionEnd = false;
		if (tempsFinAttente != null && Base.getInstance().getTemps().compare(tempsFinAttente) == 1) {
			isActionEnd = true;
		} else if (isRengenEnergie) {
			if (entiteVivante.habiliter.energie[Habiliter.ACTUEL] == entiteVivante.habiliter.energie[Habiliter.MAX]) {
				isActionEnd = true;
			}
		}

		if (isActionEnd) {
			if (isRengenEnergie) {
				entiteVivante.habiliter.energie[Habiliter.GAIN] = -1;
				entiteVivante.etat = Etat.NORMAL;
			}
		}
		return isActionEnd;
	}

}
