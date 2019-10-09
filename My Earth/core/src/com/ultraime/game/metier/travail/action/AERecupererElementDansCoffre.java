package com.ultraime.game.metier.travail.action;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.ultraime.database.entite.ElementEarth;
import com.ultraime.game.entite.EntiteVivante;
import com.ultraime.game.metier.pathfinding.Aetoile;
import com.ultraime.game.metier.pathfinding.AetoileDestinationBlockException;
import com.ultraime.game.metier.pathfinding.AetoileException;
import com.ultraime.game.metier.pathfinding.Noeud;
import com.ultraime.game.utile.Parametre;

public class AERecupererElementDansCoffre extends ActionEntite {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public ElementEarth elemenCible;
	public List<ElementEarth> elementARecuperer = new ArrayList<ElementEarth>();

	public AERecupererElementDansCoffre(int priorite) {
		super(priorite);
	}

	@Override
	public boolean doAction(Body body, World world, World worldAffichage) {
		boolean isActionEnd = false;
		// on regare si le personnage doit se deplacer.
		boolean isDeplacementEnd = doDeplacement(body, worldAffichage);

		if (isDeplacementEnd) {
			EntiteVivante ev = (EntiteVivante) body.getUserData();

			for (int i = 0; i < elementARecuperer.size(); i++) {
				elemenCible.inventaire.retirerElement(elementARecuperer.get(i));
				ev.inventaire.ajouterElement(elementARecuperer.get(i));
			}

			elemenCible = null;
			isActionEnd = true;

		}

		EntiteVivante ev = (EntiteVivante) body.getUserData();
		if (ev.getListeDeNoeudDeplacement().size() == 0 && !isActionEnd) {
			initAction(worldAffichage, body);
		}

		return isActionEnd;
	}

	@Override
	public void initAction(World world, Body body) {
		final int xDepart = Math.round(body.getPosition().x);
		final int yDepart = Math.round(body.getPosition().y);
		Noeud noeudDepart = new Noeud(xDepart, yDepart, 0);
		EntiteVivante ev = (EntiteVivante) body.getUserData();
		Aetoile aetoile = new Aetoile(world, body);
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
				final Noeud noeudDestination = new Noeud((elemenCible.x + decalageX), (elemenCible.y + decalageY), 0);
				aetoile.setCollisionEntiteConstructible(true);
				ArrayDeque<Noeud> cheminPlusCourt = aetoile.cheminPlusCourt(noeudDestination, noeudDepart,
						Parametre.AETOILE_BASE);
				ev.setListeDeNoeudDeplacement(cheminPlusCourt);
				// si c'est ok on sort de la boucle
				break;
			} catch (AetoileException e) {
				if (Parametre.MODE_DEBUG)
					e.printStackTrace();
			} catch (AetoileDestinationBlockException e) {
				// On ne fait rien, c'est une erreur normale.
			}
		}

	}

	/**
	 * @param elementEarth
	 */
	public void ajouterCible(ElementEarth elementEarth) {
		elemenCible = elementEarth;

	}

	public void ajouterElementARecuperer(ElementEarth elementEarth) {
		elementARecuperer.add(elementEarth);

	}
}
