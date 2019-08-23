package com.ultraime.database.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.physics.box2d.World;
import com.ultraime.game.entite.EntiteJoueur;
import com.ultraime.game.entite.EntiteVivante;
import com.ultraime.game.entite.EntiteVivante.TypeEntiteVivante;
import com.ultraime.game.metier.WorldBodyService;
import com.ultraime.game.metier.WorldService;
import com.ultraime.game.metier.travail.Metier;

public class BasePersonnage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<EntiteJoueur> entiteJoueurs;

	public BasePersonnage() {
		entiteJoueurs = new ArrayList<EntiteJoueur>();
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

	public void ajouterMetier(final Metier metier, final EntiteJoueur entiteJoueur) {
		entiteJoueur.ajouterMetier(metier);
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
	public void creerHitboxPersonnage(final World world, final World worldAffichage, final float radius, final float posx,
			final float posy, EntiteVivante entiteVivante) {
		WorldBodyService.creerCercleVivant(world, radius, posx, posy, entiteVivante);
		WorldBodyService.creerCercleVivant(worldAffichage, WorldService.MULTIPLICATEUR * radius,
				posx * WorldService.MULTIPLICATEUR + 32, posy * WorldService.MULTIPLICATEUR + 32, entiteVivante);

	}

	public List<EntiteJoueur> getEntiteJoueurs() {
		return entiteJoueurs;
	}

	public void setEntiteJoueurs(List<EntiteJoueur> entiteJoueurs) {
		this.entiteJoueurs = entiteJoueurs;
	}
	
	
}
