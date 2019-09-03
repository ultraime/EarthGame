package com.ultraime.music;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.ultraime.database.ElementEarth;

public class MusicManager {

	private Sound construire;

	private MusicManager() {
		construire = Gdx.audio.newSound(Gdx.files.internal("data/son/construire.ogg"));
	}

	private static MusicManager INSTANCE = null;

	public static MusicManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new MusicManager();
		}
		return INSTANCE;
	}

	public void construire(ElementEarth earth) {
		if (!earth.type.equals(ElementEarth.culture) && !earth.type.equals(ElementEarth.culture_final)
				&& !earth.type.equals(ElementEarth.culture_sol)
				&& !earth.type.equals(ElementEarth.culture_sol_constructible)) {
			construire.play();
		}
	}
}
