package com.ultraime.composant;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.ultraime.game.utile.Image;
import com.ultraime.game.utile.Parametre;
import com.ultraime.game.utile.VariableCommune;

public class Bouton {
	public static final int CLASSIQUE = 1;
	public static final int DEPLACEMENT = 2;
	public static final int CONSTRUCTION = 3;
	public static final int ROND_HD_MENU_116PX = 4;
	public static final int ROND_HD_MENU_30PX = 5;

	public static final int RECT_3_PART_1 = 6;
	public static final int RECT_3_PART_2 = 7;
	public static final int RECT_3_PART_3 = 8;
	// pas utile ?
	public static final int RECT_3_PART_1_S = 9;
	public static final int RECT_3_PART_2_S = 10;
	public static final int RECT_3_PART_3_S = 11;

	public static final int CARRE_50PX = 12;

	/**
	 * Taille en PX
	 */
	public static final int TAILLE_SELECT = 4;
	// liste des états
	private static final int RIEN = 0;
	private static final int CLIQUE = 1;
	private static final int OVER = 2;
	private int etat;

	private int typeBouton;
	private Rectangle rectangleBoutton;

	private Sprite spriteNormal;
	private Sprite spriteOver;
	private Sprite spriteClique;
	private Sprite[] spriteSelect;
	private Sprite spriteLogo;
	private boolean isSelect = false;

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
	 */
	public Bouton(final float x, final float y, final float largeur, final float hauteur) {
		this(x, y, largeur, hauteur, "");
	}

	/**
	 * @param texture
	 * @param x
	 * @param y
	 * @param largeur
	 * @param hauteur
	 * @param label
	 */
	public Bouton(final float x, final float y, final float largeur, final float hauteur, final String label) {
		this(x, y, largeur, hauteur, label, CLASSIQUE);
	}

	/**
	 * @param texture
	 * @param x
	 * @param y
	 * @param largeur
	 * @param hauteur
	 * @param label
	 * @param typeBouton
	 */
	public Bouton(final float x, final float y, final float largeur, final float hauteur, final String label,
			final int typeBouton) {
		this.x = x;
		this.y = y;
		this.largeur = largeur;
		this.hauteur = hauteur;

		this.rectangleBoutton = new Rectangle(x, y, largeur, hauteur);

		this.label = label;

		this.bitmapFont = new BitmapFont(Gdx.files.internal(Parametre.FONT), false);
		initSpriteSelection();
		modifierSprite(typeBouton);

	}

	private void initSpriteSelection() {
		Pixmap pixmap = new Pixmap((int) this.largeur, TAILLE_SELECT, Pixmap.Format.RGBA8888);
		pixmap.setColor(Color.YELLOW);
		pixmap.fillRectangle(0, 0, (int) this.largeur, TAILLE_SELECT);
		final Texture textureLargeur = new Texture(pixmap);

		pixmap = new Pixmap(TAILLE_SELECT, (int) this.hauteur, Pixmap.Format.RGBA8888);
		pixmap.setColor(Color.YELLOW);
		pixmap.fillRectangle(0, 0, TAILLE_SELECT, (int) this.hauteur);
		final Texture textureHauteur = new Texture(pixmap);
		pixmap.dispose();

		this.spriteSelect = new Sprite[4];
		this.spriteSelect[0] = new Sprite(textureLargeur);
		this.spriteSelect[1] = new Sprite(textureHauteur);
		this.spriteSelect[2] = new Sprite(textureLargeur);
		this.spriteSelect[3] = new Sprite(textureHauteur);

		this.spriteSelect[0].setPosition(this.x, this.y);
		this.spriteSelect[1].setPosition(this.x + this.largeur - TAILLE_SELECT, this.y);
		this.spriteSelect[2].setPosition(this.x, this.y + this.hauteur - TAILLE_SELECT);
		this.spriteSelect[3].setPosition(this.x, this.y);

	}

	public void setPosition(final float x, final float y) {
		this.rectangleBoutton.setPosition(x, y);
		this.spriteNormal.setPosition(this.x, this.y);
		this.spriteClique.setPosition(this.x, this.y);
		this.spriteOver.setPosition(this.x, this.y);
		this.x = x;
		this.y = y;
		this.spriteSelect[0].setPosition(this.x, this.y);
		this.spriteSelect[1].setPosition(this.x + this.largeur - 2f, this.y);
		this.spriteSelect[2].setPosition(this.x, this.y + this.hauteur - 2f);
		this.spriteSelect[3].setPosition(this.x + this.largeur - 2f, this.y + this.hauteur - 2f);
	}

	public void modifierSprite(final int typeBouton) {
		this.typeBouton = typeBouton;
		final Texture txtNormal = Image.getBtnNormal(this.typeBouton);
		this.spriteNormal = new Sprite(txtNormal);
		this.spriteNormal.setPosition(this.x, this.y);
		this.spriteNormal.setSize(this.largeur, this.hauteur);

		final Texture txtClique = Image.getBtnClique(this.typeBouton);
		this.spriteClique = new Sprite(txtClique);
		this.spriteClique.setPosition(this.x, this.y);
		this.spriteClique.setSize(this.largeur, this.hauteur);

		final Texture txtOver = Image.getBtnOver(this.typeBouton);
		this.spriteOver = new Sprite(txtOver);
		this.spriteOver.setPosition(this.x, this.y);
		this.spriteOver.setSize(this.largeur, this.hauteur);

	}

	public void setScale(final float scaleXY) {
		this.spriteClique.setScale(scaleXY);
		this.spriteOver.setScale(scaleXY);
		this.spriteNormal.setScale(scaleXY);
	}

	public void ajouterSpriteLogo(final Texture texture, final float x, final float y) {
		this.spriteLogo = new Sprite(texture);
		this.spriteLogo.setPosition(x, y);
	}

	public void render(final SpriteBatch batch) {
		if (this.etat == CLIQUE) {
			this.spriteClique.draw(batch);
		} else if (this.etat == OVER) {
			this.spriteOver.draw(batch);
		} else {
			this.spriteNormal.draw(batch);
		}
		if (isSelect) {
			for (int i = 0; i < this.spriteSelect.length; i++) {
				this.spriteSelect[i].draw(batch);
			}
		}
		if (this.spriteLogo != null) {
			this.spriteLogo.draw(batch);
		}
		this.bitmapFont.getData().setScale(0.6f);
		this.bitmapFont.draw(batch, this.label, this.x, this.y + this.hauteur / 2 + Parametre.y(10));
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bitmapFont == null) ? 0 : bitmapFont.hashCode());
		result = prime * result + etat;
		result = prime * result + Float.floatToIntBits(hauteur);
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		result = prime * result + Float.floatToIntBits(largeur);
		result = prime * result + ((rectangleBoutton == null) ? 0 : rectangleBoutton.hashCode());
		result = prime * result + ((spriteClique == null) ? 0 : spriteClique.hashCode());
		result = prime * result + ((spriteNormal == null) ? 0 : spriteNormal.hashCode());
		result = prime * result + ((spriteOver == null) ? 0 : spriteOver.hashCode());
		result = prime * result + typeBouton;
		result = prime * result + Float.floatToIntBits(x);
		result = prime * result + Float.floatToIntBits(y);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Bouton other = (Bouton) obj;
		if (bitmapFont == null) {
			if (other.bitmapFont != null)
				return false;
		} else if (!bitmapFont.equals(other.bitmapFont))
			return false;
		if (etat != other.etat)
			return false;
		if (Float.floatToIntBits(hauteur) != Float.floatToIntBits(other.hauteur))
			return false;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		if (Float.floatToIntBits(largeur) != Float.floatToIntBits(other.largeur))
			return false;
		if (rectangleBoutton == null) {
			if (other.rectangleBoutton != null)
				return false;
		} else if (!rectangleBoutton.equals(other.rectangleBoutton))
			return false;
		if (spriteClique == null) {
			if (other.spriteClique != null)
				return false;
		} else if (!spriteClique.equals(other.spriteClique))
			return false;
		if (spriteNormal == null) {
			if (other.spriteNormal != null)
				return false;
		} else if (!spriteNormal.equals(other.spriteNormal))
			return false;
		if (spriteOver == null) {
			if (other.spriteOver != null)
				return false;
		} else if (!spriteOver.equals(other.spriteOver))
			return false;
		if (typeBouton != other.typeBouton)
			return false;
		if (Float.floatToIntBits(x) != Float.floatToIntBits(other.x))
			return false;
		if (Float.floatToIntBits(y) != Float.floatToIntBits(other.y))
			return false;
		return true;
	}

	public boolean isSelect() {
		return isSelect;
	}

	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

}
