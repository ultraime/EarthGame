package com.ultraime.database.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ultraime.database.ElementEarth;

public class BaseMeuble implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<ElementEarth> elementEarthMeubles;

	/**
	 * Recherche un lit sans propriétaire
	 * 
	 * @return ElementEarth lit
	 */
	public ElementEarth rechercheLitLibre() {
		ElementEarth lit = null;
		for (int i = 0; i < elementEarthMeubles.size(); i++) {
			final ElementEarth elementEarth = elementEarthMeubles.get(i);
			if (elementEarth.sousType != null && elementEarth.sousType.equals(ElementEarth.literie)) {
				if (elementEarth.proprietaire == null) {
					lit = elementEarth;
					break;
				}
			}
		}
		return lit;
	}

	public ElementEarth updateLit(ElementEarth lit) {
		ElementEarth litRetour = null;
		for (int i = 0; i < elementEarthMeubles.size(); i++) {
			final ElementEarth elementEarth = elementEarthMeubles.get(i);
			if(elementEarth == lit){
				litRetour = lit;
				break;
			}
		}
		return litRetour;
	}

	public BaseMeuble() {
		this.elementEarthMeubles = new ArrayList<>();
	}

	public List<ElementEarth> getElementEarthMeubles() {
		return elementEarthMeubles;
	}

	public void setElementEarthMeubles(List<ElementEarth> elementEarthMeubles) {
		this.elementEarthMeubles = elementEarthMeubles;
	}

}
