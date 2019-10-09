package com.ultraime.game.metier;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.ultraime.database.base.Base;
import com.ultraime.database.entite.ElementEarth;
import com.ultraime.game.entite.EntiteJoueur;
import com.ultraime.game.entite.EntiteStatic;
import com.ultraime.game.entite.EntiteVivante;
import com.ultraime.game.utile.Parametre;

public class WorldService {

	// Le vrai monde, avec les stats normal (petit nombre)
	public World world;
	public ArrayList<Body> bodiesEntiteVivant = new ArrayList<Body>();

	// World utilisé surtout pour le debug
	public World worldAffichage;
	public ArrayList<Body> bodiesAffichageEntiteVivant = new ArrayList<Body>();

	public Box2DDebugRenderer debugRenderer;

	private SpriteBatch batch;

	/**
	 * = 64
	 */
	public static int MULTIPLICATEUR = 64;

	private Body bodyTouche;

	private int tempsAnimation = 0;

	/** Instance unique pré-initialisée */
	private static WorldService INSTANCE = new WorldService();

	/** Point d'accès pour l'instance unique du singleton */
	public static WorldService getInstance() {
		return INSTANCE;
	}

	private WorldService() {
		world = new World(new Vector2(0, 0), true);
		worldAffichage = new World(new Vector2(0, 0), true);
		this.debugRenderer = new Box2DDebugRenderer();
		this.batch = new SpriteBatch();

	}

	public void render() {
		if (!Parametre.PAUSE) {
			world.step(1 / 60f, 6, 2);
			worldAffichage.step(1 / 60f, 6, 2);
		}
		tempsAnimation += Gdx.graphics.getDeltaTime();
		this.batch.begin();
		gestionBodies();
		// List<ElementEarth> elementEarths =
		// Base.getInstance().getElementEarthPlantes();
		List<ElementEarth> elementEarths = Base.getInstance().getListEarth(ElementEarth.culture);
		for (int i = 0; i < elementEarths.size(); i++) {
			elementEarths.get(i).render(batch, tempsAnimation);
		}

		this.batch.end();
		// deplacementBodies();
	}

	public Body recupererBodyFromEntite(final EntiteVivante entiteVivante) {
		Body retourBody = null;
		ArrayList<Body> bodies = WorldService.getInstance().bodiesEntiteVivant;
		for (int i = 0; i < bodies.size(); i++) {
			final Body body = bodies.get(i);
			if (body.getUserData() instanceof EntiteVivante) {
				final EntiteVivante ev = (EntiteVivante) body.getUserData();
				if (ev == entiteVivante) {
					retourBody = body;
					break;
				}
			}
		}
		return retourBody;
	}

	private void gestionBodies() {

		ArrayList<Body> bodies = WorldService.getInstance().bodiesEntiteVivant;

		for (int i = 0; i < bodies.size(); i++) {
			final Body body = bodies.get(i);
			// if (!Parametre.PAUSE) {
			// if (body.getUserData() instanceof EntiteVivante) {
			// final EntiteVivante ev = (EntiteVivante) body.getUserData();
			// ev.doAction(body, world, worldAffichage);
			// }
			// }
			if (body.getUserData() instanceof EntiteJoueur) {
				final EntiteJoueur entiteJoueur = (EntiteJoueur) body.getUserData();
				// entiteJoueur.doMetier();
				entiteJoueur.doAction(body, world, worldAffichage);
				entiteJoueur.render(batch);
			}

		}

	}

	/**
	 * @param OrthographicCamera
	 *            camera
	 */
	public void renderDebug(final OrthographicCamera camera) {
		if (Parametre.MODE_DEBUG) {
			try {
				this.debugRenderer.render(world, camera.combined);
				this.debugRenderer.render(worldAffichage, camera.combined);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}
		}
	}

	public void creerCollision(final int x, final int y) {
		EntiteStatic entiteStatic = new EntiteStatic(x, y, 1, 1);
		Base.getInstance().creerRectangleStatic(this.world, this.worldAffichage, x, y, 1, 1, entiteStatic);
	}

	public void retirerCollision(int posX, int posY) {
		Base.getInstance().retirerRectangleStatic(this.world, this.worldAffichage, posX, posY);
	}

	/**
	 * @param TiledMapTileLayer
	 *            layer
	 */
	public void initialiserCollision(final TiledMapTileLayer layer) {
		for (int x = 0; x < layer.getWidth(); x++) {
			for (int y = 0; y < layer.getHeight(); y++) {
				final Cell cell = layer.getCell(x, y);
				if (cell != null && cell.getTile() != null) {
					final int id = cell.getTile().getId();
					if (TileMurManager.isMurEnBois(id)) {
						EntiteStatic entiteStatic = new EntiteStatic(x, y, 1, 1);
						Base.getInstance().creerRectangleStatic(this.world, this.worldAffichage, x, y, 1, 1,
								entiteStatic);
						ElementEarth elementEarth = new ElementEarth(
								Base.getInstance().recupererElementEarthByNom("mur_en_bois"));
						elementEarth.x = x;
						elementEarth.y = y;
						Base.getInstance().ajouterElementEarth(elementEarth);
					}
				}
			}
		}
	}

	/**
	 * @param screenX
	 * @param screenY
	 * @param pointer
	 * @param button
	 * @param OrthographicCamera
	 * @return
	 */
	public Body touchDown(final int screenX, final int screenY, final int pointer, final int button,
			final OrthographicCamera camera) {
		Body bodyToucheAretourner = null;
		final Vector3 point = new Vector3();
		point.set(screenX, screenY, 0);
		camera.unproject(point);

		QueryCallback callback = new QueryCallback() {
			@Override
			public boolean reportFixture(Fixture fixture) {
				if (fixture.testPoint(point.x, point.y)) {
					bodyTouche = fixture.getBody();
					return false;
				} else {
					return true;
				}

			}
		};
		worldAffichage.QueryAABB(callback, point.x - 0.1f, point.y - 0.1f, point.x + 0.1f, point.y + 0.1f);
		if (bodyTouche != null) {
			bodyToucheAretourner = bodyTouche;
			bodyTouche = null;
		}
		return bodyToucheAretourner;
	}

	public void updateCamera(OrthographicCamera camera) {
		batch.setProjectionMatrix(camera.combined);

	}

}
