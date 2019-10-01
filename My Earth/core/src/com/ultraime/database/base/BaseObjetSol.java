package com.ultraime.database.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.ultraime.database.ElementEarth;
import com.ultraime.game.metier.TileMapService;

public class BaseObjetSol implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<ElementEarth> elementEarthsObjetSol;

	public BaseObjetSol() {
		this.elementEarthsObjetSol = new ArrayList<ElementEarth>();
	}

	public List<ElementEarth> getElementEarthsObjetSol() {
		return elementEarthsObjetSol;
	}

	public void setElementEarthsObjetSol(List<ElementEarth> elementEarthsObjetSol) {
		this.elementEarthsObjetSol = elementEarthsObjetSol;
	}

	/**
	 * @param x
	 * @param y
	 * @return isObjetPresent
	 */
	public boolean isObjetPresent(final int x, final int y) {
		boolean isObjetPresent = false;
		for (int i = 0; i < elementEarthsObjetSol.size(); i++) {
			ElementEarth earth = elementEarthsObjetSol.get(i);
			if (x == earth.x && y == earth.y) {
				isObjetPresent = true;
				break;
			}
		}
		return isObjetPresent;
	}

	/**
	 * @param sousType
	 * @param elementEarthNext - Si non null, on récupére l'élément aprés
	 *                         elementEarthNext
	 * @return elementEarth (peut être null)
	 */
	public ElementEarth rechercheObjetSol(final String sousType, ElementEarth elementEarthNext) {
		ElementEarth elementEarth = null;

		for (int i = 0; i < elementEarthsObjetSol.size(); i++) {
			ElementEarth earth = elementEarthsObjetSol.get(i);
			if (elementEarthNext != null) {
				if (elementEarthNext == earth) {
					elementEarthNext = null;
				}
			} else if (sousType.equals(earth.sousType)) {
				elementEarth = earth;
				break;
			}

		}
		return elementEarth;

	}

	public void remove(final ElementEarth elemenCible) {
		TiledMapTileLayer layer = (TiledMapTileLayer) TileMapService.getInstance().tiledMap.getLayers()
				.get(TileMapService.SOL_1);
		TileMapService.getInstance().viderCellMap(elemenCible.x, elemenCible.y, layer);
		elementEarthsObjetSol.remove(elemenCible);

	}

}
