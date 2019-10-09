package com.ultraime.game.metier.travail;

import java.util.ArrayDeque;
import java.util.List;

import com.ultraime.database.base.Base;
import com.ultraime.database.entite.ElementEarth;
import com.ultraime.game.entite.EntiteVivante;
import com.ultraime.game.entite.Habiliter;
import com.ultraime.game.metier.Temps;
import com.ultraime.game.metier.pathfinding.Noeud;
import com.ultraime.game.metier.travail.action.AEAttend;
import com.ultraime.game.metier.travail.action.AEDeplacement;

public class MetierGeneral extends Metier {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MetierGeneral(EntiteVivante entiteVivante, final int priorite) {
		super(entiteVivante, priorite);
	}

	@Override
	public boolean doMetier() {
		// on regarde si l'entite à déja une construction à faire.
		boolean isAction = isActionEncours();
		if (!isAction) {
			if (this.entiteVivante.habiliter.hydratation[Habiliter.ACTUEL] < 20) {
				isAction = rechercheEau();
			}
			if (!isAction && this.entiteVivante.habiliter.energie[Habiliter.ACTUEL] < 20) {
				if (!this.entiteVivante.isSurLit()) {
					isAction = rechercheLit();
				} else {
					// le perso est dans le lit.
					isAction = dormir();
				}
			}

		}
		return isAction;
	}

	private boolean rechercheEau() {
		boolean isDoAction = false;
		// final AEDeplacement adAeDeplacement = new AEDeplacement(xDestination,
		// yDestination, priorite)
		List<ElementEarth> earths = Base.getInstance().baseMeuble.getElementEarthMeubles();
		for (int i = 0; i < earths.size(); i++) {
			final ElementEarth elementEarth = earths.get(i);
			if (elementEarth.sousType != null && elementEarth.sousType.equals(ElementEarth.aqua)) {
				isDoAction = verifierAccessibilite(isDoAction, elementEarth, this.entiteVivante, true);
			}
			if (isDoAction) {
				ArrayDeque<Noeud> cheminPlusCourt = this.entiteVivante.getListeDeNoeudDeplacement();
				Noeud lastNoeud = cheminPlusCourt.getLast();
				AEDeplacement aeDeplacement = new AEDeplacement(lastNoeud.x, lastNoeud.y, 0);
				this.entiteVivante.ajouterAction(aeDeplacement);
				break;
			}

		}
		return isDoAction;
	}

	private boolean dormir() {
		AEAttend aeAttend = new AEAttend(0, Temps.HUIT_HEURES, true);
		this.entiteVivante.ajouterAction(aeAttend);
		return true;
	}

	private boolean rechercheLit() {
		boolean litTrouve = false;
		final ElementEarth lit = this.entiteVivante.lit;
		if (lit != null) {
			AEDeplacement actionEntite = new AEDeplacement(lit.x, lit.y, 0);
			this.entiteVivante.ajouterAction(actionEntite);
			litTrouve = true;
		}
		return litTrouve;
	}

}
