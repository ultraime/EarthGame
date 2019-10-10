package com.ultraime.game.ecran;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.ultraime.game.utile.Parametre;

public class EcranManager extends ApplicationAdapter implements InputProcessor {
	/**
	 * L'ecran qui est actuellement utilisé
	 */
	public Ecran ecranActuel;

	public EcranPrincipal ecranPrincipal;

	public EcranDeTest ecranDeTest;

	public EcranDebug ecranDebug;

	public EcranJeu ecranCarte;

	public InputMultiplexer inputMultiplexer = new InputMultiplexer();

	public void initialiserEcran(final Ecran ecran) {
		inputMultiplexer.clear();
		inputMultiplexer.addProcessor(this);
		if (Parametre.MODE_DEBUG) {
			ecranDebug.changerEcran(inputMultiplexer);
		}
		ecran.changerEcran(inputMultiplexer);

		Gdx.input.setInputProcessor(inputMultiplexer);
		ecranActuel = ecran;

	}

	public void ajouterInput(InputProcessor inputNew) {
		inputMultiplexer.addProcessor(inputNew);
		Gdx.input.setInputProcessor(inputMultiplexer);
	}

	@Override
	public void create() {
		Gdx.graphics.setTitle("My Eart");

		Parametre.initLangue();
		inputMultiplexer = new InputMultiplexer();

		ecranDeTest = new EcranDeTest();
		ecranDeTest.create(this);
		ecranDebug = new EcranDebug(this);

		ecranCarte = new EcranJeu();
		ecranCarte.create(this);

		ecranPrincipal = new EcranPrincipal();
		initialiserEcran(ecranPrincipal);
		ecranActuel.create(this);

	}

	@Override
	public void render() {
//		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		//decomenter ?
//		 Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		ecranActuel.render();

		if (Parametre.MODE_DEBUG) {
			this.ecranDebug.render();
		}

	}

	@Override
	public void dispose() {

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

}
