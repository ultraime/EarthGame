package com.ultraime.database.entite;

import java.io.Serializable;
import java.util.List;

/**
 * @author Ultraime
 * ElementCible de l'action
 */
public class ElementCible implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Type de la cible.
	 */
	public String type;

	/**
	 * nom de la cible.
	 */
	public String nom;

	/**
	 * @param elementCibles
	 * @param typeCible
	 * @return true si la liste contient le type cible.
	 */
	public static boolean containsType(final List<ElementCible> elementCibles, final String typeCible) {
		boolean isContain = false;
		for (int i = 0; i < elementCibles.size(); i++) {
			final ElementCible elementCible = elementCibles.get(i);
			if (elementCible.type.equals(typeCible)) {
				isContain = true;
				break;
			}
		}
		return isContain;
	}

	/**
	 * @param elementCibles
	 * @param nomCible
	 * @return true si la liste contient le type cible.
	 */
	public static boolean containsNom(final List<ElementCible> elementCibles, final String nomCible) {
		boolean isContain = false;
		for (int i = 0; i < elementCibles.size(); i++) {
			final ElementCible elementCible = elementCibles.get(i);
			if (elementCible.nom.equals(nomCible)) {
				isContain = true;
				break;
			}
		}
		return isContain;
	}

}
