package com.ultraime.database;

import com.sun.org.apache.xalan.internal.xsltc.compiler.sym;
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
	public static boolean controlerIdTuileNone(final ElementEarth elementEarth, int posX, int posY) {

		if (!elementEarth.type.equals(ElementEarth.sol_constructible)) {
			if (elementEarth.type.equals(ElementEarth.action)) {
				elementEarth.showIdTuileNone = controlerAction(elementEarth, posX, posY);
			} else if (elementEarth.elementCibles != null && elementEarth.elementCibles.size() > 0) {
				// TODO faire le controle sur le puits.
				elementEarth.showIdTuileNone = true;
				ElementEarth elementEarthFind = Base.getInstance().recupererElementEarth(posX, posY);
				if (elementEarthFind != null) {
					if (ElementCible.containsNom(elementEarth.elementCibles, elementEarthFind.nom)) {
						elementEarth.showIdTuileNone = false;
					}
				}
			} else {
				elementEarth.showIdTuileNone = false;
				for (int i = 0; i < elementEarth.elementEarthImages.size(); i++) {
					final ElementEarthImage earthImage = elementEarth.elementEarthImages.get(i);
					final int posXImage = earthImage.x + posX;
					final int posYImage = earthImage.y + posY;
					final ElementEarth elementEarthFind = Base.getInstance().recupererElementEarth(posXImage,
							posYImage);
					if (elementEarthFind != null) {
						elementEarth.showIdTuileNone = true;
						break;
					}
				}

			}
		}
		return elementEarth.showIdTuileNone;
	}

	/**
	 * @param elementEarth
	 * @param posX
	 * @param posY
	 * @return
	 */
	private static boolean controlerAction(final ElementEarth elementEarth, final int posX, final int posY) {
		elementEarth.showIdTuileNone = true;
		ElementEarth elementEarthFind = Base.getInstance().recupererElementEarth(posX, posY);
		if (elementEarthFind != null) {
			if (ElementCible.containsType(elementEarth.elementCibles, elementEarthFind.type)) {
				elementEarth.showIdTuileNone = false;
			}
		}
		return elementEarth.showIdTuileNone;
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
