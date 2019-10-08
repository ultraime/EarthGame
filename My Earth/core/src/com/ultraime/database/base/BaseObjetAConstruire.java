package com.ultraime.database.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ultraime.database.ElementEarth;

public class BaseObjetAConstruire implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<ElementEarth> elementEarthsAConstruire;

	public BaseObjetAConstruire() {
		this.elementEarthsAConstruire = new ArrayList<>();
	}

	/**
	 * @return le premier element de la liste elementAconstruire et le suprimme de
	 *         la liste. Attention, peut return null
	 */
	public ElementEarth getElementAConstruire() {
		ElementEarth elementAconstruire = null;
		if (elementEarthsAConstruire.size() > 0) {
			elementAconstruire = elementEarthsAConstruire.get(0);
			retirerElementAconstruire(elementEarthsAConstruire.get(0));
		}
		return elementAconstruire;
	}

	private void retirerElementAconstruire(ElementEarth elementEarth) {
		elementEarthsAConstruire.remove(elementEarth);

	}

	/**
	 * @param ElementEarth AJoute un élément à construire. Attention si on place un
	 *                     élément à l'endroit d'un objet en construction présent on
	 *                     le suprimme.
	 */
	public void ajouterElementAconstruire(ElementEarth elementAconstruire) {
		retirerElementAconstruire(elementAconstruire.x, elementAconstruire.y);
		elementEarthsAConstruire.add(elementAconstruire);
	}

	/**
	 * @param x
	 * @param y
	 */
	public boolean retirerElementAconstruire(final int x, final int y) {
		boolean isRemove = false;
		for (int i = 0; i < elementEarthsAConstruire.size(); i++) {
			ElementEarth elementAconstruire = elementEarthsAConstruire.get(i);
			if (elementAconstruire.x == x && elementAconstruire.y == y) {
				elementEarthsAConstruire.remove(i);
				isRemove = true;
				break;
			}
		}
		return isRemove;
	}

	/**
	 * @param x
	 * @param y
	 */
	public boolean retirerElementAconstruireSaufAction(final int x, final int y) {
		boolean isRemove = false;
		for (int i = 0; i < elementEarthsAConstruire.size(); i++) {
			ElementEarth elementAconstruire = elementEarthsAConstruire.get(i);
			if (elementAconstruire.x == x && elementAconstruire.y == y) {
				if (!elementAconstruire.type.equals(ElementEarth.action)) {
					elementEarthsAConstruire.remove(i);
					isRemove = true;
				}
				break;
			}
		}
		return isRemove;
	}

	public List<ElementEarth> getElementEarthsAConstruire() {
		return elementEarthsAConstruire;
	}

	public void setElementEarthsAConstruire(List<ElementEarth> elementEarthsAConstruire) {
		this.elementEarthsAConstruire = elementEarthsAConstruire;
	}

}
