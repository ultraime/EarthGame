package com.ultraime.game.metier;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

import box2dLight.ConeLight;
import box2dLight.RayHandler;

public class Lumiere {
	private World world;
	public RayHandler rayHandler;
	private ConeLight coneLight;
	private float lvlDeLaLumiere = 1f;

	/** Instance unique pré-initialisée */
	private static Lumiere INSTANCE;

	/** Point d'accès pour l'instance unique du singleton */
	public static Lumiere getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new Lumiere();
		}
		return INSTANCE;
	}

	private Lumiere() {
		// creation des éléments
		world = new World(new Vector2(), false);
		rayHandler = new RayHandler(world);
		rayHandler.setAmbientLight(1f);

		coneLight = new ConeLight(rayHandler, 1000, new Color(0x540000FF), 1000, 0, 0, 90, 35);
		coneLight.setActive(false);
	}

	/**
	 * @param OrthographicCamera
	 *            camera
	 */
	@SuppressWarnings("deprecation")
	public void renderLumiere(OrthographicCamera camera) {
		rayHandler.setCombinedMatrix(camera.combined);

		rayHandler.updateAndRender();

	}

	public void diminuerLumiere() {
		lvlDeLaLumiere = lvlDeLaLumiere - 0.003f;
		if (lvlDeLaLumiere < 0.01f) {
			lvlDeLaLumiere = 0.01f;
		}
		rayHandler.setAmbientLight(lvlDeLaLumiere);
	}

	public void augmenterLumiere() {
		lvlDeLaLumiere = lvlDeLaLumiere + 0.0035f;
		if (lvlDeLaLumiere > 1f) {
			lvlDeLaLumiere = 1f;
		}
		rayHandler.setAmbientLight(lvlDeLaLumiere);
	}

	public RayHandler getRayHandler() {
		return rayHandler;
	}

	public void setRayHandler(RayHandler rayHandler) {
		this.rayHandler = rayHandler;
	}
}
