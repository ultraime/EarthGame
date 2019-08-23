package com.ultraime.composant;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ultraime.database.Action;
import com.ultraime.database.ElementEarth;
import com.ultraime.database.base.Base;
import com.ultraime.game.utile.Image;
import com.ultraime.game.utile.Parametre;
import com.ultraime.game.utile.VariableCommune;

public class HudComposant extends Composant {
	public static final int CONSTRUCTION_STRUCTURE = 0;
	public static final int CONSTRUCTION_CULTURE = 1;
	private static final int CONSTRUCTION_MEUBLE = 2;
	private static final int CONSTRUCTION_SOL = 3;
	private Sprite sprite;
	// private Sprite spriteTest;

	private Bouton boutonConstruction;
	private Bouton boutonCulture;
	private Bouton boutonInformation;

	private Bouton boutonVitesse_1;
	private Bouton boutonVitesse_2;
	private Bouton boutonVitesse_3;
	private Bouton boutonVitesse_stop;

	private boolean showConstructionList = false;

	private List<BoutonTroisPars> boutonTroisParsConstruction;

	private MenuAction menuAction;

	// pour afficher le temps
	private BitmapFont bitmapFont;
	private int posXTemps;
	private int posYTemps;

	public HudComposant() {
		this.batch = new SpriteBatch();

		final Texture textureFond = Image.getImage(VariableCommune.HUD_MENU_BAS_GAUCHE);
		this.sprite = new Sprite(textureFond);

		float posX = textureFond.getWidth() * 0.40f;
		float posY = textureFond.getHeight() * 0.40f;
		this.sprite.setSize(posX, posY);
		this.sprite.setPosition(Parametre.LARGEUR_ECRAN - posX, 10 + Parametre.DECALAGE_Y);
		// bouton
		// les gros boutons
		final Texture textureBtn116px = Image.getBtnNormal(Bouton.ROND_HD_MENU_116PX);

		posX = Parametre.LARGEUR_ECRAN - textureFond.getWidth() * 0.40f;
		posY = 20 + Parametre.DECALAGE_Y;
		boutonConstruction = new Bouton(posX, posY, textureBtn116px.getWidth(), textureBtn116px.getHeight(), "",
				Bouton.ROND_HD_MENU_116PX);

		posX = Parametre.LARGEUR_ECRAN - textureFond.getWidth() * 0.37f;
		posY = 140 + Parametre.DECALAGE_Y;
		boutonCulture = new Bouton(posX, posY, textureBtn116px.getWidth(), textureBtn116px.getHeight(), "",
				Bouton.ROND_HD_MENU_116PX);

		posX = Parametre.LARGEUR_ECRAN - textureFond.getWidth() * 0.31f;
		posY = 250 + Parametre.DECALAGE_Y;
		boutonInformation = new Bouton(posX, posY, textureBtn116px.getWidth(), textureBtn116px.getHeight(), "",
				Bouton.ROND_HD_MENU_116PX);

		// les petits boutons
		final Texture textureBtn32px = Image.getBtnNormal(Bouton.ROND_HD_MENU_30PX);
		posX = Parametre.LARGEUR_ECRAN - textureFond.getWidth() * 0.17f;
		posY = 330 + Parametre.DECALAGE_Y;
		boutonVitesse_1 = new Bouton(posX, posY, textureBtn32px.getWidth(), textureBtn32px.getHeight(), "",
				Bouton.ROND_HD_MENU_30PX);
		boutonVitesse_1.ajouterSpriteLogo(Image.getImage(VariableCommune.LOGO_VITESSE1), posX, posY);

		posX = Parametre.LARGEUR_ECRAN - textureFond.getWidth() * 0.135f;
		posY = 345 + Parametre.DECALAGE_Y;
		boutonVitesse_2 = new Bouton(posX, posY, textureBtn32px.getWidth(), textureBtn32px.getHeight(), "",
				Bouton.ROND_HD_MENU_30PX);
		boutonVitesse_2.ajouterSpriteLogo(Image.getImage(VariableCommune.LOGO_VITESSE2), posX, posY);

		posX = Parametre.LARGEUR_ECRAN - textureFond.getWidth() * 0.095f;
		posY = 355 + Parametre.DECALAGE_Y;
		boutonVitesse_3 = new Bouton(posX, posY, textureBtn32px.getWidth(), textureBtn32px.getHeight(), "",
				Bouton.ROND_HD_MENU_30PX);
		boutonVitesse_3.ajouterSpriteLogo(Image.getImage(VariableCommune.LOGO_VITESSE3), posX, posY);

		posX = Parametre.LARGEUR_ECRAN - textureFond.getWidth() * 0.05f;
		posY = 360 + Parametre.DECALAGE_Y;
		boutonVitesse_stop = new Bouton(posX, posY, textureBtn32px.getWidth(), textureBtn32px.getHeight(), "",
				Bouton.ROND_HD_MENU_30PX);
		boutonVitesse_stop.ajouterSpriteLogo(Image.getImage(VariableCommune.LOGO_PAUSE), posX, posY);
		// alimentation btn 3 pars
		alimenterBtn3Parts(textureFond);

		this.menuAction = new MenuAction();

		// init du temps
		this.bitmapFont = new BitmapFont(Gdx.files.internal(Parametre.FONT), false);
		this.posXTemps = (int) this.boutonInformation.getX() + 125;
		this.posYTemps = (int) this.boutonVitesse_1.getY() - 10;
	}

	public void alimenterBtn3Parts(final Texture textureFond) {
		this.boutonTroisParsConstruction = new ArrayList<>();
		int largeurBtn = 150;
		float posX = Parametre.LARGEUR_ECRAN - (textureFond.getWidth() * 0.40f + largeurBtn);
		float posY = 20 + Parametre.DECALAGE_Y;
		final BoutonTroisPars boutonTroisPars_structure = creerBtnMenu(textureFond, largeurBtn,
				"txt.construction.structure", posX, posY);
		this.boutonTroisParsConstruction.add(boutonTroisPars_structure);

		final BoutonTroisPars boutonTroisPars_culture = creerBtnMenu(textureFond, largeurBtn,
				"txt.construction.culture", posX, 42 + posY);
		this.boutonTroisParsConstruction.add(boutonTroisPars_culture);

		final BoutonTroisPars boutonTroisPars_meuble = creerBtnMenu(textureFond, largeurBtn, "txt.construction.meuble",
				posX, 42 * 2 + posY);
		this.boutonTroisParsConstruction.add(boutonTroisPars_meuble);
		
		final BoutonTroisPars boutonTroisPars_sol = creerBtnMenu(textureFond, largeurBtn, "txt.construction.sol",
				posX, 42 * 3 + posY);
		this.boutonTroisParsConstruction.add(boutonTroisPars_sol);
	}

	/**
	 * Creer le menu des objet a construire
	 * 
	 * @param textureFond
	 * @param largeurBtn
	 * @param string
	 * @param posX
	 * @param posY
	 * @return BoutonTroisPars
	 */
	private BoutonTroisPars creerBtnMenu(Texture textureFond, int largeurBtn, String string, float posX, float posY) {
		final BoutonTroisPars boutonTroisPars_culture = new BoutonTroisPars(posX, posY, largeurBtn, 42,
				Parametre.bundle.get(string), Bouton.RECT_3_PART_1);
		return boutonTroisPars_culture;
	}

	@Override
	public boolean isClique(final float x, final float y) {
		Boolean resetSelecteur = false;
		isCliqueBoutonTroisPars(x, y);
		if (this.boutonConstruction.isClique(x, y)) {
			this.menuAction.stopToShow();
			if (showConstructionList) {
				showConstructionList = false;
			} else {
				showConstructionList = true;
			}
		} else if (this.boutonCulture.isClique(x, y)) {
			this.menuAction.stopToShow();
			resetSelecteur = true;
			showConstructionList = false;
		} else if (this.boutonInformation.isClique(x, y)) {
			resetSelecteur = true;
		} else if (this.boutonVitesse_1.isClique(x, y)) {
			Parametre.gererVitesse(Parametre.VITESSE_NORMAL);
			resetSelecteur = true;
		} else if (this.boutonVitesse_2.isClique(x, y)) {
			Parametre.gererVitesse(Parametre.VITESSE_RAPIDE);
			resetSelecteur = true;
		} else if (this.boutonVitesse_3.isClique(x, y)) {
			Parametre.gererVitesse(Parametre.VITESSE_TRES_RAPIDE);
			resetSelecteur = true;
		} else if (this.boutonVitesse_stop.isClique(x, y)) {
			Parametre.gererPause();
			resetSelecteur = true;
		}
		this.menuAction.isClique(x, y);

		if (resetSelecteur) {
			Action.ordreReset();
			resetSelector();
		}
		return false;
	}

	private void isCliqueBoutonTroisPars(final float x, final float y) {
		boolean isClique = false;
		if (showConstructionList) {
			for (int i = 0; i < this.boutonTroisParsConstruction.size(); i++) {
				isClique = this.boutonTroisParsConstruction.get(i).isClique(x, y);
				if (isClique) {
					Action.ordreReset();
					resetSelector();
					if (i == CONSTRUCTION_STRUCTURE) {
						this.menuAction.show(true, ElementEarth.structure_constructible);
					} else if (i == CONSTRUCTION_CULTURE) {
						this.menuAction.show(true, ElementEarth.culture_sol_constructible);
					}else if (i == CONSTRUCTION_MEUBLE) {
						this.menuAction.show(true, ElementEarth.meuble_constructible);
					}else if (i == CONSTRUCTION_SOL) {
						this.menuAction.show(true, ElementEarth.sol_constructible);
					}
				}
			}
		}
	}

	private void isOverBoutonTroisPars(float x, float y) {
		if (showConstructionList) {
			for (int i = 0; i < this.boutonTroisParsConstruction.size(); i++) {
				this.boutonTroisParsConstruction.get(i).isOver(x, y);
			}
		}
	}

	@Override
	public boolean isOver(final float x, final float y) {
		this.boutonConstruction.isOver(x, y);
		this.boutonCulture.isOver(x, y);
		this.boutonInformation.isOver(x, y);
		this.boutonVitesse_1.isOver(x, y);
		this.boutonVitesse_2.isOver(x, y);
		this.boutonVitesse_3.isOver(x, y);
		this.boutonVitesse_stop.isOver(x, y);
		isOverBoutonTroisPars(x, y);
		this.menuAction.isOver(x, y);
		return false;
	}

	@Override
	public void touchUP(int x, int y) {
	}

	@Override
	public void render() {
		this.batch.begin();
		this.sprite.draw(batch);
		this.boutonConstruction.render(batch);
		this.boutonCulture.render(batch);
		this.boutonInformation.render(batch);
		this.boutonVitesse_1.render(batch);
		this.boutonVitesse_2.render(batch);
		this.boutonVitesse_3.render(batch);
		this.boutonVitesse_stop.render(batch);
		renderBoutonsTroisPars();

		this.bitmapFont.getData().setScale(0.4f);
		this.bitmapFont.draw(batch, Base.getInstance().getTemps().showTemp(), this.posXTemps, this.posYTemps);
		this.batch.end();
		this.menuAction.render();
	}

	private void renderBoutonsTroisPars() {
		if (showConstructionList) {
			for (int i = 0; i < this.boutonTroisParsConstruction.size(); i++) {
				this.boutonTroisParsConstruction.get(i).render(batch);
			}
		}
	}

	@Override
	public void resetSelector() {
		this.menuAction.resetSelector();

	}

}
