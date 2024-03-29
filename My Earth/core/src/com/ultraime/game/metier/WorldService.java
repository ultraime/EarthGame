package com.ultraime.game.metier;

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
import com.badlogic.gdx.utils.Array;
import com.ultraime.database.Base;
import com.ultraime.database.ElementEarth;
import com.ultraime.game.entite.EntiteJoueur;
import com.ultraime.game.entite.EntiteStatic;
import com.ultraime.game.entite.EntiteVivante;
import com.ultraime.game.entite.EntiteVivante.TypeEntiteVivante;
import com.ultraime.game.metier.travail.Metier;
import com.ultraime.game.metier.travail.MetierAgriculteur;
import com.ultraime.game.metier.travail.MetierConstructeur;
import com.ultraime.game.utile.Parametre;

public class WorldService {

	// Le vrai monde, avec les stats normal (petit nombre)
	public World world;

	// World utilisé surtout pour le debug
	public World worldAffichage;

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
	//	List<ElementEarth> elementEarths = Base.getInstance().getElementEarthPlantes();
		List<ElementEarth> elementEarths = Base.getInstance().getListEarth(ElementEarth.culture);
		for (int i = 0; i < elementEarths.size(); i++) {
			elementEarths.get(i).render(batch,tempsAnimation);
		}

		this.batch.end();
		// deplacementBodies();
	}

	private void gestionBodies() {
		Array<Body> bodies = new Array<Body>();
		this.world.getBodies(bodies);
		for (final Body body : bodies) {
			if (!Parametre.PAUSE) {
				if (body.getUserData() instanceof EntiteVivante) {
					final EntiteVivante ev = (EntiteVivante) body.getUserData();
					ev.doAction(body, world, worldAffichage);
				}
			}
			if (body.getUserData() instanceof EntiteJoueur) {
				final EntiteJoueur entiteJoueur = (EntiteJoueur) body.getUserData();
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
			this.debugRenderer.render(world, camera.combined);
			this.debugRenderer.render(worldAffichage, camera.combined);
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
					if (TileMapService.isMurEnBois(id)) {
						EntiteStatic entiteStatic = new EntiteStatic(x, y, 1, 1);
						Base.getInstance().creerRectangleStatic(this.world, this.worldAffichage, x, y, 1, 1,
								entiteStatic);
						ElementEarth elementEarth = new ElementEarth(Base.getInstance().recupererElementEarthByNom("mur_en_bois"));
						elementEarth.x = x;
						elementEarth.y = y;
						Base.getInstance().ajouterElementEarth(elementEarth);
					}
				}
			}
		}
	}

	/**
	 * @param posx
	 * @param posy
	 */
	public void initialiserEntite(final float posx, final float posy) {
		final float radius = 0.4f;
		final EntiteJoueur entiteVivante = new EntiteJoueur(posx, posy, radius, TypeEntiteVivante.PERSONNAGE);
		Base.getInstance().creerCercleVivant(this.world, worldAffichage, radius, posx, posy, entiteVivante);
	}

	/**
	 * @param posx
	 * @param posy
	 * @param metier
	 */
	public void initialiserEntiteTestConstructeur(final float posx, final float posy, Metier metier) {
		final float radius = 0.4f;
		final EntiteJoueur entiteVivante = new EntiteJoueur(posx, posy, radius, TypeEntiteVivante.PERSONNAGE);
		entiteVivante.ajouterMetier(new MetierConstructeur(entiteVivante));
		Base.getInstance().creerCercleVivant(this.world, worldAffichage, radius, posx, posy, entiteVivante);
	}
	/**
	 * @param posx
	 * @param posy
	 * @param metier
	 */
	public void initialiserEntiteTestAgriculteur(final float posx, final float posy, Metier metier) {
		final float radius = 0.4f;
		final EntiteJoueur entiteVivante = new EntiteJoueur(posx, posy, radius, TypeEntiteVivante.PERSONNAGE);
		entiteVivante.ajouterMetier(new MetierAgriculteur(entiteVivante));
		Base.getInstance().creerCercleVivant(this.world, worldAffichage, radius, posx, posy, entiteVivante);
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
