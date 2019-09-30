package com.ultraime.game.metier;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;

public class TileMurManager {

	public TiledMap tiledMap;



	public TileMurManager(final TiledMap tiledMap) {
		this.tiledMap = tiledMap;
	}

	public void creerMurEnBois() {
		final TiledMapTileLayer tilePourMur = (TiledMapTileLayer) tiledMap.getLayers().get(TileMapService.OBJET_0);
		final TiledMapTileSet tileSet = this.tiledMap.getTileSets().getTileSet(TileMapService.TUILE);
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

	public boolean isMurAgauche(final int x, final int y) {
		boolean isMurAgauche = false;
		final TiledMapTileLayer tilePourMur = (TiledMapTileLayer) tiledMap.getLayers().get(TileMapService.OBJET_0);
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
		final TiledMapTileLayer tilePourMur = (TiledMapTileLayer) tiledMap.getLayers().get(TileMapService.OBJET_0);
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
		final TiledMapTileLayer tilePourMur = (TiledMapTileLayer) tiledMap.getLayers().get(TileMapService.OBJET_0);
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
		final TiledMapTileLayer tilePourMur = (TiledMapTileLayer) tiledMap.getLayers().get(TileMapService.OBJET_0);
		if (y > 1) {
			if (tilePourMur.getCell(x, y - 1) != null) {
				if (isMurEnBois(tilePourMur.getCell(x, y - 1).getTile().getId())) {
					isMurEnBas = true;
				}
			}
		}
		return isMurEnBas;
	}

	public static Boolean isMurEnBois(int id) {
		boolean isMurEnBois = false;
		if (id > 0 && id < 10) {
			isMurEnBois = true;
		}
		return isMurEnBois;
	}
}
