package com.ultraime.database;

import java.util.List;

/**
 * @author Ultraime
 *
 */
public class ElementCible {

	/**
	 * Type de la cible.
	 */
	public String type;

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


}
