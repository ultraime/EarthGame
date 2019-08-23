package com.ultraime.game.metier.travail;

import com.ultraime.database.ElementEarth;
import com.ultraime.database.base.Base;
import com.ultraime.game.entite.EntiteVivante;
import com.ultraime.game.metier.travail.action.AEConstruction;
import com.ultraime.game.metier.travail.action.AEDeposerElementDansCoffre;
import com.ultraime.game.metier.travail.action.AERecolte;

public class MetierAgriculteur extends Metier {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MetierAgriculteur(EntiteVivante entiteVivante) {
		super(entiteVivante);
	}

	@Override
	public boolean doMetier() {
		boolean isAction = isActionEncours();
		if (!isAction) {
			if (!this.entiteVivante.inventaire.placeDisponible
					|| this.entiteVivante.inventaire.espaceDisponnible() <= 1f) {
				isAction = tryFindCoffre();
			}
			if (!isAction) {
				isAction = tryFindRecolte();
				if (!isAction) {
					isAction = tryFindCulture();
				}
			}
		}
		return isAction;
	}

	private boolean tryFindCoffre() {
		boolean isDoAction = false;
		final AEDeposerElementDansCoffre aeDeposerElementDansCoffre = new AEDeposerElementDansCoffre(0);
		final ElementEarth elementEarth = Base.getInstance().rechercherCoffreDisponible();
		if (elementEarth != null) {
			aeDeposerElementDansCoffre.ajouterCible(elementEarth);
			this.entiteVivante.ajouterAction(aeDeposerElementDansCoffre);
		}
		return isDoAction;
	}

	/**
	 * recherche les plantes prêtes à être récolté
	 * 
	 * @return isDoAction
	 */
	private boolean tryFindRecolte() {
		boolean isDoAction = false;
		final AERecolte actionEntiteRecolte = new AERecolte(0);
		final ElementEarth elementEarth = Base.getInstance().baseCulture.rechercherElementARecolter();
		if (elementEarth != null) {
			if (this.entiteVivante.inventaire.placeDisponible) {
				final float poidsTotal = elementEarth.nombreRecolte * elementEarth.poids;
				if (this.entiteVivante.inventaire.placeSuffisante(poidsTotal)) {
					actionEntiteRecolte.ajouterElementArecolter(elementEarth);
					this.entiteVivante.ajouterAction(actionEntiteRecolte);
					ElementEarth elementEarthNew = new ElementEarth(
							Base.getInstance().recupererElementEarthByNom(elementEarth.elementEarthEvolution),
							elementEarth.x, elementEarth.y);
					Base.getInstance().ajouterElementEarth(elementEarthNew);
					isDoAction = true;
				}
			}

		}
		return isDoAction;
	}

	/**
	 * echerche le sol prêt à accueillir une graine
	 * 
	 * @return isDoAction
	 */
	private boolean tryFindCulture() {
		boolean isDoAction = false;

		AEConstruction actionEntite = new AEConstruction(0);
		ElementEarth elementEarth = Base.getInstance().baseCulture.rechercherElementAcultiver();

		if (elementEarth != null) {
			actionEntite.ajouterElementAconstruire(elementEarth);
			this.entiteVivante.ajouterAction(actionEntite);
			isDoAction = true;
		}

		return isDoAction;
	}

}
