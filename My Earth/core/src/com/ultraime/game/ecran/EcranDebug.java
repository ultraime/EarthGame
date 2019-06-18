package com.ultraime.game.ecran;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.TimeUtils;

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
		long delta = TimeUtils.timeSinceMillis(lastTimeCounted);
		lastTimeCounted = TimeUtils.millis();

		sinceChange += delta;
		if (sinceChange >= 1000) {
			sinceChange = 0;
			frameRate = Gdx.graphics.getFramesPerSecond();
		}
	}

	public void render() {
		update();
		batch.begin();
		font.draw(batch, (int) frameRate + " fps", 3, Gdx.graphics.getHeight() - 0);

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
