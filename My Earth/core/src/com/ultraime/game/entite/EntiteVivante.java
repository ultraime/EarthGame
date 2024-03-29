package com.ultraime.game.entite;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.ultraime.animation.AnimationManager;
import com.ultraime.game.metier.pathfinding.Aetoile.Noeud;
import com.ultraime.game.metier.travail.Metier;
import com.ultraime.game.metier.travail.action.ActionEntite;

public abstract class EntiteVivante extends Entite {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	transient public static final int BAS = 0;
	transient public static final int GAUCHE = 1;
	transient public static final int DROITE = 2;
	transient public static final int HAUT = 3;

	public static enum TypeEntiteVivante {
		PERSONNAGE, ZOMBIE
	}

	public static enum TypeShape {
		CERCLE, RECTANGLE
	}

	// Le type de l'entite
	public TypeShape typeShape;
	protected Circle cercleShape;

	// pour l'animation
	protected AnimationManager animationManager;
	protected int direction = 0;
	// pour le deplacement
	private ArrayDeque<Noeud> listeDeNoeudDeplacement;
	private List<ActionEntite> listeAction;
	private ActionEntite actionEntite = null;

	// élément propre au entité
	private float vitesse = 4f;//3f
	protected List<Metier> metiers;
	public Inventaire inventaire;

	protected abstract void creerAnimation();

	/**
	 * @param x
	 * @param y
	 * @param radius
	 */
	public EntiteVivante(final float x, final float y, final float radius) {
		super(x, y);
		this.listeDeNoeudDeplacement = new ArrayDeque<Noeud>();
		this.listeAction = new ArrayList<ActionEntite>();
		this.typeShape = TypeShape.CERCLE;
		this.cercleShape = new Circle(x, y, radius);
		this.metiers = new ArrayList<>();
		this.inventaire = new Inventaire(50);
	}

	/**
	 * @param metier
	 */
	public void ajouterMetier(final Metier metier) {
		this.metiers.add(metier);
	}

	public void doMetier() {
		boolean isDoingMetier = false;
		if (this.metiers.size() > 0) {
			for (Metier metier : metiers) {
				isDoingMetier = metier.doMetier();
				if (isDoingMetier) {
					break;
				}
			}
		}
	}

	/**
	 * @param entiteVivante
	 * @param world
	 * @param worldAffichage
	 */
	public boolean doAction(final Body body, final World world, final World worldAffichage) {
		doMetier();
		boolean isActionEnd = false;
		if (actionEntite == null) {
			if (listeAction.size() > 0) {
				actionEntite = listeAction.get(0);
				actionEntite.initAction(world, body);
			}
		}
		if (actionEntite != null) {
			boolean isEnd = actionEntite.doAction(body, world, worldAffichage);
			if (isEnd) {
				listeAction.remove(0);
				actionEntite = null;
				isActionEnd = true;
			}
		}
		return isActionEnd;
	}

	public List<ActionEntite> getListeAction() {
		return listeAction;
	}

	public void setListeAction(List<ActionEntite> listeAction) {
		this.listeAction = listeAction;
	}

	public void ajouterAction(ActionEntite actionEntite) {
		listeAction.add(actionEntite);

	}

	public float getVitesse() {
		return vitesse;
	}

	public void setVitesse(float vitesse) {
		this.vitesse = vitesse;
	}

	public ArrayDeque<Noeud> getListeDeNoeudDeplacement() {
		return listeDeNoeudDeplacement;
	}

	public void setListeDeNoeudDeplacement(ArrayDeque<Noeud> listeDeNoeudDeplacement) {
		this.listeDeNoeudDeplacement = listeDeNoeudDeplacement;
	}

	public Circle getCercleShape() {
		return cercleShape;
	}

	public void setCercleShape(Circle cercleShape) {
		this.cercleShape = cercleShape;
	}

	public int getDirection() {
		return direction;
	}

	public void setDirection(int direction) {
		this.direction = direction;
	}

}
