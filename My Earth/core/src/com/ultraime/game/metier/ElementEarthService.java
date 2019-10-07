package com.ultraime.game.metier;

import com.ultraime.database.ElementEarth;
import com.ultraime.database.base.Base;

public class ElementEarthService {

	/**
	 * Permet de créer les objets généré par @param ElementEarth elem
	 * 
	 * @param elem
	 */
	public static void genererElement(final ElementEarth elem) {
		final ElementEarth elementEarth = Base.getInstance().recupererElementEarthByNom(elem.elementGenere.nom);
		for (int i = 0; i < elem.elementGenere.nbGenere; i++) {
			elementEarth.x = elem.x;
			elementEarth.y = elem.y;
			TileMapService.getInstance().construireItem(elementEarth);
		}
	}

}
