package com.ultraime.database.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ultraime.database.ElementEarth;
import com.ultraime.game.metier.TileMapService;

public class BaseCulture implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<ElementEarth> elementEarthplantes;

	public BaseCulture() {
		this.elementEarthplantes = new ArrayList<>();
	}

	/**
	 * recherche un élément disponible pour être cultivé.
	 * 
	 * @return ElementEarth à cultiver
	 */
	public ElementEarth rechercherElementAcultiver() {
		ElementEarth elementEarthCulture = null;
		for (int i = 0; i < getElementEarthPlantes().size(); i++) {
			final ElementEarth elementEarth = getElementEarthPlantes().get(i);
			if (elementEarth.type.equals(ElementEarth.culture_sol)) {
				if (!Base.getInstance().isObjetPresentLayer(elementEarth.x, elementEarth.y, TileMapService.OBJET_0)) {
					elementEarthCulture = elementEarth;
					break;
				}
			}
		}
		return elementEarthCulture;
	}

	/**
	 * @return elementEarthCulture
	 */
	public ElementEarth rechercherElementARecolter() {
		ElementEarth elementEarthCulture = null;
		for (int i = 0; i < Base.getInstance().getListEarth(ElementEarth.culture).size(); i++) {
			final ElementEarth elementEarth = Base.getInstance().getListEarth(ElementEarth.culture).get(i);
			if (elementEarth.type.equals(ElementEarth.culture_final)) {
				elementEarthCulture = elementEarth;
			}
		}
		return elementEarthCulture;
	}

	/**
	 * @return elementEarthCulture
	 */
	public ElementEarth rechercherElementARecolterNext(final ElementEarth elementParam) {
		ElementEarth elementEarthCulture = null;
		ElementEarth elementEarthCultureNext = null;
		for (int i = 0; i < Base.getInstance().getListEarth(ElementEarth.culture).size(); i++) {
			final ElementEarth elementEarth = Base.getInstance().getListEarth(ElementEarth.culture).get(i);
			if (elementParam == elementEarth) {
				elementEarthCulture = elementEarth;
			} else if (elementEarthCulture != null) {
				if (elementEarth.type.equals(ElementEarth.culture_final)) {
					elementEarthCultureNext = elementEarth;
				}
			}

		}
		return elementEarthCultureNext;
	}

	public List<ElementEarth> getElementEarthPlantes() {
		return elementEarthplantes;
	}

	public void setElementEarthPlantes(List<ElementEarth> elementEarthplantes) {
		this.elementEarthplantes = elementEarthplantes;
	}

}
