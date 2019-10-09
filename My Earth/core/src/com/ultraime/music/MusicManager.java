package com.ultraime.music;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.ultraime.database.entite.ElementEarth;

public class MusicManager {

	private Sound construire;
	private Music music;

	private MusicManager() {
		construire = Gdx.audio.newSound(Gdx.files.internal("data/son/construire.ogg"));
		music = Gdx.audio.newMusic(Gdx.files.internal("data/son/music.ogg"));

	}

	private static MusicManager INSTANCE = null;

	public static MusicManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new MusicManager();
		}
		return INSTANCE;
	}

	public void musicDeFond() {
//		music.setVolume(1f);
//		music.setLooping(true);
//		music.play();

	}

	public void construire(ElementEarth earth) {
		if (!earth.type.equals(ElementEarth.culture) && !earth.type.equals(ElementEarth.culture_final)
				&& !earth.type.equals(ElementEarth.culture_sol)
				&& !earth.type.equals(ElementEarth.culture_sol_constructible)) {
			construire.play();
		}
	}
}
