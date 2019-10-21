package com.ultraime.game.metier.travail;

import java.util.ArrayDeque;

import com.badlogic.gdx.physics.box2d.Body;
import com.ultraime.database.base.Base;
import com.ultraime.database.entite.ElementEarth;
import com.ultraime.game.entite.EntiteVivante;
import com.ultraime.game.metier.WorldService;
import com.ultraime.game.metier.pathfinding.AetoileDestinationBlockException;
import com.ultraime.game.metier.pathfinding.AetoileException;
import com.ultraime.game.metier.pathfinding.Noeud;
import com.ultraime.game.metier.travail.action.AEConstruction;
import com.ultraime.game.metier.travail.action.AEPrendre;
import com.ultraime.game.metier.travail.action.AERecolte;
import com.ultraime.game.utile.Parametre;

public class MetierAgriculteur extends Metier {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ElementEarth elementEarthOld;

	public MetierAgriculteur(final EntiteVivante entiteVivante, final int priorite) {
		super(entiteVivante, priorite);
	}

	@Override
	public boolean doMetier() {
		boolean isAction = isActionEncours();
		if (!isAction) {
			// option 1 :
			isAction = rechercheElementARamasser();
			if (!isAction) {
				// option 2 : on fait la recolte
				isAction = rechercherRecolte();
			}
			// option 3 : on apporte les légumes dans un coffre.
			if (!isAction) {
				isAction = rangerElement(ElementEarth.legume);
			}
			// option 4 : on plante
			if (!isAction) {
				isAction = rechercherCulture();
			}

		}
		return isAction;
	}

	private boolean rechercheElementARamasser() {
		boolean isDoAction = false;
		ElementEarth elementAramasser = null;
		do {
			elementAramasser = Base.getInstance().baseObjetAConstruire.getElementARecuperer(elementAramasser,ElementEarth.prendre);
			if (elementAramasser != null) {
				isDoAction = verifierAccessibilite(true, elementAramasser, this.entiteVivante, true);
				if (isDoAction) {
					AEPrendre actionEntite = new AEPrendre(0);
					actionEntite.ajouterElementArecolter(elementAramasser);
					this.entiteVivante.ajouterAction(actionEntite);
					Base.getInstance().baseObjetAConstruire.retirerElementAconstruireSaufAction(elementAramasser.x, elementAramasser.y);
				}
			}
		} while (elementAramasser != null && !isDoAction);
		return isDoAction;
	}

	/**
	 * recherche les plantes prêtes à être récolté
	 * 
	 * @return isDoAction
	 */
	private boolean rechercherRecolte() {
		boolean isDoAction = false;
		final AERecolte actionEntiteRecolte = new AERecolte(0);
		final ElementEarth elementEarth = getUneCulturePrete();
		if (elementEarth != null) {
			// si un ObjetSOl est présent sur la case, on ne peut pas recolter l'élément.
			if (!Base.getInstance().baseObjetSol.isObjetPresent(elementEarth.x, elementEarth.y)) {

				if (this.entiteVivante.inventaire.placeDisponible) {
					isDoAction = true;
					isDoAction = verifierAccessibiliteCulture(true, elementEarth, this.entiteVivante);
					if (isDoAction) {
						final float poidsTotal = elementEarth.nombreRecolte * elementEarth.poids;
						if (this.entiteVivante.inventaire.placeSuffisante(poidsTotal)) {
							actionEntiteRecolte.ajouterElementArecolter(elementEarth);
							this.entiteVivante.ajouterAction(actionEntiteRecolte);
							ElementEarth elementEarthNew = new ElementEarth(
									Base.getInstance().recupererElementEarthByNom(elementEarth.elementEarthEvolution),
									elementEarth.x, elementEarth.y);
							Base.getInstance().ajouterElementEarth(elementEarthNew);
							isDoAction = true;
							this.elementEarthOld = null;
						} else {
							isDoAction = false;
						}
					} else {
						this.elementEarthOld = elementEarth;
					}
				}

			}

		}
		return isDoAction;
	}

	/**
	 * Permet de récupérer une culture. Un système de "old" est mis en place pour
	 * récupérer la prochaine plante si la premiere n'est pas dispo
	 * 
	 * @return elementEarth
	 */
	public ElementEarth getUneCulturePrete() {
		ElementEarth elementEarth = null;
		if (this.elementEarthOld != null) {
			elementEarth = Base.getInstance().baseCulture.rechercherElementARecolterNext(elementEarthOld);
		}
		if (elementEarth == null) {
			elementEarth = Base.getInstance().baseCulture.rechercherElementARecolter();
			this.elementEarthOld = null;
		}
		return elementEarth;
	}

	/**
	 * recherche le sol prêt à accueillir une graine
	 * 
	 * @return isDoAction
	 */
	private boolean rechercherCulture() {
		boolean isDoAction = false;

		AEConstruction actionEntite = new AEConstruction(0);
		ElementEarth elementEarth = Base.getInstance().baseCulture.rechercherElementAcultiver();

		if (elementEarth != null) {
			isDoAction = true;
			isDoAction = verifierAccessibiliteCulture(isDoAction, elementEarth, this.entiteVivante);
			if (isDoAction) {
				actionEntite.ajouterElementAconstruire(elementEarth);
				this.entiteVivante.ajouterAction(actionEntite);
			}
		}

		return isDoAction;
	}

	public boolean verifierAccessibiliteCulture(boolean isDoAction, final ElementEarth element,
			final EntiteVivante ev) {
		final Body body = WorldService.getInstance().recupererBodyFromEntite(ev);
//		final World world = WorldService.getInstance().world;
		final int xDepart = Math.round(body.getPosition().x);
		final int yDepart = Math.round(body.getPosition().y);
		Noeud noeudDepart = new Noeud(xDepart, yDepart, 0);
		final Noeud noeudDestination = new Noeud(element.x, element.y, 0);
//		Aetoile aetoile = new Aetoile(world, body);
		try {
			ArrayDeque<Noeud> cheminPlusCourt = aetoile.cheminPlusCourt(noeudDestination, noeudDepart,
					Parametre.AETOILE_BASE_2);
			ev.setListeDeNoeudDeplacement(cheminPlusCourt);
		} catch (AetoileException e) {
			isDoAction = false;
		} catch (AetoileDestinationBlockException e) {
			isDoAction = false;
		}
		return isDoAction;
	}

}
