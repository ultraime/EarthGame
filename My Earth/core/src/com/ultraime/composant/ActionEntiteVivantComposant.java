package com.ultraime.composant;

import java.util.ArrayDeque;
import java.util.Iterator;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ultraime.game.utile.Parametre;

public class ActionEntiteVivantComposant extends Composant {
	private Bouton boutonNoAction;
	private Bouton boutonDeplacement;
	private Bouton boutonConstruction;
	private ArrayDeque<Bouton> listBouton;
	
	private Boolean isShowAllList = false;

	public int action_en_cours = 0;
	public static int ACTION_RIEN = 0;
	public static int ACTION_DEPLACEMENT = 1;
	public static int ACTION_CONSTRUCTION = 2;

	private static int TAILLE_BOUTON = 64;
	private static int POSITION_X = 100;
	private static int POSITION_Y = 100;

	public ActionEntiteVivantComposant() {
		listBouton = new ArrayDeque<Bouton>();
		this.boutonDeplacement = new Bouton(Parametre.x(POSITION_X), Parametre.y(POSITION_Y),
				Parametre.x(TAILLE_BOUTON), Parametre.y(TAILLE_BOUTON), "", Bouton.DEPLACEMENT);
		this.boutonNoAction = new Bouton(Parametre.x(POSITION_X), Parametre.y(POSITION_Y), Parametre.x(TAILLE_BOUTON),
				Parametre.y(TAILLE_BOUTON), "", Bouton.CLASSIQUE);
		this.boutonConstruction = new Bouton(Parametre.x(POSITION_X), Parametre.y(POSITION_Y),
				Parametre.x(TAILLE_BOUTON), Parametre.y(TAILLE_BOUTON), "", Bouton.CONSTRUCTION);
		listBouton.addFirst(boutonConstruction);
		listBouton.addFirst(boutonDeplacement);
		listBouton.addFirst(boutonNoAction);
		batch = new SpriteBatch();

	}

	@Override
	public void render() {
		this.batch.begin();
		if (!isShowAllList) {
			this.listBouton.getFirst().setPosition(Parametre.x(POSITION_X), Parametre.y(POSITION_Y));
			this.listBouton.getFirst().render(this.batch);
		} else {
			final Integer listSize = new Integer(listBouton.size());
			for (int i = 0; i < listSize; i++) {
				Bouton bouton = listBouton.removeFirst();
				bouton.setPosition(Parametre.x(POSITION_X), Parametre.y(POSITION_Y + (i * TAILLE_BOUTON)));
				listBouton.addLast(bouton);
			}
			for (Iterator<Bouton> iterator = listBouton.iterator(); iterator.hasNext();) {
				Bouton bouton = (Bouton) iterator.next();
				bouton.render(batch);
			}
			this.listBouton.getLast().render(this.batch);

		}
		this.batch.end();
	}

	@Override
	public boolean isClique(final float x, float y) {
		boolean isClique = false;
		if (!isShowAllList) {
			isShowAllList = isClique = this.listBouton.getFirst().isClique(x, y);
		} else {
			for (Iterator<Bouton> iterator = listBouton.iterator(); iterator.hasNext();) {
				Bouton bouton = (Bouton) iterator.next();
				isClique = bouton.isClique(x, y);
				if (isClique) {
					this.listBouton.remove(bouton);
					this.listBouton.addFirst(bouton);
					if (bouton == this.boutonConstruction) {
						this.action_en_cours = ACTION_CONSTRUCTION;
					} else if (bouton == this.boutonDeplacement) {
						this.action_en_cours = ACTION_DEPLACEMENT;
					} else {
						this.action_en_cours = ACTION_RIEN;
					}
					isShowAllList = false;
					break;
				}
			}
		}
		return isClique;
	}

	@Override
	public boolean isOver(final float x, float y) {
		boolean isOver = false;
		if (!isShowAllList) {
			isOver = this.listBouton.getFirst().isOver(x, y);
		} else {
			for (Iterator<Bouton> iterator = listBouton.iterator(); iterator.hasNext();) {
				Bouton bouton = (Bouton) iterator.next();
				bouton.isOver(x, y);
			}
		}
		return isOver;
	}

	@Override
	public void touchUP(int x, int y) {
		this.listBouton.getFirst().touchUP(x, y);

	}

	@Override
	public void resetSelector() {
	}

}
