package com.ultraime.game.utile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.ultraime.composant.Bouton;

public class Image {
	private static Texture tableauImage[] = new Texture[500];

	private static Texture btnNormal[] = new Texture[200];
	private static Texture btnOver[] = new Texture[200];
	private static Texture btnClique[] = new Texture[500];

	public static void initialisation() {
		getImage(1);
		getBtnNormal(1);
		getBtnClique(1);
		getBtnOver(1);
	}

	public static Texture getImage(final int typeElement) {
		if (tableauImage[1] == null) {
			tableauImage[VariableCommune.PERSO1] = new Texture(Gdx.files.internal("personnage/perso1.png"));
			tableauImage[VariableCommune.HUD_MENU_BAS_GAUCHE] = new Texture(Gdx.files.internal("hud/hud_menu_bas_gauche.png"));
			tableauImage[VariableCommune.LOGO_VITESSE1] = new Texture(Gdx.files.internal("logo/vitesse1.png"));
			tableauImage[VariableCommune.LOGO_VITESSE2] = new Texture(Gdx.files.internal("logo/vitesse2.png"));
			tableauImage[VariableCommune.LOGO_VITESSE3] = new Texture(Gdx.files.internal("logo/vitesse3.png"));
			tableauImage[VariableCommune.LOGO_PAUSE] = new Texture(Gdx.files.internal("logo/pause.png"));
			tableauImage[VariableCommune.HUD_CADRE_STATS] = new Texture(Gdx.files.internal("hud/hud_cadre_stats.png"));
		}
		return tableauImage[typeElement];
	}

	// pour les boutons
	public static Texture getBtnOver(final int typeElement) {
		if (btnOver[1] == null) {
			btnOver[Bouton.CLASSIQUE] = new Texture(Gdx.files.internal("bouton/btn_over.png"));
			btnOver[Bouton.DEPLACEMENT] = new Texture(Gdx.files.internal("bouton/btn_deplacement_over.png"));
			btnOver[Bouton.CONSTRUCTION] = new Texture(Gdx.files.internal("bouton/btn_construction_over.png"));
			btnOver[Bouton.ROND_HD_MENU_116PX] = new Texture(Gdx.files.internal("bouton/bouton_rond_116px_over.png"));
			btnOver[Bouton.ROND_HD_MENU_30PX] = new Texture(Gdx.files.internal("bouton/bouton_rond_30px_over.png"));
			btnOver[Bouton.RECT_3_PART_1] = new Texture(Gdx.files.internal("bouton/bouton_3parts/bouton_rect_1_over.png"));
			btnOver[Bouton.RECT_3_PART_2] = new Texture(Gdx.files.internal("bouton/bouton_3parts/bouton_rect_2_over.png"));
			btnOver[Bouton.RECT_3_PART_3] = new Texture(Gdx.files.internal("bouton/bouton_3parts/bouton_rect_3_over.png"));
			btnOver[Bouton.CARRE_50PX] = new Texture(Gdx.files.internal("bouton/btn_carre_50px_over.png"));
		}
		return btnOver[typeElement];
	}

	public static Texture getBtnClique(final int typeElement) {
		if (btnClique[1] == null) {
			btnClique[Bouton.CLASSIQUE] = new Texture(Gdx.files.internal("bouton/btn_clique.png"));
			btnClique[Bouton.DEPLACEMENT] = new Texture(Gdx.files.internal("bouton/btn_deplacement_clique.png"));
			btnClique[Bouton.CONSTRUCTION] = new Texture(Gdx.files.internal("bouton/btn_construction_clique.png"));
			btnClique[Bouton.ROND_HD_MENU_116PX] = new Texture(Gdx.files.internal("bouton/bouton_rond_116px_clique.png"));
			btnClique[Bouton.ROND_HD_MENU_30PX] = new Texture(Gdx.files.internal("bouton/bouton_rond_30px_clique.png"));
			btnClique[Bouton.RECT_3_PART_1] = new Texture(Gdx.files.internal("bouton/bouton_3parts/bouton_rect_1_clique.png"));
			btnClique[Bouton.RECT_3_PART_2] = new Texture(Gdx.files.internal("bouton/bouton_3parts/bouton_rect_2_clique.png"));
			btnClique[Bouton.RECT_3_PART_3] = new Texture(Gdx.files.internal("bouton/bouton_3parts/bouton_rect_3_clique.png"));
			btnClique[Bouton.CARRE_50PX] = new Texture(Gdx.files.internal("bouton/btn_carre_50px_clique.png"));
		}
		return btnClique[typeElement];
	}

	public static Texture getBtnNormal(final int typeElement) {
		if (btnNormal[1] == null) {
			btnNormal[Bouton.CLASSIQUE] = new Texture(Gdx.files.internal("bouton/btn_normal.png"));
			btnNormal[Bouton.DEPLACEMENT] = new Texture(Gdx.files.internal("bouton/btn_deplacement_normal.png"));
			btnNormal[Bouton.CONSTRUCTION] = new Texture(Gdx.files.internal("bouton/btn_construction_normal.png"));
			btnNormal[Bouton.ROND_HD_MENU_116PX] = new Texture(Gdx.files.internal("bouton/bouton_rond_116px_normal.png"));
			btnNormal[Bouton.ROND_HD_MENU_30PX] = new Texture(Gdx.files.internal("bouton/bouton_rond_30px_normal.png"));
			
			btnNormal[Bouton.RECT_3_PART_1] = new Texture(Gdx.files.internal("bouton/bouton_3parts/bouton_rect_1_normal.png"));
			btnNormal[Bouton.RECT_3_PART_2] = new Texture(Gdx.files.internal("bouton/bouton_3parts/bouton_rect_2_normal.png"));
			btnNormal[Bouton.RECT_3_PART_3] = new Texture(Gdx.files.internal("bouton/bouton_3parts/bouton_rect_3_normal.png"));
			btnNormal[Bouton.RECT_3_PART_1_S] = new Texture(Gdx.files.internal("bouton/bouton_3parts/bouton_rect_1_select.png"));
			btnNormal[Bouton.RECT_3_PART_2_S] = new Texture(Gdx.files.internal("bouton/bouton_3parts/bouton_rect_2_select.png"));
			btnNormal[Bouton.RECT_3_PART_3_S] = new Texture(Gdx.files.internal("bouton/bouton_3parts/bouton_rect_3_select.png"));
			
			btnNormal[Bouton.CARRE_50PX] = new Texture(Gdx.files.internal("bouton/btn_carre_50px_normal.png"));
		}
		return btnNormal[typeElement];
	}

}
