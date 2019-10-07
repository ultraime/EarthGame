package com.ultraime.game.metier;

import java.util.Collections;

import com.ultraime.game.entite.EntiteVivante;
import com.ultraime.game.metier.travail.Metier;

public class MetierService {

	private MetierService() {
	}

	private static MetierService INSTANCE = null;

	public static MetierService getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new MetierService();
		}
		return INSTANCE;
	}

	public static void ajouterMetier(final Metier metier, final EntiteVivante entiteVivante) {
		entiteVivante.ajouterMetier(metier);
		trierMetier(entiteVivante);
	}

	public static void trierMetier(final EntiteVivante entiteVivante) {
		Collections.sort(entiteVivante.metiers);
	}

	/**
	 * @param metier
	 * @param entiteVivante
	 * @param value         (augmenter ou diminuer la priorité)
	 */
	public static void modifierPriorite(final Metier metier, final EntiteVivante entiteVivante, int value) {
		int prioriteActuel = metier.priorite;
		for (int i = 0; i < entiteVivante.metiers.size(); i++) {
			Metier nextMetier = entiteVivante.metiers.get(i);

			if (value > 0 && nextMetier.priorite > prioriteActuel && nextMetier.priorite <= prioriteActuel + value) {
				nextMetier.priorite = nextMetier.priorite - 1;
			} else if (value < 0 && nextMetier.priorite < prioriteActuel
					&& nextMetier.priorite >= prioriteActuel + value) {
				nextMetier.priorite = nextMetier.priorite + 1;
			} else if (metier.priorite == prioriteActuel) {
				metier.priorite = metier.priorite + value;
			}
		}
		trierMetier(entiteVivante);
	}

}
