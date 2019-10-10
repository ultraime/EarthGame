package com.ultraime.game.ecran;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.ultraime.game.utile.Parametre;

public class CameraGame {
	public OrthographicCamera camera;
	public boolean isMonter = false;
	public boolean isDescendre = false;
	public boolean isGauche = false;
	public boolean isDroite = false;
	public static float CAMERA_ZOOM = 1;

	public CameraGame() {
		this.camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	}

	public void updateCamera() {
		camera.update();

		if (isMonter) {
			if (this.camera.position.y < (Parametre.MONDE_Y * 64) - Parametre.CAMERA_MAX_HAUT * CAMERA_ZOOM) {
				this.camera.position.y += 10 * CAMERA_ZOOM;
			}
		}
		if (isDescendre) {
			// Decalage camera petit ecrnan : 375
			if (this.camera.position.y > Parametre.CAMERA_MAX_BAS * CAMERA_ZOOM) {
				this.camera.position.y -= 10 * CAMERA_ZOOM;
			}
		}
		if (isGauche) {
			if (this.camera.position.x > Parametre.CAMERA_MAX_GAUCHE * CAMERA_ZOOM) {
				this.camera.position.x -= 10 * CAMERA_ZOOM;
			}
		}
		if (isDroite) {
			if (this.camera.position.x < (Parametre.MONDE_X * 64) - Parametre.CAMERA_MAX_DROITE * CAMERA_ZOOM) {
				this.camera.position.x += 10 * CAMERA_ZOOM;
			}
		}

		// camera.position.y += (int) ((camera.position.x * TILE_SIZE -
		// mCam.position.y) / 2 + TILE_SIZE / 2) / 8;
		camera.position.set(Math.round(camera.position.x), Math.round(camera.position.y), 1);
	}

	public OrthographicCamera getCamera() {
		return camera;
	}

	public void setCamera(OrthographicCamera camera) {
		this.camera = camera;
	}

	public void zoom(int amount) {
		float testValidite = 0;
		if (amount < 0) {
			testValidite = CAMERA_ZOOM - 0.2f;
			if (testValidite >= 0.2f) {// 0.6f
				CAMERA_ZOOM = CAMERA_ZOOM - 0.2f;
			}
		} else {
			testValidite = CAMERA_ZOOM + 0.2f;
			if (testValidite < 5f) {
				CAMERA_ZOOM = CAMERA_ZOOM + 0.2f;
			}
		}
		camera.zoom = MathUtils.round(10f * CAMERA_ZOOM) / 10f;

	}

}
