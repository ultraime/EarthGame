package com.ultraime.game.metier;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.ultraime.database.Action;
import com.ultraime.database.base.Base;
import com.ultraime.game.utile.Parametre;

public class TiledMapClickListener extends ClickListener {

	private TiledMapActor actor;
	public static Boolean IGNORE_CLICK = false;

	public TiledMapClickListener(TiledMapActor actor) {
		this.actor = actor;
	}

	@Override
	public void clicked(InputEvent event, float x, float y) {
		if (!IGNORE_CLICK) {
			if (Parametre.MODE_DEBUG && Parametre.MODE_DEBUG_CLICK_CASE) {
				System.err.println("Clique sur la case -> " + actor.getX() / 64 + "  " + actor.getY() / 64);
			}

			Action.ordreConstructionElementNEW(actor, 0, 0);
			if (Parametre.MODE_DEBUG) {
				int posX = (int) (actor.getX() / 64);
				int posY = (int) (actor.getY() / 64);
				Action.elementEarthSelect = Base.getInstance().recupererElementEarth(posX, posY);
			}
		} else {
			IGNORE_CLICK = false;
		}
	}

	// private void deplacerEntiteVivante() {
	// Body body = Action.body;
	// EntiteVivante entiteVivante = null;
	// if (body.getUserData() instanceof EntiteVivante) {
	// entiteVivante = (EntiteVivante) body.getUserData();
	// float xTemp = (int) ((this.actor.getX() / WorldService.MULTIPLICATEUR));
	// float yTemp = (int) ((this.actor.getY() / WorldService.MULTIPLICATEUR));
	//
	// final int xArrive = Math.round(xTemp);
	// final int yArrive = Math.round(yTemp);
	//
	// ActionEntite actionEntite = new AEDeplacement(xArrive, yArrive, 1);
	// entiteVivante.ajouterAction(actionEntite);
	// }
	// }

	// private void construireMur() {
	// TiledMapTileSet tileSet =
	// actor.getTiledMap().getTileSets().getTileSet("tuile");
	// TiledMapTileLayer tiledLayer = (TiledMapTileLayer)
	// this.actor.getTiledMap().getLayers().get("OBJET_0");
	//
	// int posX = (int) actor.getX();
	// int posY = (int) actor.getY();
	//
	// Cell cell = new Cell();
	// cell.setTile(tileSet.getTile(1));
	// tiledLayer.setCell(posX / 64, posY / 64, cell);
	// // actor.cell.setTile(tileSet.getTile(6));
	// }
}
