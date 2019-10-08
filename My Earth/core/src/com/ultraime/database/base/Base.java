package com.ultraime.database.base;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.ultraime.database.ElementEarth;
import com.ultraime.database.ElementEarthImage;
import com.ultraime.game.entite.EntiteStatic;
import com.ultraime.game.metier.Temps;
import com.ultraime.game.metier.TileMapService;
import com.ultraime.game.metier.WorldBodyService;
import com.ultraime.game.metier.WorldService;
import com.ultraime.game.metier.thread.TempsThread;
import com.ultraime.game.utile.Parametre;

public class Base implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Base instance;

	public BaseCulture baseCulture;
	public BaseMeuble baseMeuble;
	public BaseStructure baseStructure;
	public BaseSol baseSol;
	public BasePersonnage basePersonnage;
	public BaseAction baseAction;
	public BaseNature baseNature;
	public BaseObjetSol baseObjetSol;
	public BaseObjetAConstruire baseObjetAConstruire;
	/**
	 * pour les collisions
	 */
	private List<Rectangle> rectangleBodies;

	private List<Rectangle> rectangleBodiesAConstruire;

	private Temps temps;
	transient private TempsThread tempsThread;
	transient private Boolean isStartTempsThread = false;

	transient private List<ElementEarth> referenceElementEarth;

	private Base() {
		this.rectangleBodies = new ArrayList<>();
		this.rectangleBodiesAConstruire = new ArrayList<>();
		this.tempsThread = new TempsThread();
		this.referenceElementEarth = new ArrayList<>();

		baseCulture = new BaseCulture();
		baseMeuble = new BaseMeuble();
		baseStructure = new BaseStructure();
		baseSol = new BaseSol();
		basePersonnage = new BasePersonnage();
		baseAction = new BaseAction();
		baseNature = new BaseNature();
		baseObjetSol = new BaseObjetSol();
		baseObjetAConstruire = new BaseObjetAConstruire();
	}

	/**
	 * permet d'ajouter les objets dans la liste de référence.
	 * 
	 * @param elementEarth
	 */
	public void addReferenceElementEarth(ElementEarth elementEarth) {
		if (referenceElementEarth == null) {
			this.referenceElementEarth = new ArrayList<>();
		}
		referenceElementEarth.add(elementEarth);
	}

	/**
	 * @param typeElement
	 * @return elementEarthsRetour
	 */
	public List<ElementEarth> recupererElementEarth(final String typeElement) {
		List<ElementEarth> elementEarthsRetour = new ArrayList<>();
		for (ElementEarth elementEarth : referenceElementEarth) {
			if (elementEarth.type.equals(typeElement)) {
				elementEarthsRetour.add(elementEarth);
			}
		}
		return elementEarthsRetour;
	}

	public static void chargeData(final Base baseData) {
		instance = baseData;
	}

	/**
	 * @param elementEarthEvolution elementEarthEvolution
	 * @return
	 */
	public ElementEarth recupererElementEarthByNom(String elementEarthEvolution) {
		ElementEarth elemRetour = null;
		for (ElementEarth elementEarth : referenceElementEarth) {
			if (elementEarth.nom.equals(elementEarthEvolution)) {
				elemRetour = elementEarth;
				break;
			}
		}
		return elemRetour;
	}

	/**
	 * débute le thread du temps
	 */
	public void startTempsThread() {
		if (!isStartTempsThread) {
			tempsThread.start();
		}
	}

	public static Base getInstance() {
		if (instance == null) {
			instance = new Base();
		}
		return instance;
	}

	/**
	 * @param world
	 * @param x
	 * @param y
	 * @param largeur
	 * @param hauteur
	 */
	public void creerRectangleStatic(final World world, final World worldAffichable, final int x, final int y,
			final int largeur, final int hauteur, final EntiteStatic entiteStatic) {

		WorldBodyService.creerRectangleStatic(world, x, y, largeur, hauteur, entiteStatic);
		WorldBodyService.creerRectangleStatic(worldAffichable, x * WorldService.MULTIPLICATEUR + 32,
				y * WorldService.MULTIPLICATEUR + 32, largeur * WorldService.MULTIPLICATEUR,
				hauteur * WorldService.MULTIPLICATEUR, entiteStatic);

		Rectangle rectangle = new Rectangle(x, y, entiteStatic.getLargeur() / 2f, entiteStatic.getHauteur() / 2f);
		rectangleBodies.add(rectangle);
	}

	public void retirerRectangleStatic(World world, World worldAffichage, int posX, int posY) {
		try {
			Array<Body> bodies = new Array<Body>();
			world.getBodies(bodies);
			for (Body body : bodies) {
				if (posX == body.getPosition().x && posY == body.getPosition().y) {
					world.destroyBody(body);
					WorldService.getInstance().bodiesEntiteVivant.remove(body);
					break;
				}
			}
			worldAffichage.getBodies(bodies);
			for (Body body : bodies) {
				if (posX * WorldService.MULTIPLICATEUR + 32 == body.getPosition().x
						&& posY * WorldService.MULTIPLICATEUR + 32 == body.getPosition().y) {
					worldAffichage.destroyBody(body);
					WorldService.getInstance().bodiesAffichageEntiteVivant.remove(body);
					break;
				}
			}
			for (Rectangle rect : rectangleBodies) {
				if (rect.x == posX && rect.y == posY) {
					rectangleBodies.remove(rect);
					break;
				}
			}
		} catch (GdxRuntimeException e) {
			if (Parametre.MODE_DEBUG) {
				e.printStackTrace();
			}
			retirerRectangleStatic(world, worldAffichage, posX, posY);
		}
	}

	public List<Rectangle> getRectangleBodies() {
		return rectangleBodies;
	}

	public List<Rectangle> getRectangleBodiesAConstruire() {
		return rectangleBodiesAConstruire;
	}

	public void retirerRectangleConstructible(final int posX, final int posY) {
		for (Rectangle rect : rectangleBodies) {
			if (rect.x == posX && rect.y == posY) {
				rectangleBodiesAConstruire.remove(rect);
				break;
			}
		}

	}

	public void ajouterRectangleConstructible(Rectangle rectangle) {
		rectangleBodiesAConstruire.add(rectangle);

	}

	/**
	 * Ajoute un élément dans l'un des liste d'objet.
	 * 
	 * @param element
	 */
	public void ajouterElementEarth(final ElementEarth element) {
		getListEarth(element.type).add(element);
	}

	public boolean isObjetPresentSaufPlante(int x, int y) {
		boolean isObjetPresent = false;
		for (int i = 0; i < ElementEarth.types.size(); i++) {
			String type = ElementEarth.types.get(i);
			if (!type.equals(ElementEarth.culture)) {
				ElementEarth elementEarth = recupererElementEarth(x, y, type);
				if (elementEarth != null) {
					isObjetPresent = true;
				}
			}
		}
		return isObjetPresent;
	}

	public boolean isObjetPresent(int x, int y) {
		boolean isObjetPresent = false;
		for (int i = 0; i < ElementEarth.types.size(); i++) {
			String type = ElementEarth.types.get(i);
			ElementEarth elementEarth = recupererElementEarth(x, y, type);
			if (elementEarth != null) {
				isObjetPresent = true;
			}

		}
		return isObjetPresent;
	}

	public boolean retirerElementEarthAllObjet(final int x, final int y) {
		boolean elementRetire = false;
		for (int i = 0; i < ElementEarth.types.size(); i++) {
			String type = ElementEarth.types.get(i);
			boolean isOk = retirerElementEarth(x, y, type);
			if (isOk) {
				elementRetire = isOk;
				break;
			}
		}
		return elementRetire;
	}

	/**
	 * retore l'élement qui se trouve à la position x et y
	 * 
	 * @param x
	 * @param y
	 * @param type
	 * @return elementRetire
	 */
	public boolean retirerElementEarth(final int x, final int y, final String type) {
		boolean elementRetire = false;
		ElementEarth element = recupererElementEarth(x, y, type);
		if (element != null) {
			for (int j = 0; j < element.elementEarthImages.size(); j++) {
				final ElementEarthImage earthImage = element.elementEarthImages.get(j);
				final int posX = earthImage.x + element.x;
				final int posY = earthImage.y + element.y;
				if (!type.equals(ElementEarth.culture)) {
					String layer = null;
					if (element.layerCible != null) {
						layer = element.layerCible;
					} else {
						layer = earthImage.layerCible;
					}
					TileMapService.getInstance().viderCellMap(posX, posY,
							TileMapService.getInstance().getLayers(layer));
					WorldService.getInstance().retirerCollision(posX, posY);

				}
			}
			getListEarth(type).remove(element);
			elementRetire = true;
			// break;
		}

		return elementRetire;
	}

	/**
	 * Retire un élément de la carte
	 * 
	 * @param elementAretirer
	 */
	public void retirerElementEarth(ElementEarth elementAretirer) {

		for (int j = 0; j < elementAretirer.elementEarthImages.size(); j++) {
			final ElementEarthImage earthImage = elementAretirer.elementEarthImages.get(j);
			final int posX = earthImage.x + elementAretirer.x;
			final int posY = earthImage.y + elementAretirer.y;
			TileMapService.getInstance().viderCellMap(posX, posY,
					TileMapService.getInstance().getLayers(elementAretirer.layerCible));

		}
		getListEarth(elementAretirer.type).remove(elementAretirer);

	}

	public List<ElementEarth> getListEarth(final String type) {
		List<ElementEarth> earths = null;
		switch (type) {
		case ElementEarth.culture:
			earths = baseCulture.getElementEarthPlantes();
			break;
		case ElementEarth.culture_sol:
			earths = baseCulture.getElementEarthPlantes();
			break;
		case ElementEarth.culture_final:
			earths = baseCulture.getElementEarthPlantes();
			break;
		case ElementEarth.meuble:
			earths = baseMeuble.getElementEarthMeubles();
			break;
		case ElementEarth.structure:
			earths = baseStructure.getElementEarthStructure();
			break;
		case ElementEarth.sol:
			earths = baseSol.getElementEarthSol();
			break;
		case ElementEarth.action:
			earths = baseAction.getElementEarthAction();
			break;
		case ElementEarth.nature:
			earths = baseNature.getElementEarthNature();
			break;
		case ElementEarth.eau:
			earths = baseNature.getElementEarthEau();
			break;
		case ElementEarth.objet_sol:
			earths = baseObjetSol.getElementEarthsObjetSol();
			break;
		default:
			break;
		}
		return earths;
	}

	/**
	 * @param x
	 * @param y
	 * @return element
	 */
	public ElementEarth recupererElementEarth(final int x, final int y, final String type) {
		ElementEarth element = null;
		List<ElementEarth> earths = getListEarth(type);
		for (int i = 0; i < earths.size(); i++) {
			ElementEarth earth = earths.get(i);
			for (int j = 0; j < earth.elementEarthImages.size(); j++) {
				final ElementEarthImage earthImage = earth.elementEarthImages.get(j);
				final int posX = earthImage.x + earth.x;
				final int posY = earthImage.y + earth.y;
				if (posX == x && posY == y) {
					element = earth;
					break;
				}
			}
		}
		return element;
	}

	/**
	 * @param x
	 * @param y
	 * @return element
	 */
	public ElementEarth recupererElementEarth(final int x, final int y) {
		ElementEarth element = null;
		for (int a = 0; a < ElementEarth.types.size(); a++) {
			final String type = ElementEarth.types.get(a);
			List<ElementEarth> earths = getListEarth(type);
			for (int i = 0; i < earths.size(); i++) {
				ElementEarth earth = earths.get(i);
				for (int j = 0; j < earth.elementEarthImages.size(); j++) {
					final ElementEarthImage earthImage = earth.elementEarthImages.get(j);
					final int posX = earthImage.x + earth.x;
					final int posY = earthImage.y + earth.y;
					if (posX == x && posY == y) {
						element = earth;
						break;
					}
				}
			}
		}
		return element;
	}

	/**
	 * @param x
	 * @param y
	 * @param layer
	 * @return isObjetPresent
	 */
	public boolean isObjetPresentLayer(final int x, final int y, final String layer) {
		boolean isObjetPresent = false;
		for (int i = 0; i < ElementEarth.types.size(); i++) {
			String type = ElementEarth.types.get(i);
			if (!type.equals(ElementEarth.culture)) {
				ElementEarth elementEarth = recupererElementEarth(x, y, type);
				if (elementEarth != null && elementEarth.layerCible.equals(layer)) {
					isObjetPresent = true;
				}
			}
		}
		return isObjetPresent;
	}

	/**
	 * Récupére le coffre aprés "elementBefore"
	 * 
	 * @return elementEarth
	 */
	public ElementEarth rechercherCoffreDisponible(ElementEarth elementBefore) {
		ElementEarth elementEarthCoffre = null;
		for (int i = 0; i < getListEarth(ElementEarth.meuble).size(); i++) {
			final ElementEarth elementEarth = getListEarth(ElementEarth.meuble).get(i);
			if (elementEarth.inventaire != null) {

				if (elementBefore == null) {
					if (elementEarth.inventaire.placeDisponible) {
						if (elementEarth.inventaire.espaceDisponnible() >= 1f) {
							elementEarthCoffre = elementEarth;
							break;
						}
					}
				} else {
					if (elementBefore == elementEarth) {
						elementBefore = null;
					}
				}
			}

		}
		return elementEarthCoffre;
	}

	public Temps getTemps() {
		return temps;
	}

	public void setTemps(Temps temps) {
		this.temps = temps;
	}

}
