package com.ultraime.game.metier.travail.action;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.ultraime.database.ElementEarth;
import com.ultraime.database.base.Base;
import com.ultraime.game.entite.EntiteVivante;
import com.ultraime.game.metier.TileMapService;

public class AEConstruction extends ActionEntite {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public List<ElementEarth> elementAconstruires;
	
	public AEConstruction(int priorite) {
		super(priorite);
		this.elementAconstruires = new ArrayList<>();
	}

	@Override
	public boolean doAction(Body body, World world, World worldAffichage) {
		boolean isActionEnd = false;
		// on regare si le personnage doit se deplacer.
		boolean isDeplacementEnd = doDeplacement(body, worldAffichage);

		if (isDeplacementEnd) {
			ElementEarth elem = elementAconstruires.get(0);
			TileMapService.getInstance().construireItem(elem);
			Base.getInstance().retirerRectangleConstructible(elem.x, elem.y);
			elementAconstruires.remove(0);
			if (elementAconstruires.size() == 0) {
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
		//TODO a décommenter ?
//		MetierConstructeur.verifierAccessibilite(false, elementAconstruires.get(0), (EntiteVivante) body.getUserData());
	}

	public void ajouterElementAconstruire(final ElementEarth elementAconstruire) {
		elementAconstruires.add(elementAconstruire);
	}

}
