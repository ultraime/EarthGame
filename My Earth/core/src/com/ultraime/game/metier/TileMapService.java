package com.ultraime.game.metier;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.ultraime.database.ElementEarth;
import com.ultraime.database.ElementEarthImage;
import com.ultraime.database.RotationManager;
import com.ultraime.database.base.Base;
import com.ultraime.game.entite.ElementAconstruire;

public class TileMapService {

	public static final String OBJET_0 = "OBJET_0";
	public static final String SOL_0 = "SOL_0";
	public final static String CONSTRUCTION = "CONSTRUCTION";
	public final static String CONSTRUCTION_TEMP = "CONSTRUCTION_TEMP";

	public static final String TUILE = "tuile";

	// Pour la carte
	public TiledMap tiledMap;
	private TiledMapTileLayer tileSol_0;
	public OrthogonalTiledMapRenderer rendererMap;
	private static TileMapService instance;

	// Pour la construction
	// private static List<ElementAconstruire> elementAconstruires;
	// Pour la construction
	private static List<ElementEarth> elementAconstruiresNEW;

	private static List<ElementAconstruire> elementsAcultiver;

	public static TileMapService getInstance() {
		if (instance == null) {
			instance = new TileMapService();
		}
		return instance;
	}

	private TileMapService() {
		tiledMap = new TmxMapLoader().load("carte/carte.tmx");
		rendererMap = new OrthogonalTiledMapRenderer(tiledMap, 1);
		tileSol_0 = (TiledMapTileLayer) tiledMap.getLayers().get("SOL_0");
		elementsAcultiver = new ArrayList<>();
		elementAconstruiresNEW = new ArrayList<>();
	}

	public TiledMapTileSet getTileSet(final String tileName) {
		return tiledMap.getTileSets().getTileSet(tileName);
	}

	public int recupererIdTile(final float x, final float y) {
		tileSol_0.getCell((int) x / 64, (int) y / 64).setTile(null);
		return 0;
	}

	public Vector2 recupererPositionDepart() {
		final Vector2 posDepart = new Vector2();
		for (MapObject object : tiledMap.getLayers().get("position").getObjects()) {
			if (object.getName().equals("depart")) {
				posDepart.x = (Float) object.getProperties().get("x");
				posDepart.y = (Float) object.getProperties().get("y");
				break;
			}
		}
		return posDepart;
	}

	public static Boolean isMurEnBois(int id) {
		boolean isMurEnBois = false;
		if (id > 0 && id < 10) {
			isMurEnBois = true;
		}

		return isMurEnBois;
	}

	public void render() {
		this.rendererMap.render();

	}

	public void updateCamera(OrthographicCamera camera) {
		rendererMap.setView(camera);
	}

	public TiledMapTileLayer getLayers(String string) {
		return (TiledMapTileLayer) tiledMap.getLayers().get(string);
	}

	public void evoluerElement(ElementEarth elementEarth) {
		final int posX = elementEarth.x;
		final int posY = elementEarth.y;
		ElementEarth elementEvolue = Base.getInstance().recupererElementEarthByNom(elementEarth.elementEarthEvolution);
		TiledMapTileSet tileSet = tiledMap.getTileSets().getTileSet(TUILE);
		TiledMapTileLayer tiledLayer = (TiledMapTileLayer) tiledMap.getLayers().get(elementEvolue.layerCible);
		TiledMapTileLayer tiledLayerBase = (TiledMapTileLayer) tiledMap.getLayers().get(elementEarth.layerCible);
		Cell cell = new Cell();
		cell.setTile(tileSet.getTile(elementEvolue.elementEarthImages.get(0).idTuile));
		tiledLayerBase.setCell(posX, posY, null);
		tiledLayer.setCell(posX, posY, cell);
		if (!elementEvolue.layerCible.equals(SOL_0)) {
			if (elementEvolue.elementEarthImages.get(0).isCollision) {
				WorldService.getInstance().creerCollision(posX, posY);
			} else {
				WorldService.getInstance().retirerCollision(posX, posY);
			}
		}
		retirerElementAconstruire(posX, posY);
		// ajout dans la liste des éléments de culture.
		if (elementEvolue.type.equals(ElementEarth.culture_sol) || elementEvolue.type.equals(ElementEarth.culture)
				|| elementEvolue.type.equals(ElementEarth.culture_final)) {
			Base.getInstance().retirerElementEarthAllObjet(posX, posY);
			ElementEarth elementEarthNew = new ElementEarth(elementEvolue);
			elementEarthNew.x = posX;
			elementEarthNew.y = posY;
			Base.getInstance().ajouterElementEarth(elementEarthNew);

		} else {
			Base.getInstance().retirerElementEarthAllObjet(posX, posY);
		}

	}

	/**
	 * Ajout un item sur la carte (méthode appelée quand un personnage construit
	 * un élément)
	 * 
	 * @param elementAconstruire
	 */
	public void construireItem(final ElementEarth elementAconstruire) {
		final int posX = elementAconstruire.x;
		final int posY = elementAconstruire.y;
		ElementEarth elementEvolue = Base.getInstance()
				.recupererElementEarthByNom(elementAconstruire.elementEarthEvolution);
		if (elementEvolue == null) {
			elementEvolue = Base.getInstance().recupererElementEarthByNom(elementAconstruire.nom);
		}
		retirerElementAconstruire(posX, posY);

		String layerCible = elementEvolue.layerCible;

		for (int i = 0; i < elementAconstruire.elementEarthImages.size(); i++) {
			ElementEarthImage elementEarthImage = elementAconstruire.elementEarthImages.get(i);
			int posXImage = posX + elementEarthImage.x;
			int posYImage = posY + elementEarthImage.y;
			layerCible = elementEvolue.layerCible;
			if (layerCible == null) {
				layerCible = elementEarthImage.layerCible;
			}
			if (!layerCible.equals(SOL_0)) {
				Base.getInstance().retirerElementEarthAllObjet(posXImage, posYImage);
				if (elementEvolue.elementEarthImages.get(i).isCollision) {
					WorldService.getInstance().creerCollision(posXImage, posYImage);
				} else {
					WorldService.getInstance().retirerCollision(posXImage, posYImage);
				}
			} else {
				Base.getInstance().retirerElementEarth(posXImage, posYImage, ElementEarth.culture);
			}
		}
		TiledMapTileLayer tiledLayer = (TiledMapTileLayer) tiledMap.getLayers().get(layerCible);
		TiledMapTileLayer tiledLayerConstruction = (TiledMapTileLayer) tiledMap.getLayers().get(CONSTRUCTION);
		alimenterImageFromMultiTile(elementAconstruire, tiledLayerConstruction, posX, posY, true);
		// on passe la rotation à l'objet évolué
		elementEvolue = RotationManager.getElementRotate(elementEvolue, elementAconstruire.rotation);

		alimenterImageFromMultiTile(elementEvolue, tiledLayer, posX, posY, false);

		if (elementEvolue.nom.equals("mur_en_bois")) {
			creerMurEnBois();
		}

		ElementEarth elementEarth = new ElementEarth(elementEvolue);
		elementEarth.x = posX;
		elementEarth.y = posY;
		Base.getInstance().ajouterElementEarth(elementEarth);
	}

	/**
	 * Détruit un élément sur la carte à partir d'un ElementAction
	 * 
	 * @param elementAction
	 */
	public void detruireItem(final ElementEarth elementAction) {
		final int posX = elementAction.x;
		final int posY = elementAction.y;
		retirerElementAconstruire(posX, posY);
		for (int i = 0; i < elementAction.elementEarthImages.size(); i++) {
			int posXImage = posX + elementAction.elementEarthImages.get(i).x;
			int posYImage = posY + elementAction.elementEarthImages.get(i).y;
			Base.getInstance().retirerElementEarthAllObjet(posXImage, posYImage);

		}
		// supression de l'élément "constructible"
		final TiledMapTileLayer tiledLayerConstruction = (TiledMapTileLayer) tiledMap.getLayers().get(CONSTRUCTION);
		alimenterImageFromMultiTile(elementAction, tiledLayerConstruction, posX, posY, true);
		// juste pour l'affichage
		creerMurEnBois();

	}

	/**
	 * place l'élément sur la carte (méthode appelée au chargement.)
	 * 
	 * @param elementAconstruire
	 */
	public void placerElementEarth(final ElementEarth elementAconstruire) {
		final int posX = elementAconstruire.x;
		final int posY = elementAconstruire.y;

		for (int i = 0; i < elementAconstruire.elementEarthImages.size(); i++) {
			int posXImage = posX + elementAconstruire.elementEarthImages.get(i).x;
			int posYImage = posY + elementAconstruire.elementEarthImages.get(i).y;

			if (!elementAconstruire.layerCible.equals(SOL_0)) {
				if (elementAconstruire.elementEarthImages.get(i).isCollision) {
					WorldService.getInstance().creerCollision(posXImage, posYImage);
				} else {
					WorldService.getInstance().retirerCollision(posXImage, posYImage);
				}
			}
		}
		TiledMapTileLayer tiledLayer = (TiledMapTileLayer) tiledMap.getLayers().get(elementAconstruire.layerCible);
		TiledMapTileLayer tiledLayerConstruction = (TiledMapTileLayer) tiledMap.getLayers().get(CONSTRUCTION);
		alimenterImageFromMultiTile(elementAconstruire, tiledLayerConstruction, posX, posY, true);
		alimenterImageFromMultiTile(elementAconstruire, tiledLayer, posX, posY, false);

		if (elementAconstruire.nom.equals("mur_en_bois")) {
			creerMurEnBois();
		}
		// si c'est une culture, on place la terre en dessous d'elle
		if (elementAconstruire.type.equals(ElementEarth.culture)
				|| elementAconstruire.type.equals(ElementEarth.culture_final)) {
			ElementEarth elementEarth = Base.getInstance().recupererElementEarthByNom("sol_carotte");
			TiledMapTileLayer tiled = (TiledMapTileLayer) tiledMap.getLayers().get(elementEarth.layerCible);
			alimenterImageFromMultiTile(elementEarth, tiled, posX, posY, false);
		}
	}

	/**
	 * affiche l'objet en jeu. L'objet peut être composé de plusieurs tiles.
	 * 
	 * @param elementEarth
	 * @param tiledLayer
	 * @param posX
	 * @param posY
	 * @param aVider
	 */
	public void alimenterImageFromMultiTile(ElementEarth elementEarth, TiledMapTileLayer tiledLayer,
			final int posX, final int posY, final boolean aVider) {
		// TODO pb si plusieurs layer pour un obj
		TiledMapTileSet tileSet = TileMapService.getInstance().getTileSet(TileMapService.TUILE);
		for (int j = 0; j < elementEarth.elementEarthImages.size(); j++) {
			Cell cell = new Cell();
			ElementEarthImage earthImage = elementEarth.elementEarthImages.get(j);
			if (aVider) {
				cell.setTile(null);
			} else {
				int idTuile = earthImage.idTuile;
				if (elementEarth.showIdTuileNone) {
					idTuile = elementEarth.idTuileNone;
				}
				cell.setTile(tileSet.getTile(idTuile));
				RotationManager.rotateCell(cell, elementEarth.rotation);

			}
			if (elementEarth.layerCible == null) {
				tiledLayer = TileMapService.getInstance().getLayers(earthImage.layerCible);
			}
			tiledLayer.setCell(posX + earthImage.x, posY + earthImage.y, cell);
		}

	}

	/**
	 * @param posX
	 * @param posY
	 * @param tiledMapTileLayer
	 */
	public void viderCellMap(final int posX, final int posY, final TiledMapTileLayer tiledMapTileLayer) {
		if (tiledMapTileLayer.getCell(posX, posY) != null) {
			tiledMapTileLayer.setCell(posX, posY, null);
		}
	}

	public void creerMurEnBois() {
		final TiledMapTileLayer tilePourMur = (TiledMapTileLayer) tiledMap.getLayers().get(OBJET_0);
		final TiledMapTileSet tileSet = this.tiledMap.getTileSets().getTileSet(TUILE);
		final int nbTileY = tilePourMur.getHeight();
		final int nbTileX = tilePourMur.getWidth();
		for (int x = 0; x < nbTileX; x++) {
			for (int y = 0; y < nbTileY; y++) {
				Cell cell = tilePourMur.getCell(x, y);
				if (cell != null && cell.getTile() != null) {
					if (isMurEnBois(cell.getTile().getId())) {
						cell.setTile(tileSet.getTile(1));
						if (!isMurEnHaut(x, y) && !isMurADroite(x, y) && !isMurEnBas(x, y) && !isMurAgauche(x, y)) {
							// le mur est seul
							cell.setTile(tileSet.getTile(1));
						} else if (!isMurEnHaut(x, y) && !isMurEnBas(x, y)) {
							// pas de mur en haut et en bas
							if (isMurADroite(x, y) && isMurAgauche(x, y)) {
								cell.setTile(tileSet.getTile(2));
							} else if (isMurADroite(x, y)) {
								cell.setTile(tileSet.getTile(4));
							} else if (isMurAgauche(x, y)) {
								cell.setTile(tileSet.getTile(4));
								cell.setFlipHorizontally(true);
							}
						} else if (!isMurADroite(x, y) && !isMurAgauche(x, y)) {
							// pas de mur à gauche et à droite.
							if (isMurEnHaut(x, y) && isMurEnBas(x, y)) {
								cell.setTile(tileSet.getTile(7));
							} else if (isMurEnHaut(x, y)) {
								cell.setTile(tileSet.getTile(5));
							} else if (isMurEnBas(x, y)) {
								cell.setTile(tileSet.getTile(9));
							}
						} else {
							// il y a un mur en haut ou en bas et a gauche ou a
							// droite
							if (isMurADroite(x, y) && !isMurAgauche(x, y)) {
								if (isMurEnHaut(x, y) && !isMurEnBas(x, y)) {
									cell.setTile(tileSet.getTile(3));
								} else if (!isMurEnHaut(x, y) && isMurEnBas(x, y)) {
									cell.setTile(tileSet.getTile(8));
									cell.setFlipVertically(true);
								} else if (isMurEnHaut(x, y) && isMurEnBas(x, y)) {
									cell.setTile(tileSet.getTile(7));
								}
							} else if (!isMurADroite(x, y) && isMurAgauche(x, y)) {
								if (isMurEnHaut(x, y) && !isMurEnBas(x, y)) {
									cell.setTile(tileSet.getTile(3));
									cell.setFlipHorizontally(true);
								} else if (!isMurEnHaut(x, y) && isMurEnBas(x, y)) {
									cell.setTile(tileSet.getTile(8));
									cell.setFlipVertically(true);
									cell.setFlipHorizontally(true);
								} else if (isMurEnHaut(x, y) && isMurEnBas(x, y)) {
									cell.setTile(tileSet.getTile(7));
								}
							} else {
								if (isMurADroite(x, y) && isMurAgauche(x, y)) {
									if (!isMurEnHaut(x, y) && isMurEnBas(x, y)) {
										cell.setTile(tileSet.getTile(9));
									} else if (isMurEnHaut(x, y) && isMurEnBas(x, y)) {
										cell.setTile(tileSet.getTile(6));
									} else if (isMurEnHaut(x, y) && !isMurEnBas(x, y)) {
										cell.setTile(tileSet.getTile(5));
									}
								}
							}
						}

					}
				}
			}
		}

	}

	public boolean isObjetPresent(final int x, final int y) {
		boolean isObjetPresent = false;
		final TiledMapTileLayer tileObjt0 = (TiledMapTileLayer) tiledMap.getLayers().get(OBJET_0);
		if (tileObjt0.getCell(x, y) != null) {
			isObjetPresent = true;
		}
		if (!isObjetPresent) {
			List<ElementEarth> earths = Base.getInstance().baseCulture.getElementEarthPlantes();
			for (int i = 0; i < earths.size(); i++) {
				if (earths.get(i).x == x && earths.get(i).y == y) {
					if (earths.get(i).elementEarthImages != null
							&& earths.get(i).elementEarthImages.get(0).isCollision) {
						isObjetPresent = true;
						break;
					}
				}
			}
		}
		return isObjetPresent;
	}

	public boolean isMurAgauche(final int x, final int y) {
		boolean isMurAgauche = false;
		final TiledMapTileLayer tilePourMur = (TiledMapTileLayer) tiledMap.getLayers().get(OBJET_0);
		if (x > 1) {
			if (tilePourMur.getCell(x - 1, y) != null) {
				if (isMurEnBois(tilePourMur.getCell(x - 1, y).getTile().getId())) {
					isMurAgauche = true;
				}
			}
		}
		return isMurAgauche;
	}

	public boolean isMurADroite(final int x, int y) {
		boolean isMurADroite = false;
		final TiledMapTileLayer tilePourMur = (TiledMapTileLayer) tiledMap.getLayers().get(OBJET_0);
		final int nbTileX = tilePourMur.getWidth();
		if (x + 1 < nbTileX) {
			if (tilePourMur.getCell(x + 1, y) != null) {
				if (isMurEnBois(tilePourMur.getCell(x + 1, y).getTile().getId())) {
					isMurADroite = true;
				}
			}
		}
		return isMurADroite;
	}

	public boolean isMurEnHaut(final int x, int y) {
		boolean isMurEnHaut = false;
		final TiledMapTileLayer tilePourMur = (TiledMapTileLayer) tiledMap.getLayers().get(OBJET_0);
		final int nbTileY = tilePourMur.getHeight();
		if (y + 1 < nbTileY) {
			if (tilePourMur.getCell(x, y + 1) != null) {
				if (isMurEnBois(tilePourMur.getCell(x, y + 1).getTile().getId())) {
					isMurEnHaut = true;
				}
			}
		}
		return isMurEnHaut;
	}

	public boolean isMurEnBas(final int x, int y) {
		boolean isMurEnBas = false;
		final TiledMapTileLayer tilePourMur = (TiledMapTileLayer) tiledMap.getLayers().get(OBJET_0);
		if (y > 1) {
			if (tilePourMur.getCell(x, y - 1) != null) {
				if (isMurEnBois(tilePourMur.getCell(x, y - 1).getTile().getId())) {
					isMurEnBas = true;
				}
			}
		}
		return isMurEnBas;
	}

	/**
	 * @return le premier element de la liste elementAconstruire. Attention,
	 *         peut return null
	 */
	public ElementEarth getElementAConstruire() {
		ElementEarth elementAconstruire = null;
		if (elementAconstruiresNEW.size() > 0) {
			elementAconstruire = elementAconstruiresNEW.get(0);
			retirerElementAconstruireNEW(elementAconstruiresNEW.get(0));
		}
		return elementAconstruire;
	}

	private void retirerElementAconstruireNEW(ElementEarth elementEarth) {
		elementAconstruiresNEW.remove(elementEarth);

	}

	/**
	 * @return le premier element de la liste elementAconstruire. Attention,
	 *         peut return null
	 */
	public ElementAconstruire getElementACultiver() {
		ElementAconstruire elementACultiver = null;
		if (elementsAcultiver.size() > 0) {
			elementACultiver = elementsAcultiver.get(0);
			retirerElementAcultiver(elementsAcultiver.get(0));
		}
		return elementACultiver;
	}

	/**
	 * @param ElementEarth
	 *            AJoute un élément à construire. Attention si on place un
	 *            élément à l'endroit d'un objet en construction présent on le
	 *            suprimme.
	 */
	public static void ajouterElementAconstruireNEW(ElementEarth elementAconstruire) {
		retirerElementAconstruire(elementAconstruire.x, elementAconstruire.y);
		elementAconstruiresNEW.add(elementAconstruire);
	}

	/**
	 * @param elementAconstruire
	 */
	public static void ajouterElementAcultiver(ElementAconstruire elementAcultiver) {
		elementsAcultiver.add(elementAcultiver);
	}

	/**
	 * @param elementAconstruire
	 */
	public static void retirerElementAcultiver(ElementAconstruire elementAcultiver) {
		elementsAcultiver.remove(elementAcultiver);
	}

	/**
	 * @param x
	 * @param y
	 */
	public static boolean retirerElementAconstruire(final int x, final int y) {
		boolean isRemove = false;
		for (int i = 0; i < elementAconstruiresNEW.size(); i++) {
			ElementEarth elementAconstruire = elementAconstruiresNEW.get(i);
			if (elementAconstruire.x == x && elementAconstruire.y == y) {
				elementAconstruiresNEW.remove(i);
				isRemove = true;
				break;
			}
		}
		return isRemove;
	}

}
