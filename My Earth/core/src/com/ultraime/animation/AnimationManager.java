package com.ultraime.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.ultraime.game.entite.EntiteVivante.TypeEntiteVivante;
import com.ultraime.game.utile.Image;

public class AnimationManager {
	// Animation spécifique
	protected transient Animation animation[];
	protected transient TextureRegion regionCourante;
	protected transient float largeur;
	protected transient float hauteur;
	protected transient float vitesseAnimation;
	protected transient Texture texture;
	// pour que les plantes ne bouge pas en même temps
	private float tempsAnimation = 0;

	/**
	 * @param texture
	 * @param largeur
	 * @param hauteur
	 * @param vitesseAnimation
	 */
	public AnimationManager(Texture texture, int largeur, int hauteur, float vitesseAnimation) {
		creerAnimation(texture, largeur, hauteur, vitesseAnimation);
	}

	/**
	 * @param typeEntiteVivante
	 * @param typeUnite
	 */
	public AnimationManager(final TypeEntiteVivante typeEntiteVivante, final int typeUnite) {
		Texture txt = Image.getImage(typeUnite);
		float largeur = 0;
		float hauteur = 0;
		float vitesseAnimation = 0;
		if (typeEntiteVivante == TypeEntiteVivante.PERSONNAGE) {
			largeur = 64;
			hauteur = 128;
			vitesseAnimation = 0.500f;

		}
		creerAnimation(txt, largeur, hauteur, vitesseAnimation);
	}

	public AnimationManager(final float largeur, final float hauteur, final float vitesseAnimation, String lienImage) {
		Texture txt = new Texture(Gdx.files.internal(lienImage));
		this.texture = txt;
		creerAnimation(txt, largeur, hauteur, vitesseAnimation);
	}

	public AnimationManager(AnimationManager animationManager) {
		this.largeur = animationManager.largeur;
		this.hauteur = animationManager.hauteur;
		this.vitesseAnimation = animationManager.vitesseAnimation;
		this.texture = animationManager.texture;
		creerAnimation(animationManager.texture, largeur, hauteur, vitesseAnimation);
	}

	/**
	 * @param texture
	 * @param largeur
	 * @param hauteur
	 * @param vitesseAnimation
	 */
	public void creerAnimation(Texture texture, float largeur, float hauteur, float vitesseAnimation) {
		int largeur_texture = texture.getWidth();
		int hauteur_texture = texture.getHeight();
		this.largeur = largeur;
		this.hauteur = hauteur;
		this.vitesseAnimation = vitesseAnimation;
		int nbLargeurImage = getNombreImage(largeur_texture, this.largeur);
		int nbHauteurImage = getNombreImage(hauteur_texture, this.hauteur);
		TextureRegion[][] tmp = TextureRegion.split(texture, largeur_texture / nbLargeurImage,
				hauteur_texture / nbHauteurImage);
		this.animation = new Animation[nbHauteurImage];
		for (int i = 0; i < nbHauteurImage; i++) {
			this.animation[i] = new Animation(vitesseAnimation, tmp[i]);
		}
		this.regionCourante = this.animation[0].getKeyFrame(0, true);
	}

	/**
	 * @param batch
	 * @param x
	 * @param y
	 * @param nbLigne
	 */
	public void render(final SpriteBatch batch, final float x, final float y, final int nbLigne) {
		this.tempsAnimation += Gdx.graphics.getDeltaTime();
		// if (this.tempsAnimation > 1000) {
		// this.tempsAnimation = 1;
		// }
		this.regionCourante = this.animation[nbLigne].getKeyFrame(this.tempsAnimation, true);
		batch.draw(this.regionCourante, x, y, this.largeur, this.hauteur);
	}

	/**
	 * @param big_largeur
	 * @param small_largeur
	 * @return big_largeur/small_largeur
	 */
	private int getNombreImage(float big_largeur, float small_largeur) {
		int retour = (int) (big_largeur / small_largeur);
		return retour;
	}

}