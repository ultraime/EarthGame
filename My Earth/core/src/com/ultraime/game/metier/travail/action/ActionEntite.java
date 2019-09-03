package com.ultraime.game.metier.travail.action;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.ultraime.game.entite.EntiteVivante;
import com.ultraime.game.metier.WorldService;
import com.ultraime.game.metier.pathfinding.Noeud;
import com.ultraime.game.utile.Parametre;

/**
 * @author Ultraime implements Comparable<ActionEntite>
 */
public abstract class ActionEntite implements Comparable<ActionEntite>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int priorite = 0;

	public abstract void initAction(World world, Body body);

	/**
	 * @param typeAction
	 * @param priorite
	 */
	public ActionEntite(final int priorite) {
		this.priorite = priorite;
	}

	/**
	 * @param typeAction
	 * @param priorite
	 */
	public ActionEntite(final int xDestination, final int yDestination, final int priorite) {
		this(priorite);

	}

	/**
	 * @param entiteVivante
	 * @param world
	 * @param worldAffichage
	 */
	public abstract boolean doAction(final Body body, final World world, final World worldAffichage);

	/**
	 * @param body
	 * @param worldAffichage
	 * @return true si déplacement fini
	 */
	protected boolean doDeplacement(final Body body, final World worldAffichage) {
		boolean isDeplacementEnd = true;
		int direction = -1;
		if (body.getUserData() instanceof EntiteVivante) {
			final EntiteVivante ev = (EntiteVivante) body.getUserData();
			final ArrayDeque<Noeud> noeudDeplacements = ev.getListeDeNoeudDeplacement();
			if (noeudDeplacements.size() > 0) {
				isDeplacementEnd = false;
				final float valeurArrondi = 0.05f * Parametre.VITESSE_DE_JEU;
				final Noeud noeuddeplacementencours = noeudDeplacements.getFirst();
				final float posX = body.getPosition().x;
				final float posY = body.getPosition().y;
				float xVelocity = 0;
				float yVelocity = 0;
				float vitesse = ev.habiliter.vitesse * Parametre.VITESSE_DE_JEU;
				if (posX + valeurArrondi < noeuddeplacementencours.x) {
					xVelocity = vitesse;
					direction = EntiteVivante.DROITE;
				} else if (posX - valeurArrondi > noeuddeplacementencours.x) {
					xVelocity = -vitesse;
					direction = EntiteVivante.GAUCHE;
				}
				if (posY + valeurArrondi < noeuddeplacementencours.y) {
					yVelocity = vitesse;
					direction = EntiteVivante.HAUT;
				} else if (posY - valeurArrondi > noeuddeplacementencours.y) {
					yVelocity = -vitesse;
					direction = EntiteVivante.BAS;
				}
				float xDiff = posX - noeuddeplacementencours.x;
				float yDiff = posY - noeuddeplacementencours.y;

				body.setLinearVelocity(xVelocity, yVelocity);

				if (xVelocity == 0 && yVelocity == 0 || xDiff < valeurArrondi && xDiff > -valeurArrondi
						&& yDiff < valeurArrondi && yDiff > -valeurArrondi) {
					noeudDeplacements.removeFirst();
					if (noeudDeplacements.size() == 0) {
						body.setLinearVelocity(0, 0);
						isDeplacementEnd = true;
					}
				}
			}
		}
		try {
			ArrayList<Body> bodiesAffichage = WorldService.getInstance().bodiesAffichageEntiteVivant;
			final EntiteVivante ev = (EntiteVivante) body.getUserData();
			for (int i = 0; i < bodiesAffichage.size(); i++) {
				final Body bodyAfficher = bodiesAffichage.get(i);
				if (bodyAfficher.getUserData() instanceof EntiteVivante) {
					final EntiteVivante evAfficher = (EntiteVivante) bodyAfficher.getUserData();
					if (evAfficher.id == ev.id) {
						bodyAfficher.setTransform(body.getPosition().x * 64 + 32, body.getPosition().y * 64 + 32, 0);
						evAfficher.x = body.getPosition().x;
						evAfficher.y = body.getPosition().y;
						if (direction != -1) {
							evAfficher.setDirection(direction);
						}
						break;
					}
				}
			}
		} catch (GdxRuntimeException e) {
			if (Parametre.MODE_DEBUG) {
				e.printStackTrace();
			}
			doDeplacement(body, worldAffichage);
		}
		return isDeplacementEnd;
	}

	@Override
	public int compareTo(ActionEntite o) {
		int retour = 0;
		if (this.priorite < o.priorite) {
			retour = -1;
		} else if (this.priorite < o.priorite) {
			retour = 1;
		}
		return retour;
	}

}
