package com.ultraime.game.metier.travail.action;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.ultraime.database.base.Base;
import com.ultraime.game.metier.Temps;

public class AEAttend extends ActionEntite {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Temps tempsFinAttente;

	public AEAttend(int priorite) {
		super(priorite);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void initAction(World world, Body body) {
		tempsFinAttente = new Temps(Base.getInstance().getTemps());
		tempsFinAttente.addMinute(10);

	}

	@Override
	public boolean doAction(Body body, World world, World worldAffichage) {
		boolean isActionEnd = false;
		if (Base.getInstance().getTemps().compare(tempsFinAttente) == 1) {
			isActionEnd = true;
		}
		return isActionEnd;
	}

}
