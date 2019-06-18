package com.ultraime.game.ecran;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.ultraime.game.metier.WorldBodyService;
import com.ultraime.game.metier.WorldService;
import com.ultraime.game.utile.Parametre;

public class EcranDeTest extends Ecran {

	// Pour la Police d'écriture
	private BitmapFont font;
	private BitmapFont bitmapFont;
	// Pour la carte
	private TiledMap tiledMap;
	private OrthogonalTiledMapRenderer rendererMap;
	private CameraGame cameraGame;

	// Pour le monde
	private Box2DDebugRenderer debugRenderer;
	private WorldService monde;
	private WorldService mondeDebug;
	private static int POSITION = 64;
	private static int TAILLE = 32;
	Body bodyThatWasHit = null;

	@Override
	public void changerEcran(InputMultiplexer inputMultiplexer) {
		inputMultiplexer.addProcessor(this);
	}

	@Override
	public void create(EcranManager ecranManager) {
		// Pour la Police d'écriture
		font = new BitmapFont();
		font.setColor(Color.WHITE);
		this.bitmapFont = new BitmapFont(Gdx.files.internal("fonts/OpenSans-Bold.fnt"), false);
		this.bitmapFont.getData().setScale(0.7f);
		// Création de l'écran
		this.ecranManager = ecranManager;
		this.batch = new SpriteBatch();

		this.cameraGame = new CameraGame();

		// Pour la carte
		tiledMap = new TmxMapLoader().load("carte/carte.tmx");
		rendererMap = new OrthogonalTiledMapRenderer(tiledMap, 1);
		for (MapObject object : tiledMap.getLayers().get("position").getObjects()) {
			if (object.getName().equals("depart")) {
				this.cameraGame.camera.position.x = (Float) object.getProperties().get("x");
				this.cameraGame.camera.position.y = (Float) object.getProperties().get("y");
			}
		}

		// Pour le monde
		this.monde = WorldService.getInstance();
		this.debugRenderer = new Box2DDebugRenderer();

		//
		// float posx = this.cameraGame.camera.position.x / TAILLE;
		// float posy = this.cameraGame.camera.position.y / TAILLE;
		// WorldBodyService.creerRectangleStatic(this.monde.world, posx, posy,
		// 1, 1, null);

		TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get("SOL_0");

		for (int x = 0; x < layer.getWidth(); x++) {
			for (int y = 0; y < layer.getHeight(); y++) {
				// layer.getCell(x, y).getTile().
				int id = layer.getCell(x, y).getTile().getId();
				if (id == 6 || id == 7 || id == 8 || id == 9 || id == 18 || id == 19) {
					WorldBodyService.creerRectangleStatic(this.monde.world, x * POSITION + 32, y * POSITION + 32,
							TAILLE, TAILLE, null);
				}
			}

		}

		// WorldBodyService.creerCercleVivant(this.mondeDebug.world, 30, posx *
		// TAILLE, posy * TAILLE);

	}

	// création de l'IHM

	@Override
	public void render() {
		updateCamera();
		this.batch.begin();
		this.rendererMap.render();
		this.monde.render();
		if (Parametre.MODE_DEBUG) {
			this.mondeDebug.render();
			this.debugRenderer.render(this.mondeDebug.world, this.cameraGame.camera.combined);
			this.debugRenderer.render(this.monde.world, this.cameraGame.camera.combined);
		}
		this.batch.end();
	}

	private void updateCamera() {
		this.cameraGame.updateCamera();

		OrthographicCamera camera = this.cameraGame.camera;
		// camera.position.x = utilisateur.getJoueur().getX() + 100;
		// camera.position.y = utilisateur.getJoueur().getY();

		batch.setProjectionMatrix(camera.combined);
		rendererMap.setView(camera);
	}

	@Override
	public void dispose() {
		this.batch.dispose();
		this.bitmapFont.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		switch (keycode) {
		case Input.Keys.Z:
			this.cameraGame.isMonter = true;
			break;
		case Input.Keys.S:
			this.cameraGame.isDescendre = true;
			break;
		case Input.Keys.D:
			this.cameraGame.isDroite = true;
			break;
		case Input.Keys.Q:
			this.cameraGame.isGauche = true;
			break;
		default:
			break;
		}
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		switch (keycode) {
		case Input.Keys.Z:
			this.cameraGame.isMonter = false;
			break;
		case Input.Keys.S:
			this.cameraGame.isDescendre = false;
			break;
		case Input.Keys.D:
			this.cameraGame.isDroite = false;
			break;
		case Input.Keys.Q:
			this.cameraGame.isGauche = false;
			break;
		default:
			break;
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		final Vector3 point = new Vector3();
		point.set(screenX, screenY, 0);
		this.cameraGame.camera.unproject(point);

		QueryCallback callback = new QueryCallback() {
			@Override
			public boolean reportFixture(Fixture fixture) {
				if (fixture.testPoint(point.x, point.y)) {
					bodyThatWasHit = fixture.getBody();
					return false;
				} else {
					return true;
				}

			}
		};

		this.mondeDebug.world.QueryAABB(callback, point.x - 0.1f, point.y - 0.1f, point.x + 0.1f, point.y + 0.1f);
		if (bodyThatWasHit != null) {
			System.err.println("aaaaaaaaa");
			bodyThatWasHit = null;
		}
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
		this.cameraGame.zoom(amount);

		return false;
	}

}
