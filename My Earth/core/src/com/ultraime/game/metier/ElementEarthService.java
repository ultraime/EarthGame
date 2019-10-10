package com.ultraime.game.metier;

import com.ultraime.database.base.Base;
import com.ultraime.database.entite.ElementEarth;

public class ElementEarthService {

	/**
	 * Permet de créer les objets généré par @param ElementEarth elem
	 * 
	 * @param elem
	 */
	public static void genererElement(final ElementEarth elem) {
		if (elem != null && elem.elementGenere != null) {
			final ElementEarth elementEarth = Base.getInstance().recupererElementEarthByNom(elem.elementGenere.nom);
			for (int i = 0; i < elem.elementGenere.nbGenere; i++) {
				elementEarth.x = elem.x;
				elementEarth.y = elem.y;
				TileMapService.getInstance().construireItem(elementEarth);
			}
		}
	}

}
