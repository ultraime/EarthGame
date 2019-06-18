package com.ultraime.game.metier;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class WorldBodyService {

	// private BodyDef groundBodyDef;
	// public float largeur;
	// public float hauteur;

	public WorldBodyService() {
	}

	/**
	 * @param world
	 * @param x
	 * @param y
	 * @param largeur
	 * @param hauteur
	 */
	public static void creerRectangleStatic(final World world, final float x, final float y, final float largeur,
			final float hauteur, final Object objet) {
		BodyDef groundBodyDef = new BodyDef();
		groundBodyDef.position.set(new Vector2(x, y));
		Body groundBody = world.createBody(groundBodyDef);
		PolygonShape groundBox = new PolygonShape();
		groundBox.setAsBox(largeur / 2, hauteur / 2);
		groundBody.createFixture(groundBox, 0.0f);
		groundBody.setUserData(objet);

	}
	

	public static void creerCercleVivant(World world, float diametre, float posX, float posY, final Object objet) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody;
		bodyDef.position.set(posX, posY);

		Body body = world.createBody(bodyDef);

		CircleShape circle = new CircleShape();
		circle.setRadius(diametre);
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circle;
		fixtureDef.density = 0.5f;
		fixtureDef.friction = 0.5f;
		fixtureDef.restitution = 0.5f;
		fixtureDef.filter.groupIndex = -2;//pour entite a ne pas etre en colli
		body.createFixture(fixtureDef);
		body.setUserData(objet);
	}


}
