package com.ultraime.database;

import java.io.File;
import java.io.IOException;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.ultraime.animation.AnimationManager;
import com.ultraime.database.base.Base;
import com.ultraime.game.entite.Inventaire;
import com.ultraime.game.utile.Parametre;

/**
 * @author Ultraime
 *
 */
public class LecteurXML extends ApplicationAdapter {
	private static LecteurXML INSTANCE = new LecteurXML();

	public static LecteurXML getInstance() {
		return INSTANCE;
	}

	private LecteurXML() {
	}

	/**
	 * Seul méthode en public. Permet de lire et de générer toutes les data.
	 */
	public void traiterToutLesFichiers() {
		long startTime = System.currentTimeMillis();
		recuperationDataFromXML("data/ElementEarth/");
		long endTime = System.currentTimeMillis();

		if (Parametre.MODE_DEBUG) {
			System.out.println("Lecture des fichiers XML en :" + (endTime - startTime) + " ms");
		}
	}

	/**
	 * @param chemin
	 */
	private void recuperationDataFromXML(final String chemin) {
		//
		FileHandle handle = Gdx.files.internal(chemin);
		String[] listefichiers = getFichierDansRepertoire(handle);
		for (int i = 0; i < listefichiers.length; i++) {
			traiterFichier(listefichiers[i], chemin);
		}
	}

	/**
	 * @param file
	 * @param chemin
	 */
	private void traiterFichier(final String file, final String chemin) {
		final FileHandle handle = Gdx.files.internal(chemin + file);
		final XmlReader xmlReader = new XmlReader();
		try {
			Element rootElement = xmlReader.parse(handle);
			if (rootElement.getName().equals("ElementEarth")) {
				lireElementEarthXML(rootElement);
			}

		} catch (IOException e) {
			if (Parametre.MODE_DEBUG) {
				e.printStackTrace();
			}
		}

	}

	private void lireElementEarthXML(Element rootElement) {
		Array<Element> items = rootElement.getChildrenByName("elementEarths");
		for (Element child : items) {
			ElementEarth elementEarth = new ElementEarth();
			elementEarth.type = child.getChildByName("type").getText();
			elementEarth.placementType = child.getChildByName("placementType").getText();
			elementEarth.nom = child.getChildByName("nom").getText();
			elementEarth.elementEarthEvolution = child.getChildByName("evolution").getText();
			if (child.getChildByName("rotation") != null) {
				elementEarth.rotation = child.getChildByName("rotation").getText();
			}
			if (child.getChildByName("capaciteInventaire") != null) {
				final float capaciteInventaire = Float.parseFloat(child.getChildByName("capaciteInventaire").getText());
				final Inventaire inventaire = new Inventaire(capaciteInventaire);
				elementEarth.inventaire = inventaire;
			}
			if (child.getChildByName("poids") != null) {
				elementEarth.poids = Float.parseFloat(child.getChildByName("poids").getText());
			}
			if (child.getChildByName("nombreRecolte") != null) {
				elementEarth.nombreRecolte = Float.parseFloat(child.getChildByName("nombreRecolte").getText());
			}
			if (child.getChildByName("tpsEvolution") != null) {
				elementEarth.tempEvolution = Integer.parseInt(child.getChildByName("tpsEvolution").getText());
			}
			if (child.getChildByName("logoImage") != null) {
				elementEarth.logoImage = child.getChildByName("logoImage").getText();
			}
			Array<Element> itemsImageElement = child.getChildrenByName("imageElement");
			for (Element childImageElement : itemsImageElement) {
				ElementEarthImage elementEarthImage = new ElementEarthImage();
				elementEarthImage.x = Integer.parseInt(childImageElement.getChildByName("x").getText());
				elementEarthImage.y = Integer.parseInt(childImageElement.getChildByName("y").getText());
				elementEarthImage.idTuile = Integer.parseInt(childImageElement.getChildByName("idTuile").getText());
				String strCollision = childImageElement.getChildByName("collision").getText();
				if ("true".equals(strCollision)) {
					elementEarthImage.isCollision = true;
				}
				elementEarth.addElementEarthImage(elementEarthImage);
			}
			elementEarth.layerCible = child.getChildByName("layerCible").getText();

			Array<Element> elementAnimation = child.getChildrenByName("animation");
			for (Element childAnimation : elementAnimation) {
				final int largeur = Integer.parseInt(childAnimation.getChildByName("largeur").getText());
				final int hauteur = Integer.parseInt(childAnimation.getChildByName("hauteur").getText());
				final float vitesseAnimation = Float
						.parseFloat((childAnimation.getChildByName("vitesseAnimation").getText()));
				final String lienImage = childAnimation.getChildByName("lienImage").getText();
				AnimationManager animationManager = new AnimationManager(largeur, hauteur, vitesseAnimation, lienImage);
				elementEarth.animationManager = animationManager;
			}
			Base.getInstance().addReferenceElementEarth(elementEarth);
		}

	}

	public String[] getFichierDansRepertoire(final FileHandle handle) {
		File repertoire = handle.file();
		return repertoire.list();
	}

}
