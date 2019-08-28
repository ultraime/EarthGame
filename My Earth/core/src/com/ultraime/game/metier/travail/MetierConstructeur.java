package com.ultraime.game.metier.travail;

import java.util.ArrayDeque;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.ultraime.database.ElementEarth;
import com.ultraime.game.entite.EntiteVivante;
import com.ultraime.game.metier.TileMapService;
import com.ultraime.game.metier.WorldService;
import com.ultraime.game.metier.pathfinding.Aetoile;
import com.ultraime.game.metier.pathfinding.AetoileDestinationBlockException;
import com.ultraime.game.metier.pathfinding.AetoileException;
import com.ultraime.game.metier.pathfinding.Noeud;
import com.ultraime.game.metier.travail.action.AEConstruction;
import com.ultraime.game.utile.Parametre;

public class MetierConstructeur extends Metier {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Aetoile aetoile;

	public MetierConstructeur(EntiteVivante entiteVivante) {
		super(entiteVivante);
		final Body body = WorldService.getInstance().recupererBodyFromEntite(entiteVivante);
		final World world = WorldService.getInstance().world;
		aetoile = new Aetoile(world, body);
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
			isDoAction = true;
			// si l'élément à construire est accessible par le personnage
			isDoAction = verifierAccessibilite(isDoAction, elementAconstruire, this.entiteVivante);
			if (isDoAction) {
				actionEntite.ajouterElementAconstruire(elementAconstruire);
				this.entiteVivante.ajouterAction(actionEntite);
			}
		}
		return isDoAction;
	}

	/**
	 * @param isDoAction
	 * @param elementAconstruire
	 * @return isDoAction (Accessible)
	 */
	public boolean verifierAccessibilite(boolean isDoAction, final ElementEarth elementAconstruire,
			final EntiteVivante ev) {
		final Body body = WorldService.getInstance().recupererBodyFromEntite(entiteVivante);
//		final World world = WorldService.getInstance().world;
		final int xDepart = Math.round(body.getPosition().x);
		final int yDepart = Math.round(body.getPosition().y);
		Noeud noeudDepart = new Noeud(xDepart, yDepart, 0);

//		Aetoile aetoile = new Aetoile(world, body);

		// on essaie d'aller en bas sinon à gauche sinon en haut, sinon à
		// droite sinon à droite. si ce n'est pas possible on annule
		// l'action
		for (int i = 0; i < 4; i++) {
			try {
				int decalageX = 0;
				int decalageY = 0;
				if (i == 0) {
					decalageY = -1;
				} else if (i == 1) {
					decalageX = -1;
				} else if (i == 2) {
					decalageY = 1;
				} else if (i == 3) {
					decalageX = 1;
				}
				final Noeud noeudDestination = new Noeud((elementAconstruire.x + decalageX),
						(elementAconstruire.y + decalageY), 0);

				aetoile.setCollisionEntiteConstructible(true);
				ArrayDeque<Noeud> cheminPlusCourt = aetoile.cheminPlusCourt(noeudDestination, noeudDepart,Parametre.AETOILE_BASE);
				ev.setListeDeNoeudDeplacement(cheminPlusCourt);
				// si c'est ok on sort de la boucle
				break;
			} catch (AetoileException e) {
				if (i == 3) {
					isDoAction = false;
					TileMapService.ajouterElementAconstruireNEW(elementAconstruire);
				}

			} catch (AetoileDestinationBlockException e) {
				if (i == 3) {
					isDoAction = false;
					TileMapService.ajouterElementAconstruireNEW(elementAconstruire);
				}
			}
		}
		return isDoAction;
	}

}
