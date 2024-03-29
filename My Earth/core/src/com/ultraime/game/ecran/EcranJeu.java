package com.ultraime.game.ecran;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.ultraime.composant.ActionEntiteVivantComposant;
import com.ultraime.composant.ComposantManager;
import com.ultraime.composant.HudComposant;
import com.ultraime.database.Action;
import com.ultraime.database.Base;
import com.ultraime.database.ElementEarth;
import com.ultraime.database.LecteurXML;
import com.ultraime.database.Ordre;
import com.ultraime.game.metier.Lumiere;
import com.ultraime.game.metier.Temps;
import com.ultraime.game.metier.TileMapService;
import com.ultraime.game.metier.TiledMapClickListener;
import com.ultraime.game.metier.TiledMapStage;
import com.ultraime.game.metier.WorldService;
import com.ultraime.game.utile.Parametre;

public class EcranJeu extends Ecran {

	// Pour la Police d'écriture
	private BitmapFont font;
	private BitmapFont bitmapFont;

	// Pour la carte
	private TileMapService tileMapService;
	private CameraGame cameraGame;
	private Stage stage;

	// Pour le monde
	private WorldService worldService;

	// les composants
	private ComposantManager actionEntiteVivantComposant;
	private ComposantManager hudComposant;

	// la lumiere
	private Lumiere lumiere;

	@Override
	public void changerEcran(InputMultiplexer inputMultiplexer) {
		inputMultiplexer.addProcessor(this);
		inputMultiplexer.addProcessor(stage);
	}

	@Override
	public void create(EcranManager ecranManager) {
		LecteurXML.getInstance().traiterToutLesFichiers();
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
		this.tileMapService = TileMapService.getInstance();
		Vector2 posDepart = this.tileMapService.recupererPositionDepart();
		this.cameraGame.camera.position.x = posDepart.x;
		this.cameraGame.camera.position.y = posDepart.y;

		// Pour le monde
		this.worldService = WorldService.getInstance();

		final float posx = this.cameraGame.camera.position.x / WorldService.MULTIPLICATEUR;
		final float posy = this.cameraGame.camera.position.y / WorldService.MULTIPLICATEUR;

		this.worldService.initialiserCollision(this.tileMapService.getLayers("OBJET_0"));
		this.worldService.initialiserEntiteTestConstructeur(posx, posy, null);
		this.worldService.initialiserEntiteTestAgriculteur(posx + 1, posy, null);
		// this.worldService.initialiserEntite(posx + 1, posy);
		this.stage = new TiledMapStage(tileMapService.tiledMap);

		// les composants
		this.actionEntiteVivantComposant = new ComposantManager(new ActionEntiteVivantComposant());
		this.hudComposant = new ComposantManager(new HudComposant());

		// la lumiere
		lumiere = Lumiere.getInstance();

		// création du temps.
		Temps temps = new Temps(3000, 0, 1, 1, 12, 00);
		Base.getInstance().setTemps(temps);
		Base.getInstance().startTempsThread();

	}

	// création de l'IHM

	@Override
	public void render() {
		updateCamera();
		stage.act();

		this.batch.begin();

		this.tileMapService.render();

		this.worldService.render();

		if (Parametre.MODE_DEBUG) {
			try {
				this.worldService.renderDebug(this.cameraGame.camera);
			} catch (GdxRuntimeException e) {
				if (Parametre.MODE_DEBUG) {
					e.printStackTrace();
				}
			}
		}
		if (Parametre.ACTIVER_LUMIERE) {
			this.lumiere.renderLumiere(this.cameraGame.camera);
		}
		this.actionEntiteVivantComposant.render();

		this.hudComposant.render();
		this.batch.end();

	}

	private void updateCamera() {
		this.cameraGame.updateCamera();
		stage.getViewport().setCamera(this.cameraGame.camera);
		OrthographicCamera camera = this.cameraGame.camera;
		batch.setProjectionMatrix(camera.combined);
		this.worldService.updateCamera(camera);
		this.tileMapService.updateCamera(camera);
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

	/**
	 * @param screenX
	 * @param screenY
	 * @param pointer
	 * @param button
	 *            0 = clique gauche. 1 = clique droit
	 * @return
	 */
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		this.hudComposant.isClique(screenX, screenY);
		if (Action.ORDRE == null && Action.body == null) {
			// on regarde si l'on clique sur un body.
			Action.body = this.worldService.touchDown(screenX, screenY, pointer, button, this.cameraGame.camera);
		} else if (Action.body == null || (Action.ORDRE != null && Action.ORDRE.id == Ordre.construire)) {
			// si on a pas selectionné de body.
			manageActionClique(button);
		} else {
			// si on a un body de selectionné.
			// Etape 1: on regarde si l'on clique sur la barre d'action.
			boolean isClick = false;
			isClick = this.actionEntiteVivantComposant.isClique(screenX, screenY);
			if (!isClick) {
				// si l'on ne clique pas sur la barre d'action du perso, on doit
				// vérifier que l'on ai pas en train de faire une action
				if (!Action.entiteFaitAction()) {
					// si on est pas en action. On retire le body de la
					// selection.
					Action.removeBody();
				} else if (button == 1) {
					// si l'on fait un clique droit alors que l'on fait une
					// action, on l'enregistre au perso.
					Action.ajouterAction();
				}
			} else {
				// si on clique sur la barre d'action, on doit bloquer les
				// cliques sur la MAP.
				TiledMapClickListener.IGNORE_CLICK = true;
			}
		}

		return false;

	}

	private void manageActionClique(int button) {
		if (Action.ORDRE != null && Action.ORDRE.id == Ordre.construire && button == 1) {
			if ((!Action.ORDRE.elementEarth.placementType.equals(ElementEarth.unique) && Action.vectorDepart == null)
					|| Action.ORDRE.elementEarth.placementType.equals(ElementEarth.unique)) {
				Action.ordreReset();
				this.hudComposant.resetSelector();
			} else {
				Action.vectorDepart = null;
				Action.viderVectorConstruction();
			}
		}
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		this.actionEntiteVivantComposant.touchUP(screenX, screenY);
		this.hudComposant.touchUP(screenX, screenY);
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {

		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// on regarde les ordres.
		manageActionMouseMoved(screenX, screenY);

		this.actionEntiteVivantComposant.isOver(screenX, screenY);
		this.hudComposant.isOver(screenX, screenY);

		return false;
	}

	private void manageActionMouseMoved(int screenX, int screenY) {
		final Vector3 point = new Vector3();
		point.set(screenX, screenY, 0);
		this.cameraGame.getCamera().unproject(point);
		Action.ordreConstructionElementNEW(null, point.x, point.y);

	}

	@Override
	public boolean scrolled(int amount) {
		this.cameraGame.zoom(amount);

		return false;
	}

}
