package com.ultraime.game.metier;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class TiledMapStage extends Stage {

	private TiledMap tiledMap;

	public TiledMapStage(TiledMap tiledMap) {
		this.tiledMap = tiledMap;
		TiledMapTileLayer tiledLayer =(TiledMapTileLayer)  this.tiledMap.getLayers().get("SOL_0");
		createActorsForLayer(tiledLayer);

//		for (MapLayer layer : tiledMap.getLayers()) {
//			try {
//				TiledMapTileLayer tiledLayer = (TiledMapTileLayer) layer;
//				createActorsForLayer(tiledLayer);
//
//			} catch (Exception e) {
//				System.out.println("tilemap d objet :"+e);
//			}
//		
//		}
	}

	private void createActorsForLayer(TiledMapTileLayer tiledLayer) {
		for (int x = 0; x < tiledLayer.getWidth(); x++) {
			for (int y = 0; y < tiledLayer.getHeight(); y++) {
				TiledMapTileLayer.Cell cell = tiledLayer.getCell(x, y);
				TiledMapActor actor = new TiledMapActor(tiledMap, tiledLayer, cell);
				actor.setBounds(x * tiledLayer.getTileWidth(), y * tiledLayer.getTileHeight(),
						tiledLayer.getTileWidth(), tiledLayer.getTileHeight());
				addActor(actor);
				EventListener eventListener = new TiledMapClickListener(actor);
				actor.addListener(eventListener);
			}
		}
	}
}