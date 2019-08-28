package com.ultraime.game.metier.thread;

import java.util.List;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.ultraime.game.entite.EntiteVivante;
import com.ultraime.game.metier.WorldService;
import com.ultraime.game.utile.Parametre;

public class EntiteManagerThread<E> extends SuperThread {
	public static final int tempsAppel = 0;
	List<EntiteVivante> listEntite;

	@SuppressWarnings("unchecked")
	public EntiteManagerThread(final List<E> listEntite) {
		this.listEntite = (List<EntiteVivante>) listEntite;
	}

	@Override
	public void doActionThread() {
		final World world = WorldService.getInstance().world;
		final List<Body> bodies = WorldService.getInstance().bodiesEntiteVivant;

		for (int i = 0; i < bodies.size(); i++) {
			final Body body = bodies.get(i);
			if (!Parametre.PAUSE) {
				final EntiteVivante ev = (EntiteVivante) body.getUserData();
				if (listEntite.contains(ev)) {
					final World worldAffichage = WorldService.getInstance().worldAffichage;
					ev.rechercheAction(body, world, worldAffichage);
				}

			}

		}

	}

}
