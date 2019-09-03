package com.ultraime.database;

import com.ultraime.database.base.Base;

/**
 * @author Ultraime Méthode qui gére tout les contrôles des
 *         actions/construction.
 */
public class ControleurActions {

	/**
	 * @param elementEarth
	 * @param posX
	 * @param posY
	 */
	public static void controlerIdTuileNone(final ElementEarth elementEarth, final int posX, final int posY) {

		if (elementEarth.type.equals(ElementEarth.action)) {
			controlerAction(elementEarth, posX, posY);
		} else {
			// ElementEarth elementEarthFind =
			// Base.getInstance().recupererElementEarth(posX, posY);
		}
	}

	/**
	 * @param elementEarth
	 * @param posX
	 * @param posY
	 */
	public static void controlerAction(final ElementEarth elementEarth, final int posX, final int posY) {
		elementEarth.showIdTuileNone = true;
		ElementEarth elementEarthFind = Base.getInstance().recupererElementEarth(posX, posY);
		if (elementEarthFind != null) {
			if (ElementCible.containsType(elementEarth.elementCibles, elementEarthFind.type)) {
				elementEarth.showIdTuileNone = false;
			}
		}
	}

	/**
	 * @param elementEarth
	 * @return
	 */
	public static boolean isConstructible(ElementEarth elementEarth) {
		boolean isConstructible = true;
		if (elementEarth.showIdTuileNone) {
			isConstructible = false;
		}
		return isConstructible;
	}

}
