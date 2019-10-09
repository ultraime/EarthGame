package com.ultraime.composant;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ultraime.database.Action;
import com.ultraime.database.Ordre;
import com.ultraime.database.base.Base;
import com.ultraime.database.entite.ElementEarth;
import com.ultraime.game.metier.TiledMapClickListener;
import com.ultraime.game.utile.Parametre;

public class MenuAction extends Composant {

	private List<ElementEarthBouton> elementsConstructionStructure;

	private boolean show = false;

	public void stopToShow() {
		show = false;
		Action.ordreReset();
	}

	public MenuAction() {
		this.batch = new SpriteBatch();
	}

	public void alimenterListAShow(String elementType) {
		this.elementsConstructionStructure = new ArrayList<>();
		float posX = Parametre.x(300);
		float posY = Parametre.y(100);
		List<ElementEarth> elementEarths = Base.getInstance().recupererElementEarth(elementType);

		for (ElementEarth earth : elementEarths) {
			final Bouton boutonStructure = new Bouton(posX, posY, 50, 50, "", Bouton.CARRE_50PX);
			if (earth.logoImage != null) {
				final Texture txtLogo = new Texture(earth.logoImage);
				boutonStructure.ajouterSpriteLogo(txtLogo, posX, posY);
			}
			final ElementEarthBouton eeb = new ElementEarthBouton(earth, boutonStructure);
			elementsConstructionStructure.add(eeb);
			posX = posX + 50;
		}
	}

	private ElementEarth isCliqueList(final float x, final float y, final List<ElementEarthBouton> elements) {
		ElementEarth elementEarth = null;
		Bouton btnSelect = null;
		for (int i = 0; i < elements.size(); i++) {
			final Bouton bouton = elements.get(i).bouton;
			if (bouton.isSelect()) {
				btnSelect = bouton;
				break;
			}
		}
		for (int i = 0; i < elements.size(); i++) {
			final Bouton bouton = elements.get(i).bouton;
			if (bouton.isClique(x, y)) {
				bouton.setSelect(true);
				elementEarth = elements.get(i).elementEarth;
				if (btnSelect != null && btnSelect != bouton) {
					btnSelect.setSelect(false);
				}
			} else {
				if (Action.ORDRE == null) {
					bouton.setSelect(false);
				}
			}
		}
		return elementEarth;
	}

	@Override
	public boolean isClique(float x, float y) {
		ElementEarth elementEarth = null;
		if (show) {
			elementEarth = isCliqueList(x, y, elementsConstructionStructure);
			if (elementEarth != null) {
				Action.ordre(new Ordre(Ordre.construire, elementEarth));
			}
			if (elementEarth != null) {
				TiledMapClickListener.IGNORE_CLICK = true;
			}
		}
		return false;
	}

	@Override
	public boolean isOver(float x, float y) {
		if (show) {
			isOverList(x, y, elementsConstructionStructure);
		}
		return false;
	}

	private void isOverList(float x, float y, List<ElementEarthBouton> elements) {
		for (int i = 0; i < elements.size(); i++) {
			elements.get(i).bouton.isOver(x, y);
		}
	}

	@Override
	public void touchUP(int x, int y) {
		if (show) {
			touchUPList(x, y, elementsConstructionStructure);
		}
	}

	private void touchUPList(int x, int y, List<ElementEarthBouton> elements) {
		for (int i = 0; i < elements.size(); i++) {
			elements.get(i).bouton.touchUP(x, y);
		}
	}

	@Override
	public void render() {
		this.batch.begin();
		if (show)
			renderList(elementsConstructionStructure);

		this.batch.end();

	}

	private void renderList(final List<ElementEarthBouton> elementsList) {
		for (int i = 0; i < elementsList.size(); i++) {
			elementsList.get(i).bouton.render(batch);
		}

	}

	@Override
	public void resetSelector() {
		if (elementsConstructionStructure != null) {
			for (int i = 0; i < elementsConstructionStructure.size(); i++) {
				Bouton bouton = elementsConstructionStructure.get(i).bouton;
				bouton.setSelect(false);
			}
		}

	}

	public void show(final Boolean isShow, final String elementType) {
		show = isShow;
		alimenterListAShow(elementType);

	}

}
