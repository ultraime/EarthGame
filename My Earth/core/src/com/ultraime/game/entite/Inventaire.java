package com.ultraime.game.entite;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ultraime.database.ElementEarth;

/**
 * @author Ultraime Inventaire composé d'une liste de ElementEarth. Le poids est
 *         exprimé en KG
 */
public class Inventaire implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public float capaciteMax = 0;
	public float capaciteActuel = 0;
	public boolean placeDisponible = true;

	private List<ElementEarth> elementEarths;

	public Inventaire(final float capaciteMax) {
		this.elementEarths = new ArrayList<>();
		this.capaciteMax = capaciteMax;
	}

	/**
	 * @param elementAajouter
	 * @return elementNonAjouter car inventaire plein.
	 */
	public List<ElementEarth> ajouterElement(List<ElementEarth> elementAajouter) {
		List<ElementEarth> elementNonAjouter = new ArrayList<>();
		for (ElementEarth earth : elementAajouter) {
			if (capaciteActuel + earth.poids <= capaciteMax) {
				capaciteActuel += earth.poids;
				elementEarths.add(earth);
				placeDisponible = true;
			} else {
				elementNonAjouter.add(earth);
				placeDisponible = false;
			}
		}
		System.out.println("ABO -> " + elementNonAjouter.size());
		return elementNonAjouter;
	}

	/**
	 * @param elementAajouter
	 * @return elementNonAjouter car inventaire plein.
	 */
	public ElementEarth ajouterElement(ElementEarth elementAajouter) {
		ElementEarth elementNonAjouter = null;
		if (capaciteActuel + elementAajouter.poids <= capaciteMax) {
			//TODO bug a corriger.
			capaciteActuel += elementAajouter.poids;

			System.out.println("ABO -> " + capaciteActuel);
			elementEarths.add(elementAajouter);
			placeDisponible = true;
		} else {
			elementNonAjouter = elementAajouter;
			placeDisponible = false;
		}

		return elementNonAjouter;
	}

	/**
	 * @param poidsTotal
	 * @return placeSuffisante
	 */
	public boolean placeSuffisante(final float poidsTotal) {
		boolean placeSuffisante = false;
		if (capaciteActuel + poidsTotal <= capaciteMax) {
			placeSuffisante = true;
		}
		return placeSuffisante;
	}

	public float espaceDisponnible() {
		return capaciteMax - capaciteActuel;
	}

	public List<ElementEarth> getElementEarths() {
		return elementEarths;
	}

	public void setElementEarths(List<ElementEarth> elementEarths) {
		this.elementEarths = elementEarths;
		calculerCapaciter();
	}

	private void calculerCapaciter() {
		capaciteActuel = 0;
		for (ElementEarth earth : elementEarths) {
			capaciteActuel += earth.poids;
		}

	}

}
