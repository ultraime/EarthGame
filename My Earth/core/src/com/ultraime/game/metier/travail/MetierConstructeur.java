package com.ultraime.game.metier.travail;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.ultraime.database.ElementEarth;
import com.ultraime.database.base.Base;
import com.ultraime.game.entite.EntiteVivante;
import com.ultraime.game.metier.WorldService;
import com.ultraime.game.metier.travail.action.AEConstruction;

public class MetierConstructeur extends Metier {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MetierConstructeur(final EntiteVivante entiteVivante, final int priorite) {
		super(entiteVivante, priorite);
		final Body body = WorldService.getInstance().recupererBodyFromEntite(entiteVivante);
		final World world = WorldService.getInstance().world;
		initAetoile(body, world);
	}

	@Override
	public boolean doMetier() {
		// on regarde si l'entite à déja une construction à faire.
		boolean isAction = isActionEncours();
		if (!isAction) {
			isAction = rechercherUneConstruction();
		}
		return isAction;
	}

	private boolean rechercherUneConstruction() {
		boolean isDoAction = false;
		AEConstruction actionEntite = new AEConstruction(0);
		ElementEarth elementAconstruire  = Base.getInstance().baseObjetAConstruire.getElementAConstruire();
		if (elementAconstruire != null) {
			isDoAction = true;
			// si l'élément à construire est accessible par le personnage
			isDoAction = verifierAccessibilite(isDoAction, elementAconstruire, this.entiteVivante, false);
			if (isDoAction) {
				actionEntite.ajouterElementAconstruire(elementAconstruire);
				this.entiteVivante.ajouterAction(actionEntite);
			}
		}
		return isDoAction;
	}

}
