package com.ultraime.database;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ConcurrentModificationException;
import java.util.List;

import com.ultraime.database.base.Base;
import com.ultraime.database.entite.ElementEarth;
import com.ultraime.game.entite.EntiteJoueur;
import com.ultraime.game.metier.TileMapService;
import com.ultraime.game.metier.WorldService;
import com.ultraime.game.utile.Parametre;

public class SaveService {

	public static void save() {
		FileOutputStream fos;
		try {
			// this.suspendThread();

			// stopThread();
			fos = new FileOutputStream("save.sav");
			ObjectOutputStream oos = new ObjectOutputStream(fos);

			oos.writeObject(Base.getInstance());
			oos.close();
			// this.resumedThread();
			// startThread();
		} catch (FileNotFoundException e) {
			if (Parametre.MODE_DEBUG) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			if (Parametre.MODE_DEBUG) {
				e.printStackTrace();
			}
		} catch (ConcurrentModificationException e) {
			save();
		}
		if (Parametre.MODE_DEBUG) {
			System.out.println("Save ok ");
		}

	}

	public static void chargementData() {
		if (Parametre.MODE_DEBUG) {
			System.out.println(">> Début de chargement des datas");
		}
		try {
			// recuperation du save file
			FileInputStream fis = new FileInputStream("save.sav");
			ObjectInputStream ois = new ObjectInputStream(fis);

			// lecture de la save
			Base baseData = (Base) ois.readObject();
			Base.chargeData(baseData);
			LecteurXML.getInstance().traiterToutLesFichiers();

			// ici chargement et ajout des différents objets sur la carte
			chargerData(baseData.baseStructure.getElementEarthStructure());
			chargerData(baseData.baseCulture.getElementEarthPlantes());
			chargerData(baseData.baseMeuble.getElementEarthMeubles());
			chargerData(baseData.baseSol.getElementEarthSol());
			chargerData(baseData.baseNature.getElementEarthNature());
			chargerData(baseData.baseNature.getElementEarthEau());
			chargerData(baseData.baseObjetSol.getElementEarthsObjetSol());
			chargerData(baseData.baseObjetAConstruire.getElementEarthsAConstruire());
			chargerPersonnage(baseData);

			ois.close();
		} catch (ClassNotFoundException | IOException e) {
			System.err.println("ClassNotFoundException | IOException" + e);
		}
		if (Parametre.MODE_DEBUG) {
			System.out.println("<< fin de chargement des datas");
		}
	}

	/**
	 * @param baseData
	 */
	public static void chargerPersonnage(Base baseData) {
		List<EntiteJoueur> entiteJoueurs = baseData.basePersonnage.getEntiteJoueurs();
		for (int i = 0; i < entiteJoueurs.size(); i++) {
			final EntiteJoueur elem = entiteJoueurs.get(i);
			elem.creerAnimation();
			Base.getInstance().basePersonnage.creerHitboxPersonnage(WorldService.getInstance().world,
					WorldService.getInstance().worldAffichage, elem.getCercleShape().radius, elem.x, elem.y, elem);
		}
		Base.getInstance().basePersonnage.initThreadEntiteJoueurs(entiteJoueurs);

	}

	/**
	 * @param baseData
	 */
	private static void chargerData(List<ElementEarth> earths) {
		for (int i = 0; i < earths.size(); i++) {
			final ElementEarth elem = earths.get(i);
			TileMapService.getInstance().placerElementEarth(elem);
		}
	}

}
