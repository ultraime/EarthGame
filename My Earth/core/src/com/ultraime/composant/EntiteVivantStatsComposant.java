package com.ultraime.composant;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ultraime.database.Action;
import com.ultraime.game.entite.EntiteVivante;
import com.ultraime.game.entite.Habiliter;
import com.ultraime.game.utile.Image;
import com.ultraime.game.utile.Parametre;
import com.ultraime.game.utile.VariableCommune;

public class EntiteVivantStatsComposant extends Composant {
	private Sprite srpiteFont;
	private Sprite[] spriteStatsBarre = new Sprite[4];
	private BitmapFont bitmapFont;

	Sprite spriteColor[] = new Sprite[4];
	public final static int stat_vie = 0;
	public final static int stat_energie = 1;
	public final static int stat_satiete = 2;
	public final static int stat_hydra = 3;

	public EntiteVivantStatsComposant() {
		batch = new SpriteBatch();
		final Texture textureFond = Image.getImage(VariableCommune.HUD_CADRE_STATS);
		this.srpiteFont = new Sprite(textureFond);
		this.srpiteFont.setPosition(0, 15 + Parametre.DECALAGE_Y * 2);
		this.bitmapFont = new BitmapFont(Gdx.files.internal(Parametre.FONT), false);

		final Texture textureStat = Image.getImage(VariableCommune.HUD_BARRE_STATS);

		for (int i = 0; i < spriteStatsBarre.length; i++) {
			spriteStatsBarre[i] = new Sprite(textureStat);
			spriteStatsBarre[i].setPosition(120, 405 - (40 + (30 * i) - Parametre.DECALAGE_Y * 2));
		}
		initialiserSpriteStat();
	}

	private void initialiserSpriteStat() {
		int i = 0;
		spriteColor[stat_vie] = new Sprite(Image.getImage(VariableCommune.HUD_STAT_VIE));
		spriteColor[stat_vie].setPosition(122, 405 - (38 + (28 * i++) - Parametre.DECALAGE_Y * 2));

		spriteColor[stat_energie] = new Sprite(Image.getImage(VariableCommune.HUD_STAT_ENERGIE));
		spriteColor[stat_energie].setPosition(122, 405 - (38 + (30 * i++) - Parametre.DECALAGE_Y * 2));

		spriteColor[stat_satiete] = new Sprite(Image.getImage(VariableCommune.HUD_STAT_SATIETE));
		spriteColor[stat_satiete].setPosition(122, 405 - (38 + (30 * i++) - Parametre.DECALAGE_Y * 2));

		spriteColor[stat_hydra] = new Sprite(Image.getImage(VariableCommune.HUD_STAT_HYDRA));
		spriteColor[stat_hydra].setPosition(122, 405 - (38 + (30 * i++) - Parametre.DECALAGE_Y * 2));

	}

	@Override
	public void render() {
		this.batch.begin();
		updateSpriteColor();
		this.srpiteFont.draw(batch);
		this.bitmapFont.getData().setScale(0.5f);

		final EntiteVivante entiteVivante = (EntiteVivante) Action.body.getUserData();

		this.bitmapFont.draw(batch, entiteVivante.prenom, 100, 405 + Parametre.DECALAGE_Y * 2);

		this.bitmapFont.draw(batch, Parametre.bundle.get("txt.stat.sante"), 15, 405 - 30 + Parametre.DECALAGE_Y * 2);
		this.bitmapFont.draw(batch, Parametre.bundle.get("txt.stat.energie"), 15, 405 - 60 + Parametre.DECALAGE_Y * 2);
		this.bitmapFont.draw(batch, Parametre.bundle.get("txt.stat.satiete"), 15, 405 - 90 + Parametre.DECALAGE_Y * 2);
		this.bitmapFont.draw(batch, Parametre.bundle.get("txt.stat.hydratation"), 15,
				405 - 120 + Parametre.DECALAGE_Y * 2);

		for (int i = 0; i < spriteStatsBarre.length; i++) {
			spriteStatsBarre[i].draw(batch);
			spriteColor[i].draw(batch);
		}
		this.batch.end();
	}

	private void updateSpriteColor() {
		final EntiteVivante entiteVivante = (EntiteVivante) Action.body.getUserData();
		final Habiliter habiliter = entiteVivante.habiliter;

		int tailleBarre = calculerTailleBarre(habiliter.sante[Habiliter.ACTUEL], habiliter.sante[Habiliter.MAX],
				entiteVivante);
		spriteColor[stat_vie].setSize(Parametre.x(tailleBarre), 16);

		tailleBarre = calculerTailleBarre(habiliter.energie[Habiliter.ACTUEL], habiliter.energie[Habiliter.MAX],
				entiteVivante);
		spriteColor[stat_energie].setSize(Parametre.x(tailleBarre), 16);

		tailleBarre = calculerTailleBarre(habiliter.satiete[Habiliter.ACTUEL], habiliter.satiete[Habiliter.MAX],
				entiteVivante);
		spriteColor[stat_satiete].setSize(Parametre.x(tailleBarre), 16);

		tailleBarre = calculerTailleBarre(habiliter.hydratation[Habiliter.ACTUEL], habiliter.hydratation[Habiliter.MAX],
				entiteVivante);
		spriteColor[stat_hydra].setSize(Parametre.x(tailleBarre), 16);

	}

	/**
	 * @return
	 */
	private int calculerTailleBarre(final int statActu, final int statMax, final EntiteVivante entiteVivante) {
		final int maxBarre = 146;
		int tailleBarre = statActu * maxBarre / statMax;
		return tailleBarre;
	}

	@Override
	public boolean isClique(final float x, float y) {
		boolean isClique = false;
		return isClique;
	}

	@Override
	public boolean isOver(final float x, float y) {
		boolean isOver = false;

		return isOver;
	}

	@Override
	public void touchUP(int x, int y) {

	}

	@Override
	public void resetSelector() {
	}

}
