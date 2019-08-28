package com.ultraime.game.metier.travail.action;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.ultraime.game.entite.EntiteVivante;
import com.ultraime.game.metier.pathfinding.Aetoile;
import com.ultraime.game.metier.pathfinding.AetoileDestinationBlockException;
import com.ultraime.game.metier.pathfinding.AetoileException;
import com.ultraime.game.metier.pathfinding.Noeud;
import com.ultraime.game.utile.Parametre;

public class AEDeplacement extends ActionEntite {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Noeud noeudDestination;

	public AEDeplacement(final int xDestination, final int yDestination, final int priorite) {
		super(xDestination, yDestination, priorite);
		this.noeudDestination = new Noeud(xDestination, yDestination, 0);
	}

	@Override
	public boolean doAction(Body body, World world, World worldAffichage) {
		boolean isActionEnd = false;
		// on regare si le personnage doit se deplacer.
		boolean isDeplacementEnd = doDeplacement(body, worldAffichage);

		if (isDeplacementEnd) {
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
		try {
			ev.setListeDeNoeudDeplacement(aetoile.cheminPlusCourt(noeudDestination, noeudDepart,Parametre.AETOILE_BASE));
		} catch (AetoileException e) {
			if (Parametre.MODE_DEBUG_ERR_DEPLACEMENT)
				e.printStackTrace();
		} catch (AetoileDestinationBlockException e) {
			if (Parametre.MODE_DEBUG_ERR_DEPLACEMENT)
				e.printStackTrace();
		}

	}

}
