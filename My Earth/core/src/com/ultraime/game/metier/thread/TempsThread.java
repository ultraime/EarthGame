package com.ultraime.game.metier.thread;

import com.ultraime.database.ElementEarth;
import com.ultraime.database.base.Base;
import com.ultraime.game.metier.Lumiere;
import com.ultraime.game.metier.Temps;
import com.ultraime.game.metier.TileMapService;
import com.ultraime.game.utile.Parametre;

public class TempsThread implements Runnable {
	public static final int tempsAppel = 250;// 250

	public boolean running = true;
	private Thread thread;

	@Override
	public void run() {
		try {
			while (true) {
				Thread.sleep(tempsAppel / Parametre.VITESSE_DE_JEU);
				if (!Parametre.PAUSE) {
					updateElement();
					Temps tps = Base.getInstance().getTemps();
					tps.addMinute(1);
					if (Parametre.ACTIVER_LUMIERE) {
						if (tps.heure >= 17 && tps.heure < 25) {
							Lumiere lumiere = Lumiere.getInstance();
							lumiere.diminuerLumiere();
						} else if (tps.heure > 3 && tps.heure < 12) {
							Lumiere lumiere = Lumiere.getInstance();
							lumiere.augmenterLumiere();
						}
					}
				}
			}
		} catch (InterruptedException e) {
			// sleep failed
			e.printStackTrace();
		}
	}

	private void updateElement() {
		for (int i = 0; i < Base.getInstance().baseCulture.getElementEarthPlantes().size(); i++) {
			final ElementEarth elementEarth = Base.getInstance().baseCulture.getElementEarthPlantes().get(i);
			final Boolean doitEvoluer = elementEarth.gererEvolution();
			if (doitEvoluer) {
				TileMapService.getInstance().evoluerElement(elementEarth);
			}
		}

	}

	public void start() {
		if (thread == null) {
			thread = new Thread(this);
		}
		thread.start();
	}

	public void stop() {
		if (thread != null) {
			thread.interrupt();
		}
		thread = null;
	}

	public Thread getThread() {
		return thread;
	}

	public void setThread(Thread thread) {
		this.thread = thread;
	}

	@SuppressWarnings("deprecation")
	public void suspend() {
		this.thread.suspend();
	}

	@SuppressWarnings("deprecation")
	public void resume() {
		this.thread.resume();
	}

}
