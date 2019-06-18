package com.ultraime.game.utile;

import java.util.Locale;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.I18NBundle;

public class Parametre {

	public static final int MONDE_Y = 200;
	public static final int MONDE_X = 200;
	public static final String FONT = "fonts/OpenSans-Bold.fnt";
	public static int LARGEUR_ECRAN = 0;// Gdx.graphics.getWidth();
	public static int HAUTEUR_ECRAN = 0;// Gdx.graphics.getHeight();
	public static int DECALAGE_Y = 0;

	public static boolean MODE_DEBUG = true;
	public static boolean MODE_DEBUG_CLICK_CASE = false;
	public static boolean ACTIVER_LUMIERE = false;
	public static int VITESSE_DE_JEU = 1;

	public static int VITESSE_NORMAL = 1;
	public static int VITESSE_RAPIDE = 2;
	public static int VITESSE_TRES_RAPIDE = 3;

	public static boolean PAUSE = false;

	// langue
	public static I18NBundle bundle;

	public static void initEcran(int largeurEcran, int hauteurEcran) {
		LARGEUR_ECRAN = largeurEcran;
		HAUTEUR_ECRAN = hauteurEcran;
		if (HAUTEUR_ECRAN < 1050) {
			DECALAGE_Y = -10;
		}
	}

	public static float y(float y) {
		// return y ;
		return (int) y * HAUTEUR_ECRAN / 1080f;
	}

	public static float x(float x) {
		// return x ;
		return (int) x * LARGEUR_ECRAN / 1920f;
	}

	public static void initLangue() {
		FileHandle baseFileHandle = Gdx.files.internal("i18n/Bundle");
		Locale locale = new Locale(Locale.FRANCE.getLanguage(), Locale.FRANCE.getCountry());
		bundle = I18NBundle.createBundle(baseFileHandle, locale);
	}

	public synchronized static void gererPause() {
		if (PAUSE) {
			PAUSE = false;
		} else {
			PAUSE = true;
		}
	}

	public static void gererVitesse(int vitesse) {
		VITESSE_DE_JEU = vitesse;
	}

}
