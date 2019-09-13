package com.ultraime.game.ecran;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.TimeUtils;
import com.ultraime.database.Action;
import com.ultraime.database.ElementEarth;
import com.ultraime.game.utile.Parametre;

/**
 * @author Ultraime
 */
public class EcranDebug extends Ecran implements Disposable {
	long lastTimeCounted;
	private float sinceChange;
	private float frameRate;
	private BitmapFont font;
	private SpriteBatch batch;
	private OrthographicCamera cam;

	@Override
	public void changerEcran(InputMultiplexer inputMultiplexer) {
		inputMultiplexer.addProcessor(this);

	}

	public EcranDebug(EcranManager ecranManager) {
		lastTimeCounted = TimeUtils.millis();
		sinceChange = 0;
		frameRate = Gdx.graphics.getFramesPerSecond();
		font = new BitmapFont();
		font.setColor(Color.RED);
		batch = new SpriteBatch();

	}

	public void resize(int screenWidth, int screenHeight) {
		cam = new OrthographicCamera(screenWidth, screenHeight);
		cam.translate(screenWidth / 2, screenHeight / 2);
		cam.update();
		batch.setProjectionMatrix(cam.combined);
	}

	public void update() {
		if (Parametre.SHOW_FPS) {
			long delta = TimeUtils.timeSinceMillis(lastTimeCounted);
			lastTimeCounted = TimeUtils.millis();
			sinceChange += delta;
			if (sinceChange >= 1000) {
				sinceChange = 0;
				frameRate = Gdx.graphics.getFramesPerSecond();
			}
		}

	}

	/**
	 * 
	 */
	public void updateFps() {

	}

	public void render() {
		update();
		batch.begin();
		if (Parametre.SHOW_FPS) {
			font.draw(batch, (int) frameRate + " fps", 3, Gdx.graphics.getHeight() - 0);
		}

		if (Action.elementEarthSelect != null) {
			if (Action.elementEarthSelect.inventaire != null) {
				font.draw(batch, "Inventaire : " + Action.elementEarthSelect.inventaire.capaciteActuel + "/"
						+ Action.elementEarthSelect.inventaire.capaciteMax, 3, Gdx.graphics.getHeight() - 13);
			} else if (Action.elementEarthSelect.sousType != null
					&& Action.elementEarthSelect.sousType.equals(ElementEarth.literie)) {
				if (Action.elementEarthSelect.proprietaire != null) {
					font.draw(batch, "Propriétaire : " + Action.elementEarthSelect.proprietaire.prenom, 3,
							Gdx.graphics.getHeight() - 13);
				}else{
					font.draw(batch, "Le lit n a pas de propriétaire", 3,
							Gdx.graphics.getHeight() - 13);
				}
			}
		}
		batch.end();
	}

	public void dispose() {
		font.dispose();
		batch.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {

		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	@Override
	public void create(EcranManager ecranManager) {

	}
}
