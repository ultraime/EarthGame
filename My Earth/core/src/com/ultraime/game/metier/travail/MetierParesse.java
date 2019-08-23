package com.ultraime.game.metier.travail;

import com.ultraime.game.entite.EntiteVivante;
import com.ultraime.game.metier.travail.action.AEAttend;
import com.ultraime.game.metier.travail.action.AEDeplacement;
import com.ultraime.game.utile.Calcul;

public class MetierParesse extends Metier {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MetierParesse(EntiteVivante entiteVivante) {
		super(entiteVivante);
	}

	@Override
	public boolean doMetier() {
		boolean isAction = isActionEncours();
		// on regarde si une action est déjà en cours.
		if (!isAction) {
			isAction = findAction();
		}
		return isAction;
	}

	private boolean findAction() {
		boolean isDoAction = true;
		if (Calcul.random(0, 1) == 0) {
			ajouterActionAttente();
		} else {
			ajouterActionDeplacement();

		}

		return isDoAction;
	}

	private void ajouterActionDeplacement() {
		int x = (int) this.entiteVivante.x;
		int y = (int) this.entiteVivante.y;
		final int addX = Calcul.random(-2, 2);
		final int addY = Calcul.random(-2, 2);
		x += addX;
		y += addY;
		AEDeplacement actionEntite = new AEDeplacement(x, y, 0);
		this.entiteVivante.ajouterAction(actionEntite);

	}

	private void ajouterActionAttente() {
		AEAttend actionEntite = new AEAttend(0);
		this.entiteVivante.ajouterAction(actionEntite);

	}

}
