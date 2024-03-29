package com.ultraime.game.metier.travail.action;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.ultraime.database.Base;
import com.ultraime.database.ElementEarth;
import com.ultraime.game.entite.EntiteVivante;
import com.ultraime.game.metier.pathfinding.Aetoile;
import com.ultraime.game.metier.pathfinding.Aetoile.Noeud;
import com.ultraime.game.metier.pathfinding.AetoileDestinationBlockException;
import com.ultraime.game.metier.pathfinding.AetoileException;
import com.ultraime.game.utile.Parametre;

public class AERecolte extends ActionEntite {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public List<ElementEarth> elementArecolter;

	public AERecolte(int priorite) {
		super(priorite);
		this.elementArecolter = new ArrayList<>();
	}

	@Override
	public boolean doAction(Body body, World world, World worldAffichage) {
		boolean isActionEnd = false;
		// on regare si le personnage doit se deplacer.
		boolean isDeplacementEnd = doDeplacement(body, worldAffichage);

		if (isDeplacementEnd) {
			ElementEarth elem = elementArecolter.get(0);
			// ajout dans l'inventaire du perso l'element.
			EntiteVivante ev = (EntiteVivante) body.getUserData();
			for (int i = 0; i < elem.nombreRecolte; i++) {
				ev.inventaire.ajouterElement(new ElementEarth(elem));
			}

			// on retire l'élément de la carte.
			Base.getInstance().retirerElementEarth(elem);
			elementArecolter.remove(0);
			if (elementArecolter.size() == 0) {
				isActionEnd = true;
			}
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
		ElementEarth elementArecolter = this.elementArecolter.get(0);
		final Noeud noeudDestination = new Noeud((elementArecolter.x), (elementArecolter.y), 0);
		EntiteVivante ev = (EntiteVivante) body.getUserData();
		Aetoile aetoile = new Aetoile(world, body);
		try {
			ev.setListeDeNoeudDeplacement(aetoile.cheminPlusCourt(noeudDestination, noeudDepart, true));
		} catch (AetoileException e) {
			if (Parametre.MODE_DEBUG)
				e.printStackTrace();
		} catch (AetoileDestinationBlockException e) {
			if (Parametre.MODE_DEBUG)
				e.printStackTrace();
		}

	}

	/**
	 * @param elementArecolter
	 */
	public void ajouterElementArecolter(final ElementEarth elementArecolter) {
		this.elementArecolter.add(elementArecolter);
	}

}
