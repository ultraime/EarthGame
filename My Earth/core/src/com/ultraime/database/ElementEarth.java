package com.ultraime.database;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ultraime.animation.AnimationManager;
import com.ultraime.game.entite.Inventaire;

public class ElementEarth implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	transient public final static String structure_constructible = "structure_constructible";
	transient public final static String culture_sol_constructible = "culture_sol_constructible";
	transient public final static String meuble_constructible = "meuble_constructible";
	transient public final static String sol_constructible = "sol_constructible";
	transient public final static String culture_sol = "culture_sol";
	transient public final static String culture = "culture";
	transient public final static String culture_final = "culture_final";
	transient public final static String meuble = "meuble";
	transient public final static String structure = "structure";
	transient public final static String sol = "sol";
	// variable de placement
	transient public final static String unique = "unique";
	transient public final static String ligne = "ligne";
	transient public final static String rectangle = "rectangle";

	// variable de rotation
	transient public final static String rot_haut = "haut";
	transient public final static String rot_bas = "bas";
	transient public final static String rot_gauche = "gauche";
	transient public final static String rot_droite = "droite";

	transient public static List<String> types = new ArrayList<String>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		{
			add(culture);
			add(meuble);
			add(structure);
		}
	};

	/**
	 * structure,plante,securité,fourniture,energie…
	 */
	public String type;
	/**
	 * pour la méthode de placement de l'objet, unitaire, rectangle, ligne.
	 */
	public String placementType;
	/**
	 * c'est l'id de l'objet
	 */
	public String nom;

	/**
	 * lié a la donnée "nom"
	 */
	public String elementEarthEvolution;

	/**
	 * Donnée non obligatoire. Concerne les plantes. Temps pour évoluer En
	 * minute.
	 */
	public int tempEvolution = -1;

	/**
	 * le layers ou doit être affiché l'objet !
	 */
	public String layerCible;

	/**
	 * position de l'objet
	 */
	public int x, y;

	/**
	 * Peut contenir une à X image
	 */
	public List<ElementEarthImage> elementEarthImages = new ArrayList<>();

	/**
	 * Pour l'animation. Element non obligatoire.
	 */
	public AnimationManager animationManager;

	/**
	 * Image pour les Boutons. Element non obligatoire.
	 */
	public String logoImage;

	/**
	 * Inventaire de l'objet.Element non obligatoire.
	 */
	public Inventaire inventaire;

	/**
	 * en KG. Element non obligatoire.
	 */
	public float poids;

	/**
	 * Nombre d'élément produit quand recolté. Element non obligatoire.
	 */
	public float nombreRecolte;

	/**
	 * rotation de l'image.(haut,droite,bas,gauche)
	 */
	public String rotation = rot_haut;

	/**
	 * @param elementEarth
	 */
	public ElementEarth(final ElementEarth elementEarth) {
		super();
		this.type = elementEarth.type;
		this.placementType = elementEarth.placementType;
		this.nom = elementEarth.nom;
		this.elementEarthEvolution = elementEarth.elementEarthEvolution;
		this.layerCible = elementEarth.layerCible;
		this.x = new Integer(elementEarth.x);
		this.y = new Integer(elementEarth.y);
		this.elementEarthImages = elementEarth.elementEarthImages;
		this.tempEvolution = new Integer(elementEarth.tempEvolution);
		if (elementEarth.animationManager != null) {
			this.animationManager = new AnimationManager(elementEarth.animationManager);
		}
		this.logoImage = elementEarth.logoImage;
		this.poids = elementEarth.poids;
		if (elementEarth.inventaire != null) {
			this.inventaire = new Inventaire(elementEarth.inventaire.capaciteMax);
		}
		this.nombreRecolte = elementEarth.nombreRecolte;
		this.rotation = elementEarth.rotation;
	}

	public ElementEarth() {

	}

	public ElementEarth(ElementEarth recupererElementEarth, final int x, final int y) {
		this(recupererElementEarth);
		this.x = x;
		this.y = y;
	}

	public void render(final SpriteBatch batch, int tempsAnimation) {
		if (this.animationManager != null) {
			animationManager.render(batch, x * 64, y * 64, 0);
		}
	}

	/**
	 * @return true = doit évoluer, false = ne dois pas évoluer
	 */
	public boolean gererEvolution() {
		boolean doitEvoluer = false;
		if (tempEvolution != -1) {
			tempEvolution--;
			if (tempEvolution == 0) {
				doitEvoluer = true;
			}
		}

		return doitEvoluer;

	}

	public void addElementEarthImage(final ElementEarthImage elementEarthImage) {
		elementEarthImages.add(elementEarthImage);
	}

}
