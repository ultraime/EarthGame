package com.ultraime.game.metier.thread;

import com.ultraime.database.ElementEarth;
import com.ultraime.database.base.Base;
import com.ultraime.game.entite.EntiteJoueur;
import com.ultraime.game.metier.Lumiere;
import com.ultraime.game.metier.Temps;
import com.ultraime.game.metier.TileMapService;
import com.ultraime.game.utile.Parametre;

public class TempsThread extends SuperThread {
	@Override
	public void doActionThread() {
		updateElement();
		updateEntiteJoueur();
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

	private void updateEntiteJoueur() {
		for (int i = 0; i < Base.getInstance().basePersonnage.getEntiteJoueurs().size(); i++) {
			EntiteJoueur entiteJoueur = Base.getInstance().basePersonnage.getEntiteJoueurs().get(i);
			entiteJoueur.habiliter.gererStats();
			if (entiteJoueur.lit == null) {
				entiteJoueur.lit = Base.getInstance().baseMeuble.rechercheLitLibre();
				if (entiteJoueur.lit != null) {
					entiteJoueur.lit.proprietaire = entiteJoueur;
				}
			}else{
				entiteJoueur.lit = Base.getInstance().baseMeuble.updateLit(entiteJoueur.lit);
			}
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

}
