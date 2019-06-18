package com.ultraime.game.metier.travail;

import java.util.List;

import com.ultraime.game.entite.EntiteVivante;
import com.ultraime.game.metier.travail.action.ActionEntite;

public abstract class Metier {

	protected EntiteVivante entiteVivante;

	public Metier(final EntiteVivante entiteVivante) {
		super();
		this.entiteVivante = entiteVivante;
	}

	/**
	 * @return true = action à faire. false = rien à faire
	 */
	public abstract boolean doMetier();

	public boolean isActionEncours() {
		boolean isAction = false;
		List<ActionEntite> actionEntites = this.entiteVivante.getListeAction();
		if (!actionEntites.isEmpty()) {
			isAction = true;
		}
		// for (ActionEntite ac : actionEntites) {
		// if (ac instanceof ActionEntiteConstruction) {
		// isAction = true;
		// break;
		// }
		return isAction;
	}
}
