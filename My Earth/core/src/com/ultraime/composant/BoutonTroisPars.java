package com.ultraime.composant;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.ultraime.game.utile.Image;
import com.ultraime.game.utile.VariableCommune;

public class BoutonTroisPars {
	// liste des états
	private static final int RIEN = 0;
	private static final int CLIQUE = 1;
	private static final int OVER = 2;
	private int etat;

	public static final int TAILLE_BORDURE = 8;

	private Rectangle rectangleBoutton;

	private Sprite[] spriteNormal;
	private Sprite[] spriteOver;
	private Sprite[] spriteClique;

	// pour afficher le label
	private BitmapFont bitmapFont;
	private String label;

	private float x;
	private float y;
	private float hauteur;
	private float largeur;

	/**
	 * @param texture
	 * @param x
	 * @param y
	 * @param largeur
	 * @param hauteur
	 * @param label
	 * @param typeBouton
	 */
	public BoutonTroisPars(final float x, final float y, final float largeur, final float hauteur, final String label,
			final int typeBouton) {
		this.x = x;
		this.y = y;
		this.largeur = largeur;
		this.hauteur = hauteur;
		this.rectangleBoutton = new Rectangle(x, y, largeur, hauteur);

		initTableauSprite();
		modifierSprite(typeBouton);

		this.label = label;

		this.bitmapFont = new BitmapFont(Gdx.files.internal("fonts/OpenSans-Bold.fnt"), false);
	}

	/**
	 * @param x
	 * @param y
	 * @param largeur
	 * @param hauteur
	 * @param typeBouton
	 */
	public BoutonTroisPars(final float x, final float y, final float largeur, final float hauteur,
			final int typeBouton) {
		this(x, y, largeur, hauteur, "", typeBouton);
	}

	private void initTableauSprite() {
		this.spriteClique = new Sprite[3];
		this.spriteNormal = new Sprite[3];
		this.spriteOver = new Sprite[3];
	}

	public void modifierSprite(final int typeBouton) {
		float largeur = 0;
		final float hauteur = this.hauteur;

		float x = 0;
		final float y = this.y;
		for (int i = 0; i < 3; i++) {
			if (i == 0 || i == 2) {
				if (i == 0) {
					x = this.x;
				} else {
					x = this.x + this.largeur - TAILLE_BORDURE;
				}
				largeur = TAILLE_BORDURE;

			} else {
				largeur = this.largeur - TAILLE_BORDURE * 2;
				x = this.x + TAILLE_BORDURE;
			}

			final Texture txtNormal = Image.getBtnNormal(typeBouton + i);
			this.spriteNormal[i] = new Sprite(txtNormal);
			this.spriteNormal[i].setPosition(x, y);
			this.spriteNormal[i].setSize(largeur, hauteur);

			final Texture txtClique = Image.getBtnClique(typeBouton + i);
			this.spriteClique[i] = new Sprite(txtClique);
			this.spriteClique[i].setPosition(x, y);
			this.spriteClique[i].setSize(largeur, hauteur);

			final Texture txtOver = Image.getBtnOver(typeBouton + i);
			this.spriteOver[i] = new Sprite(txtOver);
			this.spriteOver[i].setPosition(x, y);
			this.spriteOver[i].setSize(largeur, hauteur);
		}

	}

	public void render(final SpriteBatch batch) {
		for (int i = 0; i < 3; i++) {
			if (this.etat == CLIQUE) {
				this.spriteClique[i].draw(batch);
			} else if (this.etat == OVER) {
				this.spriteOver[i].draw(batch);
			} else {
				this.spriteNormal[i].draw(batch);
			}
		}

		this.bitmapFont.getData().setScale(0.6f);
		this.bitmapFont.draw(batch, this.label, this.x + TAILLE_BORDURE + 2, this.y + this.hauteur / 2 + 8);

	}

	/**
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean isClique(final float x, float y) {
		boolean isClique = false;
		y = Gdx.graphics.getHeight() - y;
		final Rectangle rectangleClique = VariableCommune.rectangleClique;
		rectangleClique.setPosition(x, y);
		if (rectangleClique.overlaps(this.rectangleBoutton)) {
			this.etat = CLIQUE;
			isClique = true;
		} else {
			this.etat = RIEN;
		}
		return isClique;
	}

	public boolean isOver(final float x, float y) {
		boolean isOver = false;
		y = Gdx.graphics.getHeight() - y;
		final Rectangle rectangleClique = VariableCommune.rectangleClique;
		rectangleClique.setPosition(x, y);
		if (rectangleClique.overlaps(this.rectangleBoutton)) {
			this.etat = OVER;
			isOver = true;
		} else {
			this.etat = RIEN;
		}
		return isOver;
	}

	public void touchUP(int screenX, int screenY) {
		if (this.etat == CLIQUE) {
			this.etat = RIEN;
		}

	}

	public void dispose() {
		this.bitmapFont.dispose();
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}
