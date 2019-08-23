package com.ultraime.game.metier.travail;

import com.ultraime.database.ElementEarth;
import com.ultraime.game.entite.EntiteVivante;
import com.ultraime.game.metier.TileMapService;
import com.ultraime.game.metier.travail.action.AEConstruction;

public class MetierConstructeur extends Metier {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MetierConstructeur(EntiteVivante entiteVivante) {
		super(entiteVivante);
	}

	@Override
	public boolean doMetier() {
		// on regarde si l'entite à déja une construction à faire.
		boolean isAction = isActionEncours();
		if (!isAction) {
			isAction = tryFindConstruct();
		}
		return isAction;
	}

	private boolean tryFindConstruct() {
		boolean isDoAction = false;

		AEConstruction actionEntite = new AEConstruction(0);
		ElementEarth elementAconstruire = TileMapService.getInstance().getElementAConstruire();
		if (elementAconstruire != null) {
			actionEntite.ajouterElementAconstruire(elementAconstruire);
			this.entiteVivante.ajouterAction(actionEntite);

			isDoAction = true;
		}
		return isDoAction;
	}

}
