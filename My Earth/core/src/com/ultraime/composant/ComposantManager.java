package com.ultraime.composant;

import com.ultraime.database.Action;
import com.ultraime.game.entite.EntiteJoueur;

public class ComposantManager {

	private Composant composant;

	public ComposantManager(final Composant composant) {
		this.composant = composant;
	}

	public void render() {
		if (this.composant instanceof ActionEntiteVivantComposant && Action.body != null) {
			if (Action.body.getUserData() instanceof EntiteJoueur) {
				this.composant.render();
			}
		} else if (this.composant instanceof HudComposant) {
			this.composant.render();
		}
	}

	public boolean isClique(final float x, float y) {
		boolean isClique = false;
		if (this.composant instanceof ActionEntiteVivantComposant && Action.body != null) {
			if (Action.body.getUserData() instanceof EntiteJoueur) {
				ActionEntiteVivantComposant actionEntiteVivantComposant = (ActionEntiteVivantComposant) this.composant;
				isClique = actionEntiteVivantComposant.isClique(x, y);
			}
		}
		if (this.composant instanceof HudComposant) {
			this.composant.isClique(x, y);
		}
		return isClique;
	}


	public boolean isOver(final float x, float y) {
		boolean isOver = false;
		if (this.composant instanceof ActionEntiteVivantComposant && Action.body != null) {
			if (Action.body.getUserData() instanceof EntiteJoueur) {
				isOver = this.composant.isOver(x, y);
			}
		} else if (this.composant instanceof HudComposant) {
			this.composant.isOver(x, y);
		}
		return isOver;
	}

	public void touchUP(int x, int y) {
		if (this.composant instanceof ActionEntiteVivantComposant && Action.body != null) {
			if (Action.body.getUserData() instanceof EntiteJoueur) {
				this.composant.touchUP(x, y);
			}
		}

	}

	public Composant getComposant() {
		return composant;
	}

	public void setComposant(Composant composant) {
		this.composant = composant;
	}

	public void resetSelector() {
		this.composant.resetSelector();

	}

}
