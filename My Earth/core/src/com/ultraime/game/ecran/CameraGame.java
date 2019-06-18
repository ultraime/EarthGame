package com.ultraime.game.ecran;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;

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
			this.camera.position.y += 10*CAMERA_ZOOM;
//			camera.translate(0, 10);
		}
		if (isDescendre) {
			this.camera.position.y -= 10*CAMERA_ZOOM;
//			camera.translate(0, -10);
		}
		if (isGauche) {
			this.camera.position.x -= 10*CAMERA_ZOOM;
//			camera.translate(-10, 0);
		}
		if (isDroite) {
			this.camera.position.x += 10*CAMERA_ZOOM;
//			camera.translate(10, 0);
		}
		
	
//		camera.position.y += (int) ((camera.position.x * TILE_SIZE - mCam.position.y) / 2 + TILE_SIZE / 2) / 8;
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
			if (testValidite < 100f) {
				CAMERA_ZOOM = CAMERA_ZOOM + 0.2f;
			}
		}
		camera.zoom = MathUtils.round (10f * CAMERA_ZOOM) / 10f;


	}


}
