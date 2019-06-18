package com.ultraime.game.entite;

import com.ultraime.game.metier.TileMapService;
import com.ultraime.game.utile.Parametre;

public class ElementAconstruire {

	// Element à construire
	public static final int MUR_EN_BOIS = 11;
	public static final int PORTE_EN_BOIS = 12;
	public static final int CULTURE_MAIS_TERRE = 14;
	public static final int CULTURE_MAIS = 15;

	public int type;
	public int x;
	public int y;
	public String tiledLayer;
	public int id_tuile = -1;
	public Boolean isCulture = false;
	public int tuileFinal;
	public boolean isCollision = false;

	public ElementAconstruire(final int type, final int posX, final int posY) {
		this.type = type;
		this.x = posX;
		this.y = posY;
		initElement();
	}

	private void initElement() {
		switch (type) {
		case MUR_EN_BOIS:
			tuileFinal = 1;
			isCollision = true;
			tiledLayer = TileMapService.OBJET_0;
			break;
		case PORTE_EN_BOIS:
			tuileFinal = 10;
			tiledLayer = TileMapService.OBJET_0;
			break;
		case CULTURE_MAIS_TERRE:
			tuileFinal = 13;
			tiledLayer = TileMapService.SOL_0;
			isCulture = true;
			break;
		case CULTURE_MAIS:
			tuileFinal = 15;
			tiledLayer = TileMapService.OBJET_0;
			isCulture = true;
			break;
		default:
			if (Parametre.MODE_DEBUG) {
				System.err.println("ElementAconstruire.initElement à définir pour le type : " + type);
			}
			tuileFinal = 1;
			break;
		}

	}

	public ElementAconstruire getCulture() {
		ElementAconstruire elementAconstruire = null;
		switch (type) {
		case CULTURE_MAIS_TERRE:
			elementAconstruire = new ElementAconstruire(CULTURE_MAIS, x, y);
			break;
		default:
			// on ne fait rien
			break;
		}
		return elementAconstruire;
	}
}
