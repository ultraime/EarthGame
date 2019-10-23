package com.ultraime.game.metier.travail.action;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.ultraime.database.base.Base;
import com.ultraime.database.entite.ElementEarth;
import com.ultraime.game.entite.EntiteVivante;
import com.ultraime.game.metier.ElementEarthService;
import com.ultraime.game.metier.TileMapService;
import com.ultraime.game.metier.pathfinding.Aetoile;
import com.ultraime.game.metier.pathfinding.AetoileDestinationBlockException;
import com.ultraime.game.metier.pathfinding.AetoileException;
import com.ultraime.game.metier.pathfinding.Noeud;
import com.ultraime.game.utile.Parametre;

public class AEPrendre extends ActionEntite {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public List<ElementEarth> elementAprendre;

	public AEPrendre(int priorite) {
		super(priorite);
		this.elementAprendre = new ArrayList<>();
	}

	@Override
	public boolean doAction(Body body, World world, World worldAffichage) {
		boolean isActionEnd = false;
		// on regare si le personnage doit se deplacer.
		boolean isDeplacementEnd = doDeplacement(body, worldAffichage);

		if (isDeplacementEnd) {
			ElementEarth actionPrendre = elementAprendre.get(0);
			//pour  l'élément à prendre
			final int posX = actionPrendre.x;
			final int posY = actionPrendre.y;
			
	
			//l'élément à prendre
			final ElementEarth elementAramasser = Base.getInstance().recupererElementEarth(posX, posY);
			
			elementAprendre.remove(0);
			if (elementAprendre.size() == 0) {
				isActionEnd = true;
			}
			// on retire l'élément de la carte.
			TileMapService.getInstance().detruireItem(actionPrendre);
			// placement de l'objet sur la carte.
			ElementEarthService.genererElement(elementAramasser);
//			Base.getInstance().retirerElementEarth(elementAramasser);


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
		ElementEarth elementArecolter = this.elementAprendre.get(0);
		final Noeud noeudDestination = new Noeud((elementArecolter.x), (elementArecolter.y), 0);
		EntiteVivante ev = (EntiteVivante) body.getUserData();
		Aetoile aetoile = new Aetoile(world, body);
		try {
			ev.setListeDeNoeudDeplacement(
					aetoile.cheminPlusCourt(noeudDestination, noeudDepart, Parametre.AETOILE_BASE_2));
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
		this.elementAprendre.add(elementArecolter);
	}

}
