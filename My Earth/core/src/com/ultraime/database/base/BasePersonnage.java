package com.ultraime.database.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.ultraime.game.entite.EntiteAnimal;
import com.ultraime.game.entite.EntiteJoueur;
import com.ultraime.game.entite.EntiteVivante;
import com.ultraime.game.entite.EntiteVivante.TypeEntiteVivante;
import com.ultraime.game.metier.WorldBodyService;
import com.ultraime.game.metier.WorldService;
import com.ultraime.game.metier.thread.EntiteManagerThread;

public class BasePersonnage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<EntiteJoueur> entiteJoueurs;
	private List<EntiteAnimal> entiteAnimals;
	
	private transient EntiteManagerThread<EntiteJoueur> threadEntiteJoueurs;
	private transient EntiteManagerThread<EntiteAnimal> threadEntiteAnimal;
	
	public BasePersonnage() {
		entiteJoueurs = new ArrayList<EntiteJoueur>();
		threadEntiteJoueurs = new EntiteManagerThread<EntiteJoueur>(entiteJoueurs);
		
		entiteAnimals = new ArrayList<EntiteAnimal>();
		threadEntiteAnimal = new EntiteManagerThread<EntiteAnimal>(entiteAnimals);
	}

	public void initThreadEntiteJoueurs(final List<EntiteJoueur> entiteJoueurs) {
		threadEntiteJoueurs = new EntiteManagerThread<EntiteJoueur>(entiteJoueurs);
		threadEntiteJoueurs.start();
	}

	public void initThreadEntiteAnimal(final List<EntiteAnimal> entiteAnimals) {
		threadEntiteAnimal = new EntiteManagerThread<EntiteAnimal>(entiteAnimals);
		threadEntiteAnimal.start();
	}

	/**
	 * Créer un entité au joueur à l'emplacement X,Y
	 * 
	 * @param posx
	 * @param posy
	 * @return EntiteJoueur crée
	 */
	public EntiteJoueur creerEntiteJoueur(final int posx, final int posy) {
		final float radius = 0.4f;
		final EntiteJoueur entiteVivante = new EntiteJoueur(posx, posy, radius, TypeEntiteVivante.PERSONNAGE);
		entiteJoueurs.add(entiteVivante);

		creerHitboxPersonnage(WorldService.getInstance().world, WorldService.getInstance().worldAffichage, radius, posx,
				posy, entiteVivante);

		return entiteVivante;
	}

	/**
	 * Créer la hitbox du personnage.
	 * 
	 * @param world
	 * @param worldAffichage
	 * @param radius
	 * @param posx
	 * @param posy
	 * @param entiteVivante
	 */
	public void creerHitboxPersonnage(final World world, final World worldAffichage, final float radius,
			final float posx, final float posy, EntiteVivante entiteVivante) {
		Body body = WorldBodyService.creerCercleVivant(world, radius, posx, posy, entiteVivante);
		Body bodyAffichage = WorldBodyService.creerCercleVivant(worldAffichage, WorldService.MULTIPLICATEUR * radius,
				posx * WorldService.MULTIPLICATEUR + 32, posy * WorldService.MULTIPLICATEUR + 32, entiteVivante);

		WorldService.getInstance().bodiesEntiteVivant.add(body);
		WorldService.getInstance().bodiesAffichageEntiteVivant.add(bodyAffichage);

	}

	public void startEntiteThread() {
		this.threadEntiteJoueurs.start();
	}

	public List<EntiteJoueur> getEntiteJoueurs() {
		return entiteJoueurs;
	}

	public void setEntiteJoueurs(List<EntiteJoueur> entiteJoueurs) {
		this.entiteJoueurs = entiteJoueurs;
	}

	public EntiteManagerThread<EntiteJoueur> getThreadEntiteJoueurs() {
		return threadEntiteJoueurs;
	}

	public void setThreadEntiteJoueurs(EntiteManagerThread<EntiteJoueur> threadEntiteJoueurs) {
		this.threadEntiteJoueurs = threadEntiteJoueurs;
	}

	public List<EntiteAnimal> getEntiteAnimals() {
		return entiteAnimals;
	}

	public void setEntiteAnimals(List<EntiteAnimal> entiteAnimals) {
		this.entiteAnimals = entiteAnimals;
	}

	public EntiteManagerThread<EntiteAnimal> getThreadEntiteAnimal() {
		return threadEntiteAnimal;
	}

	public void setThreadEntiteAnimal(EntiteManagerThread<EntiteAnimal> threadEntiteAnimal) {
		this.threadEntiteAnimal = threadEntiteAnimal;
	}

}
