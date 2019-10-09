package com.ultraime.game.metier.travail;

import java.util.ArrayList;
import java.util.List;

import com.ultraime.database.base.Base;
import com.ultraime.database.entite.ElementEarth;
import com.ultraime.database.entite.Materiau;
import com.ultraime.game.entite.EntiteVivante;
import com.ultraime.game.metier.travail.action.AEConstruction;
import com.ultraime.game.metier.travail.action.AERecupererElementDansCoffre;

public class MetierConstructeur extends Metier {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MetierConstructeur(final EntiteVivante entiteVivante, final int priorite) {
		super(entiteVivante, priorite);
	}

	@Override
	public boolean doMetier() {
		// on regarde si l'entite à déja une construction à faire.
		boolean isAction = isActionEncours();
		if (!isAction) {
			isAction = rechercherUneConstruction();
		}
		return isAction;
	}

	private boolean rechercherUneConstruction() {
		boolean isDoAction = false;
		AEConstruction actionEntite = new AEConstruction(0);
		ElementEarth elementAconstruire = Base.getInstance().baseObjetAConstruire.getElementAConstruire();
		if (elementAconstruire != null) {
			isDoAction = true;
			// si l'élément à construire est accessible par le personnage
			isDoAction = verifierAccessibilite(isDoAction, elementAconstruire, this.entiteVivante, false);
			if (isDoAction) {
				isDoAction = rechercherMateriaux(elementAconstruire);
				if (isDoAction) {
					actionEntite.ajouterElementAconstruire(elementAconstruire);
					this.entiteVivante.ajouterAction(actionEntite);
				}
			}
		}
		return isDoAction;
	}

	/**
	 * @param elementAconstruire
	 * @return isDoAction
	 */
	private boolean rechercherMateriaux(ElementEarth elementAconstruire) {
		boolean materiauxTrouve = false;
		float placeDisponible = this.entiteVivante.inventaire.espaceDisponnible();
		if (elementAconstruire.materiaux_requis.size() == 0) {
			materiauxTrouve = true;
		}

		for (int i = 0; i < elementAconstruire.materiaux_requis.size(); i++) {
			final Materiau materiau = elementAconstruire.materiaux_requis.get(i);
			if (placeDisponible > Base.getInstance().recupererElementEarthByNom(materiau.nom).poids) {
				List<ElementEarth> elementARecuperer = new ArrayList<ElementEarth>();
				int nbAChercher = materiau.nombreRequis - materiau.nombreActuel;
				Boolean isDoAction = true;
				// TODO En cours : rechercher les materiaux de type "BOIS" ou
				// autre dans les
				// coffres.
				while (nbAChercher > 0 && isDoAction) {
					ElementEarth coffre = null;

					// on boucle temps que l'on a pas de coffre dispo. Ou si
					// dans la liste rien
					// n'est dispo
					do {
						coffre = Base.getInstance().rechercherCoffreAvecElement(coffre, materiau.nom);
						if (coffre != null) {
							isDoAction = verifierAccessibilite(true, coffre, this.entiteVivante, true);
						} else {
							isDoAction = false;
						}
					} while (coffre != null && !isDoAction);

					if (isDoAction) {
						// On test si dans l'inventaire du personnage on peut
						// prendre les éléments.

						List<ElementEarth> materiauARecuperer = coffre.inventaire
								.recupererAllElementByNom(materiau.nom);
						int elementRecuperable = 0;
						for (int j = 0; j < materiauARecuperer.size(); j++) {
							final ElementEarth elementARecupererEarth = materiauARecuperer.get(j);
							placeDisponible = placeDisponible - elementARecupererEarth.poids;
							if (placeDisponible >= 0) {
								elementRecuperable = elementRecuperable + 1;
								nbAChercher = nbAChercher - elementRecuperable;
								elementARecuperer.add(elementARecupererEarth);
								if (nbAChercher == 0) {
									break;
								}
							} else {
								break;
							}
						}
						if (elementRecuperable > 0) {
							final AERecupererElementDansCoffre aeRecupererElementDansCoffre = new AERecupererElementDansCoffre(
									0);
							aeRecupererElementDansCoffre.elemenCible = coffre;
							aeRecupererElementDansCoffre.elementARecuperer = elementARecuperer;
							this.entiteVivante.ajouterAction(aeRecupererElementDansCoffre);

							materiauxTrouve = true;
						}
					}
				}
			}
		}
		return materiauxTrouve;
	}

}
