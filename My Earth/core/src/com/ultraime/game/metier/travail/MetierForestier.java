package com.ultraime.game.metier.travail;

import com.ultraime.database.base.Base;
import com.ultraime.database.entite.ElementEarth;
import com.ultraime.game.entite.EntiteVivante;
import com.ultraime.game.metier.travail.action.AEConstruction;

public class MetierForestier extends Metier {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MetierForestier(final EntiteVivante entiteVivante, final int priorite) {
		super(entiteVivante, priorite);

	}

	@Override
	public boolean doMetier() {
		// on regarde si l'entite à déja une construction à faire.
		boolean isAction = isActionEncours();
		if (!isAction) {
			isAction = rechercherElementAcouper();
		}
		if (!isAction) {
			isAction = rangerElement(ElementEarth.materiaux_bois);
		}
		return isAction;
	}

	private boolean rechercherElementAcouper() {
		ElementEarth elementAcouper = null;
		boolean isDoAction = false;
		do {
			elementAcouper = Base.getInstance().baseObjetAConstruire.getElementARecuperer(elementAcouper,ElementEarth.couper);
			if (elementAcouper != null) {
				isDoAction = verifierAccessibilite(true, elementAcouper, this.entiteVivante, true);
				if (isDoAction) {
					AEConstruction actionEntite = new AEConstruction(0);
					actionEntite.ajouterElementAconstruire(elementAcouper);
					this.entiteVivante.ajouterAction(actionEntite);
				}
			}
		} while (elementAcouper != null && !isDoAction);

		return isDoAction;
	}

}
