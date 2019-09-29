package com.ultraime.database;

import java.util.List;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.ultraime.database.base.Base;

/**
 * @author Ultraime
 *
 */
public class RotationManager {

	public static ElementEarth rotateNext(final ElementEarth elementEarth) {
		ElementEarth ElementEarthRotate = null;
		switch (elementEarth.rotation) {
		case ElementEarth.rot_haut:
			ElementEarthRotate = rotateDroite(elementEarth);
			break;
		case ElementEarth.rot_bas:
			ElementEarthRotate = rotateGauche(elementEarth);
			break;
		case ElementEarth.rot_droite:
			ElementEarthRotate = rotateBas(elementEarth);
			break;
		case ElementEarth.rot_gauche:
			ElementEarthRotate = rotateHaut(elementEarth);
			break;
		default:
			break;
		}

		return ElementEarthRotate;
	}

	public static ElementEarth getElementRotate(final ElementEarth elementEarth, String rotation) {
		ElementEarth ElementEarthRotate = null;
		switch (rotation) {
		case ElementEarth.rot_haut:
			ElementEarthRotate = rotateHaut(elementEarth);
			break;
		case ElementEarth.rot_bas:
			ElementEarthRotate = rotateBas(elementEarth);
			break;
		case ElementEarth.rot_droite:
			ElementEarthRotate = rotateDroite(elementEarth);
			break;
		case ElementEarth.rot_gauche:
			ElementEarthRotate = rotateGauche(elementEarth);
			break;
		default:
			break;
		}

		return ElementEarthRotate;
	}

	private static ElementEarth rotateHaut(ElementEarth elementEarth) {
		List<ElementEarthImage> elementEarthImageRef = Base.getInstance()
				.recupererElementEarthByNom(elementEarth.nom).elementEarthImages;

		ElementEarth earth = new ElementEarth(elementEarth);
		for (int i = 0; i < earth.elementEarthImages.size(); i++) {
			ElementEarthImage elementEarthImage = earth.elementEarthImages.get(i);
			final ElementEarthImage refImage = elementEarthImageRef.get(i);
			elementEarthImage.y = refImage.y;
			elementEarthImage.x = refImage.x;
		}
		earth.rotation = ElementEarth.rot_haut;
		return earth;
	}

	private static ElementEarth rotateGauche(ElementEarth elementEarth) {
		List<ElementEarthImage> elementEarthImageRef = Base.getInstance()
				.recupererElementEarthByNom(elementEarth.nom).elementEarthImages;

		ElementEarth earth = new ElementEarth(elementEarth);
		for (int i = 0; i < earth.elementEarthImages.size(); i++) {
			ElementEarthImage elementEarthImage = earth.elementEarthImages.get(i);
			final ElementEarthImage refImage = elementEarthImageRef.get(i);
			elementEarthImage.y = refImage.x;
			elementEarthImage.x = refImage.y - refImage.y - refImage.y;
		}
		earth.rotation = ElementEarth.rot_gauche;
		return earth;
	}

	private static ElementEarth rotateBas(ElementEarth elementEarth) {
		List<ElementEarthImage> elementEarthImageRef = Base.getInstance()
				.recupererElementEarthByNom(elementEarth.nom).elementEarthImages;

		ElementEarth earth = new ElementEarth(elementEarth);
		for (int i = 0; i < earth.elementEarthImages.size(); i++) {

			ElementEarthImage elementEarthImage = earth.elementEarthImages.get(i);
			final ElementEarthImage refImage = elementEarthImageRef.get(i);
			if (refImage.y > 0) {
				elementEarthImage.y = refImage.y - refImage.y - refImage.y;
				elementEarthImage.x = refImage.x;
				if(refImage.x > 0){
					elementEarthImage.x = refImage.x - refImage.x - refImage.x;
				}
			}
			else if (refImage.x > 0) {
				elementEarthImage.y = refImage.y;
				elementEarthImage.x = refImage.x - refImage.x - refImage.x;
			}

			else {
				elementEarthImage.y = refImage.y;
				elementEarthImage.x = refImage.x;
			}
			//System.err.println(i + " => x:" + elementEarthImage.x + " y:" + elementEarthImage.y);
		}
		earth.rotation = ElementEarth.rot_bas;
		return earth;
	}

	private static ElementEarth rotateDroite(final ElementEarth elementEarth) {
		List<ElementEarthImage> elementEarthImageRef = Base.getInstance()
				.recupererElementEarthByNom(elementEarth.nom).elementEarthImages;

		ElementEarth earth = new ElementEarth(elementEarth);
		for (int i = 0; i < earth.elementEarthImages.size(); i++) {
			ElementEarthImage elementEarthImage = earth.elementEarthImages.get(i);
			final ElementEarthImage refImage = elementEarthImageRef.get(i);
			if (refImage.y > 0 && refImage.x == 0) {
				elementEarthImage.y = 0;
				elementEarthImage.x = refImage.y;
			} else if (refImage.y == 0 && refImage.x > 0) {
				elementEarthImage.y = refImage.x - refImage.x - refImage.x;
				elementEarthImage.x = refImage.y;
			} else {
				elementEarthImage.y = refImage.y - refImage.y - refImage.y;
				elementEarthImage.x = refImage.y;
			}
		}
		earth.rotation = ElementEarth.rot_droite;
		return earth;
	}

	public static void rotateCell(Cell cell, String rotation) {
		switch (rotation) {
		case ElementEarth.rot_haut:
			cell.setRotation(Cell.ROTATE_0);
			cell.setFlipHorizontally(false);
			break;
		case ElementEarth.rot_bas:
			cell.setRotation(Cell.ROTATE_180);
			break;
		case ElementEarth.rot_droite:
			cell.setRotation(Cell.ROTATE_270);
			break;
		case ElementEarth.rot_gauche:
			cell.setRotation(Cell.ROTATE_90);
			cell.setFlipHorizontally(true);
			break;
		default:
			break;
		}

	}

}
