package com.ultraime.database;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.ultraime.database.base.Base;
import com.ultraime.game.entite.EntiteVivante;
import com.ultraime.game.metier.TileMapService;
import com.ultraime.game.metier.TiledMapActor;
import com.ultraime.game.metier.travail.action.AEConstruction;
import com.ultraime.game.metier.travail.action.ActionEntite;

/**
 * @author Ultraime
 *
 */
public class Action {
	private static final int HAUT = 0;
	private static final int BAS = 1;
	private static final int GAUCHE = 2;
	private static final int DROITE = 3;

	public static Ordre ORDRE = null;

	// action de constrcution
	public static int PLACER_MUR_START = 1;
	public static int PLACER_MUR = 2;
	public static int PLACER_MUR_AUTO = 3;

	public static int PLACER_MUR_CONSTRUCTION_START = 4;
	public static int PLACER_MUR_CONSTRUCTION = 5;
	public static int PLACER_MUR_CONSTRUCTION_AUTO = 6;
	// action du personnage
	public static int DEPLACEMENT = 7;

	// action v2
	public static final int ORDRE_CONSTRUIRE_MUR = 8;
	public static final int ORDRE_CONSTRUIRE_MUR_LIGNE = 9;
	public static final int ORDRE_CONSTRUIRE_PORTE = 10;
	public static final int ORDRE_CULTURE_MAIS = 11;
	public static final int ORDRE_CULTURE_MAIS_RECTANGLE = 12;
	public static final int ORDRE_CONSTRUCTION = 13;

	public static List<Vector2> vectorsConstruction = new ArrayList<>();
	public static Vector2 vectorDepart;
	/**
	 * body selectionné
	 */
	public static Body body = null;

	/**
	 * Action en cours de création pour le Body. utile pour la construction
	 */
	public static ActionEntite actionEntite = null;

	public static Vector2 caseDepart = new Vector2(0, 0);

	public static ElementEarth elementEarthSelect;

	public static void removeBody() {
		ajouterAction();
		body = null;
		ordreReset();
	}

	public static void ajouterAction() {
		if (actionEntite != null) {
			EntiteVivante ev = (EntiteVivante) body.getUserData();
			ev.ajouterAction(actionEntite);
			actionEntite = null;
		}
	}

	public static void ordre(final Ordre ordre) {
		ordreReset();
		vectorDepart = null;
		ORDRE = ordre;
	}

	/**
	 * on reset l'action
	 */
	public static void ordreReset() {
		if (ORDRE != null && ORDRE.elementEarth != null
				&& ORDRE.elementEarth.placementType.equals(ElementEarth.unique)) {
			vectorDepart = null;
		}
		viderVectorConstruction();
		ORDRE = null;
	}

	public static void actionDeConstruction(ElementEarth elementAconstruire) {
		if (actionEntite == null) {
			actionEntite = new AEConstruction(1);
		}
		if (actionEntite instanceof AEConstruction) {
			AEConstruction actionEntiteConstruction = (AEConstruction) actionEntite;
			actionEntiteConstruction.ajouterElementAconstruire(elementAconstruire);
		}

	}

	public static boolean entiteFaitAction() {
		boolean isDoAction = false;
		if (ORDRE != null) {
			isDoAction = true;

		}
		return isDoAction;
	}

	/**
	 * construit un élément sur une case (élément temporaire).
	 * 
	 * @param actor
	 * @param x
	 * @param y
	 */
	public static void ordreConstructionElementNEW(final TiledMapActor actor, final float x, final float y) {
		if (actor != null) {
			// initialisation de l'ordre.
			if (ORDRE != null && ORDRE.id == Ordre.construire) {
				if (vectorDepart != null && !ORDRE.elementEarth.placementType.equals(ElementEarth.unique)) {
					validerConstruction();
				} else {
					int posX = (int) actor.getX();
					int posY = (int) actor.getY();
					final int xFinal = posX / 64;
					final int yFinal = posY / 64;
					vectorDepart = new Vector2(xFinal, yFinal);
					if (ORDRE.elementEarth.placementType.equals(ElementEarth.unique)) {
						// ici on valide la construction d'un objet de type
						// unique
						if (ControleurActions.isConstructible(ORDRE.elementEarth)) {
							validerConstruction();
						}
					} else {
						alimenterVectorConstructionLayer(TileMapService.CONSTRUCTION_TEMP);
					}
				}
			}
		} else if (ORDRE != null && ORDRE.id == Ordre.construire && vectorDepart != null
				&& !ORDRE.elementEarth.placementType.equals(ElementEarth.unique)) {

			viderVectorConstruction();
			Vector2 positionSouris = new Vector2(x / 64, y / 64);

			if (ORDRE.elementEarth.placementType.equals(ElementEarth.ligne)) {
				dessinerLigneAConstruire(positionSouris);
			} else if (ORDRE.elementEarth.placementType.equals(ElementEarth.rectangle)) {
				dessinerRectangleAConstuire(positionSouris);
			}
			alimenterVectorConstructionLayer(TileMapService.CONSTRUCTION_TEMP);
		} else if (ORDRE != null && ORDRE.id == Ordre.construire
				&& ORDRE.elementEarth.placementType.equals(ElementEarth.unique)) {
			// si on construit un objet d'un bloc (pas de ligne, pas de
			// rectangle etc..)
			viderVectorConstruction();
			Vector2 positionSouris = new Vector2(x / 64, y / 64);
			vectorDepart = new Vector2(positionSouris.x, positionSouris.y);
			alimenterVectorConstructionLayer(TileMapService.CONSTRUCTION_TEMP);
		}

	}

	private static void dessinerRectangleAConstuire(Vector2 positionSouris) {
		float axeX = vectorDepart.x - positionSouris.x;
		float axeY = vectorDepart.y - positionSouris.y;
		Vector2 vectorMultiDirection = calculerMultiDirection(axeX, axeY);
		alimenterVectorConstructionRectangle(positionSouris, vectorMultiDirection.y, vectorMultiDirection.x);
	}

	private static void dessinerLigneAConstruire(Vector2 positionSouris) {
		int direction = (int) (vectorDepart.x - positionSouris.x);
		direction = calculerLigneDirection(positionSouris);
		alimenterVectorConstruction(positionSouris, direction);
	}

	/**
	 * @param axeX
	 * @param axeY
	 * @return Vector2, Vector2.y = haut/nas; Vector2.y gauche/droite
	 */
	private static Vector2 calculerMultiDirection(float axeX, float axeY) {
		Vector2 multiDirection = new Vector2();
		multiDirection.y = -1;
		multiDirection.x = -1;
		// (haut,gauche,droite ou bas
		if (axeX < 0) {
			multiDirection.x = DROITE;
		} else {
			multiDirection.x = GAUCHE;
		}

		if (axeY < 0) {
			multiDirection.y = HAUT;
		} else {
			multiDirection.y = BAS;
		}
		return multiDirection;
	}

	private static int calculerLigneDirection(Vector2 positionSouris) {
		int direction;
		float axeX = vectorDepart.x - positionSouris.x;
		float axeY = vectorDepart.y - positionSouris.y;
		direction = HAUT;

		// (haut,gauche,droite ou bas
		if (Math.abs(axeX) > Math.abs(axeY)) {
			if (axeX < 0) {
				direction = DROITE;
			} else {
				direction = GAUCHE;
			}
		} else {
			if (axeY < 0) {
				direction = HAUT;
			} else {
				direction = BAS;
			}
		}
		return direction;
	}

	/**
	 * @param positionSouris
	 * @param y
	 * @param x
	 */
	private static void alimenterVectorConstructionRectangle(Vector2 positionSouris, float y, float x) {

		positionSouris.x = (int) positionSouris.x;
		positionSouris.y = (int) positionSouris.y;
		vectorsConstruction.add(vectorDepart);

		// on dessine un carré.
		int xDepart = (int) vectorDepart.x;
		int yDepart = (int) vectorDepart.y;
		int xFin = (int) positionSouris.x;
		int yFin = (int) positionSouris.y;

		if (xDepart - xFin > 0) {
			int temp = xFin;
			xFin = xDepart + 1;
			xDepart = temp;
			xFin--;
		}
		if (yDepart - yFin > 0) {
			int temp = yFin;
			yFin = yDepart + 1;
			yDepart = temp;
			yFin--;
		}
		xFin++;
		yFin++;
		// if (xFin > xDepart && yFin > yDepart) {
		for (int i = xDepart; i < xFin; i++) {
			for (int j = yDepart; j < yFin; j++) {
				final Vector2 vector2 = new Vector2(i, j);
				vectorsConstruction.add(vector2);
			}
			// }
		}

	}

	private static void alimenterVectorConstructionLayer(final String layserStr) {
		if (vectorsConstruction.size() == 0) {
			vectorsConstruction.add(vectorDepart);
		}
		for (int i = 0; i < vectorsConstruction.size(); i++) {
			Vector2 vector2 = vectorsConstruction.get(i);
			TiledMapTileLayer tiledLayer = TileMapService.getInstance().getLayers(layserStr);
			int posX = (int) vector2.x;
			int posY = (int) vector2.y;
			ControleurActions.controlerIdTuileNone(ORDRE.elementEarth, posX, posY);
			TileMapService.getInstance().alimenterImageFromMultiTile(ORDRE.elementEarth, tiledLayer, posX, posY, false);
		}

	}

	private static void alimenterVectorConstructionLayer(Vector2 vector2) {
		TiledMapTileLayer tiledLayer = TileMapService.getInstance().getLayers(ORDRE.elementEarth.layerCible);
		int posX = (int) vector2.x;
		int posY = (int) vector2.y;
		TileMapService.getInstance().alimenterImageFromMultiTile(ORDRE.elementEarth, tiledLayer, posX, posY, false);
	}

	public static void viderVectorConstruction() {
		if (vectorsConstruction.size() == 0) {
			if (vectorDepart != null) {
				vectorsConstruction.add(vectorDepart);
			}
		}
		for (int i = 0; i < vectorsConstruction.size(); i++) {
			Vector2 vector2 = vectorsConstruction.get(i);

			TiledMapTileLayer tiledLayer = TileMapService.getInstance().getLayers(TileMapService.CONSTRUCTION_TEMP);

			int posX = (int) vector2.x;
			int posY = (int) vector2.y;

			Cell cell = new Cell();
			cell.setTile(null);
			TileMapService.getInstance().alimenterImageFromMultiTile(ORDRE.elementEarth, tiledLayer, posX, posY, true);
			tiledLayer.setCell(posX, posY, cell);

		}
		vectorsConstruction.clear();
	}

	private static void alimenterVectorConstruction(Vector2 positionSouris, int direction) {
		positionSouris.x = (int) positionSouris.x;
		positionSouris.y = (int) positionSouris.y;
		vectorsConstruction.add(vectorDepart);
		if (direction == GAUCHE) {
			int nbBoucle = (int) (vectorDepart.x - Math.abs(positionSouris.x));
			nbBoucle = Math.abs(nbBoucle) + 1;
			for (int i = 0; i < nbBoucle; i++) {
				final Vector2 vector2 = new Vector2(vectorDepart.x - i, vectorDepart.y);
				vectorsConstruction.add(vector2);
			}
		} else if (direction == DROITE) {
			int nbBoucle = (int) (vectorDepart.x - Math.abs(positionSouris.x));
			nbBoucle = Math.abs(nbBoucle) + 1;
			for (int i = 0; i < nbBoucle; i++) {
				final Vector2 vector2 = new Vector2(vectorDepart.x + i, vectorDepart.y);
				vectorsConstruction.add(vector2);
			}

		} else if (direction == HAUT) {
			int nbBoucle = (int) (vectorDepart.y - Math.abs(positionSouris.y));
			nbBoucle = Math.abs(nbBoucle) + 1;
			for (int i = 0; i < nbBoucle; i++) {
				final Vector2 vector2 = new Vector2(vectorDepart.x, vectorDepart.y + i);
				vectorsConstruction.add(vector2);
			}
		} else if (direction == BAS) {
			int nbBoucle = (int) (vectorDepart.y - Math.abs(positionSouris.y));
			nbBoucle = Math.abs(nbBoucle) + 1;
			for (int i = 0; i < nbBoucle; i++) {
				final Vector2 vector2 = new Vector2(vectorDepart.x, vectorDepart.y - i);
				vectorsConstruction.add(vector2);
			}
		}

	}

	private static void validerConstruction() {
		if (vectorsConstruction.size() == 0) {
			vectorsConstruction.add(vectorDepart);
		}

		for (int i = 0; i < vectorsConstruction.size(); i++) {
			Vector2 vector2 = vectorsConstruction.get(i);
			final int posX = (int) vector2.x;
			final int posY = (int) vector2.y;

			ElementEarth earth = new ElementEarth(ORDRE.elementEarth);
			earth.x = posX;
			earth.y = posY;
			boolean doitEtreConstruit = true;
			if (ElementEarth.culture_sol_constructible.equals(earth.type)) {
				if (Base.getInstance().isObjetPresentSaufPlante(posX, posY)) {
					doitEtreConstruit = false;
				}
			}
			if (doitEtreConstruit) {
				TileMapService.ajouterElementAconstruireNEW(earth);
				alimenterVectorConstructionLayer(vector2);
				if (earth.elementEarthImages.get(0).isCollision) {
					final Rectangle rectangle = new Rectangle(posX, posY, 0.5f, 0.5f);
					Base.getInstance().ajouterRectangleConstructible(rectangle);
				}
			}

		}
		vectorDepart = null;
		viderVectorConstruction();

	}

	/**
	 * si l'on a bien un objet de selectionné on le rotate.
	 */
	public static void rotateObjet() {
		if (ORDRE != null && ORDRE.elementEarth != null && vectorDepart != null) {
			final float x = vectorDepart.x * 64;
			final float y = vectorDepart.y * 64;
			viderVectorConstruction();
			ORDRE.elementEarth = RotationManager.rotateNext(ORDRE.elementEarth);

			ordreConstructionElementNEW(null, x, y);
		}
	}

	public static ElementEarth doRotate(ElementEarth elementEvolue) {
		switch (elementEvolue.rotation) {
		case ElementEarth.rot_bas:
			break;
		case ElementEarth.rot_droite:
			break;
		case ElementEarth.rot_gauche:
			break;
		default:
			break;
		}
		return null;
	}

}
