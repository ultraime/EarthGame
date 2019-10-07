package com.ultraime.game.metier.travail;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.List;

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
import com.ultraime.game.metier.travail.action.ActionEntite;
import com.ultraime.game.utile.Parametre;

public abstract class Metier implements Serializable, Comparable<Metier> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected EntiteVivante entiteVivante;
	protected Aetoile aetoile;
	public int priorite = 0;

	public Metier(final EntiteVivante entiteVivante, final int priorite) {
		super();
		this.entiteVivante = entiteVivante;
		this.priorite = priorite;
	}

	protected void initAetoile(final Body body, final World world) {
		aetoile = new Aetoile(world, body);
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
		return isAction;
	}

	/**
	 * @param isDoAction
	 * @param elementAconstruire
	 * @param ev
	 * @param isRecherche
	 * @return isDoAction (Accessible)
	 */
	public boolean verifierAccessibilite(boolean isDoAction, final ElementEarth elementAconstruire,
			final EntiteVivante ev, boolean isRecherche) {
		final Body body = WorldService.getInstance().recupererBodyFromEntite(entiteVivante);
		final int xDepart = Math.round(body.getPosition().x);
		final int yDepart = Math.round(body.getPosition().y);
		Noeud noeudDepart = new Noeud(xDepart, yDepart, 0);
		// on essaie d'aller en bas sinon à gauche sinon en haut, sinon à
		// droite sinon à droite. si ce n'est pas possible on annule
		// l'action
		int xElementAconstruire = elementAconstruire.x;
		int yElementAconstuire = elementAconstruire.y;

		for (int i = 0; i < 4; i++) {
			try {
				int decalageX = 0;
				int decalageY = 0;
				if (i == 0) {
					decalageY = -1;
					yElementAconstuire = elementAconstruire.y - elementAconstruire.elementY(ElementEarth.min);
				} else if (i == 1) {
					decalageX = -1;
					xElementAconstruire = elementAconstruire.x - elementAconstruire.elementX(ElementEarth.min);
				} else if (i == 2) {
					decalageY = 1;
					yElementAconstuire = elementAconstruire.y + elementAconstruire.elementY(ElementEarth.max);
				} else if (i == 3) {
					decalageX = 1;
					xElementAconstruire = elementAconstruire.x + elementAconstruire.elementX(ElementEarth.max);
					;
				}

				final Noeud noeudDestination = new Noeud((xElementAconstruire + decalageX),
						(yElementAconstuire + decalageY), 0);

				aetoile.setCollisionEntiteConstructible(true);
				ArrayDeque<Noeud> cheminPlusCourt = aetoile.cheminPlusCourt(noeudDestination, noeudDepart,
						Parametre.AETOILE_BASE_2);
				ev.setListeDeNoeudDeplacement(cheminPlusCourt);
				// si c'est ok on sort de la boucle
				break;
			} catch (AetoileException e) {
				if (i == 3) {
					isDoAction = false;
					if (!isRecherche) {
						TileMapService.ajouterElementAconstruireNEW(elementAconstruire);
					}
				}

			} catch (AetoileDestinationBlockException e) {
				if (i == 3) {
					isDoAction = false;
					if (!isRecherche) {
						TileMapService.ajouterElementAconstruireNEW(elementAconstruire);
					}
				}
			}
		}
		return isDoAction;
	}

	@Override
	public int compareTo(Metier metier) {
		return this.priorite < metier.priorite ? -1 : 1;
	}
}
