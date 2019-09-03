package com.ultraime.game.metier.travail;

import java.util.ArrayDeque;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.ultraime.database.ElementEarth;
import com.ultraime.database.base.Base;
import com.ultraime.game.entite.EntiteVivante;
import com.ultraime.game.metier.WorldService;
import com.ultraime.game.metier.pathfinding.AetoileDestinationBlockException;
import com.ultraime.game.metier.pathfinding.AetoileException;
import com.ultraime.game.metier.pathfinding.Noeud;
import com.ultraime.game.metier.travail.action.AEConstruction;
import com.ultraime.game.metier.travail.action.AEDeposerElementDansCoffre;
import com.ultraime.game.metier.travail.action.AERecolte;
import com.ultraime.game.utile.Parametre;

public class MetierAgriculteur extends Metier {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ElementEarth elementEarthOld;

	public MetierAgriculteur(EntiteVivante entiteVivante) {
		super(entiteVivante);
		final Body body = WorldService.getInstance().recupererBodyFromEntite(entiteVivante);
		final World world = WorldService.getInstance().world;
		initAetoile(body, world);
	}

	@Override
	public boolean doMetier() {
		//TODO a revoir, car quand plein, le perso ne fait plus rien
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
		//TODO attention, si coffre pas Accessible, rien ne sera fait..
		boolean isDoAction = false;
		final AEDeposerElementDansCoffre aeDeposerElementDansCoffre = new AEDeposerElementDansCoffre(0);
		final ElementEarth elementEarth = Base.getInstance().rechercherCoffreDisponible();
		if (elementEarth != null) {
			isDoAction = true;
			isDoAction = verifierAccessibilite(isDoAction, elementEarth, this.entiteVivante,true);
			if (isDoAction) {;
				aeDeposerElementDansCoffre.ajouterCible(elementEarth);
				this.entiteVivante.ajouterAction(aeDeposerElementDansCoffre);
			}
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
		final ElementEarth elementEarth = rechercherUneCulturePrete();
		if (elementEarth != null) {
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
					}else{
						isDoAction = false;
					}
				} else {
					this.elementEarthOld = elementEarth;
				}
			}

		}
		return isDoAction;
	}

	/**
	 * Permet de récupérer une culture. Un système de "old" est mis en place
	 * pour récupérer la prochaine plante si la premiere n'est pas dispo
	 * 
	 * @return elementEarth
	 */
	public ElementEarth rechercherUneCulturePrete() {
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
	private boolean tryFindCulture() {
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

	public  boolean verifierAccessibiliteCulture(boolean isDoAction, final ElementEarth element,
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
