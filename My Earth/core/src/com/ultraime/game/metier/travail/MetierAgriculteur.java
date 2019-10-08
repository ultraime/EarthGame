package com.ultraime.game.metier.travail;

import java.util.ArrayDeque;

import com.badlogic.gdx.physics.box2d.Body;
import com.ultraime.database.ElementEarth;
import com.ultraime.database.base.Base;
import com.ultraime.game.entite.EntiteVivante;
import com.ultraime.game.metier.WorldService;
import com.ultraime.game.metier.pathfinding.AetoileDestinationBlockException;
import com.ultraime.game.metier.pathfinding.AetoileException;
import com.ultraime.game.metier.pathfinding.Noeud;
import com.ultraime.game.metier.travail.action.AEConstruction;
import com.ultraime.game.metier.travail.action.AEDeposerElementDansCoffre;
import com.ultraime.game.metier.travail.action.AERamasserObjetSol;
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
			// option 1 : on fait la recolte
			isAction = rechercherRecolte();
			// option 2 : on apporte les légumes dans un coffre.
			if (!isAction) {
				isAction = rangerLegume();
			}
			// option 3 : on plante
			if (!isAction) {
				isAction = rechercherCulture();
			}

		}
		return isAction;
	}

	private boolean rangerLegume() {
		final ElementEarth coffre = rechercherCoffre();
		boolean isDoAction = false;
		float poids = 0;
		final float poidsMax = this.entiteVivante.inventaire.capaciteMax;
		final float poidsActuel = this.entiteVivante.inventaire.capaciteActuel;
		boolean aRamasserUnLegume = false;
		if (coffre != null) {
			// un coffre est bien dispo, on recherche alors un legume.
			ElementEarth legume = null;
			do {
				legume = Base.getInstance().baseObjetSol.rechercheObjetSol(ElementEarth.legume, legume);
				if (legume != null) {
					if (verifierAccessibilite(true, legume, this.entiteVivante, true)) {
						// Un legume est bien disponible.

						// deux action à faire : Ramasser les legumes et le deposer dans le coffre
						final AERamasserObjetSol aeRamasserObjetSol = new AERamasserObjetSol(0);
						aeRamasserObjetSol.ajouterCible(legume);
						this.entiteVivante.ajouterAction(aeRamasserObjetSol);
						poids += legume.poids;
						aRamasserUnLegume = true;
					}
				}
			} while (legume != null && poidsMax > poidsActuel + poids);

			if (aRamasserUnLegume) {
				final AEDeposerElementDansCoffre aeDeposerElementDansCoffre = new AEDeposerElementDansCoffre(0);
				aeDeposerElementDansCoffre.ajouterCible(coffre);
				this.entiteVivante.ajouterAction(aeDeposerElementDansCoffre);
				isDoAction = true;
			}

		}
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
