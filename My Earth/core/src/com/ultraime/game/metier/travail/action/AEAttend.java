package com.ultraime.game.metier.travail.action;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.ultraime.database.base.Base;
import com.ultraime.game.metier.Temps;
import com.ultraime.game.utile.Calcul;

public class AEAttend extends ActionEntite {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Temps tempsFinAttente;

	public AEAttend(int priorite) {
		super(priorite);
	}

	public void initAction(final World world, final Body body, final int tempAttente) {
		tempsFinAttente = new Temps(Base.getInstance().getTemps());
		tempsFinAttente.addMinute(tempAttente);

	}

	@Override
	public boolean doAction(Body body, World world, World worldAffichage) {
		boolean isActionEnd = false;
		if (Base.getInstance().getTemps().compare(tempsFinAttente) == 1) {
			isActionEnd = true;
		}
		return isActionEnd;
	}

	@Override
	public void initAction(final World world, final Body body) {
		initAction(world, body, Calcul.random(15, 30));

	}

}
