package com.ultraime.game.entite;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.ultraime.animation.AnimationEntite;
import com.ultraime.animation.AnimationManager;
import com.ultraime.database.entite.ElementEarth;
import com.ultraime.game.metier.WorldService;
import com.ultraime.game.metier.pathfinding.Noeud;
import com.ultraime.game.metier.travail.Metier;
import com.ultraime.game.metier.travail.action.ActionEntite;

public abstract class EntiteVivante extends Entite implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	transient public static final int BAS = 0;
	transient public static final int GAUCHE = 1;
	transient public static final int DROITE = 2;
	transient public static final int HAUT = 3;

	public static enum TypeEntiteVivante {
		PERSONNAGE, ZOMBIE, POULE
	}

	public static enum TypeShape {
		CERCLE, RECTANGLE
	}

	// Le type de l'entite
	public TypeShape typeShape;
	protected Circle cercleShape;

	// pour l'animation
	protected AnimationEntite animationEntite;
	protected AnimationManager animationManager;
	protected int direction = 0;

	// pour le deplacement
	private ArrayDeque<Noeud> listeDeNoeudDeplacement;
	private List<ActionEntite> listeAction;
	private ActionEntite actionEntite = null;

	// élément propre au entité
	public Habiliter habiliter;
	public List<Metier> metiers;
	public Inventaire inventaire;
	protected TypeEntiteVivante typeEntiteEnum;

	// etat de l'entite
	public Etat etat = Etat.NORMAL;

	public static enum Etat {
		NORMAL, DORT
	}

	// Autre
	public String prenom = "Prenom par defaut";

	// Possesion
	/**
	 * Le lit de l'entité
	 */
	public ElementEarth lit;

	protected abstract void creerAnimation();

	/**
	 * @param x
	 * @param y
	 * @param radius
	 */
	public EntiteVivante(final float x, final float y, final float radius, final TypeEntiteVivante typeEntite) {
		super(x, y);
		this.listeDeNoeudDeplacement = new ArrayDeque<Noeud>();
		this.listeAction = new ArrayList<ActionEntite>();
		this.typeShape = TypeShape.CERCLE;
		this.cercleShape = new Circle(x, y, radius);
		this.metiers = new ArrayList<>();
		this.inventaire = new Inventaire(50);
		this.habiliter = new Habiliter();
		initAnimationEtat();

		this.typeEntiteEnum = typeEntite;
		creerAnimation();
	}

	public void initAnimationEtat() {
		this.animationEntite = new AnimationEntite();
	}

	/**
	 * @param metier
	 */
	public void ajouterMetier(final Metier metier) {
		this.metiers.add(metier);
	}

	/**
	 * réalise le métier. (fait le calcul du déplacement, la recherche des éléments
	 * etc..).
	 */
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
	 * fait le déplacement, l'ajout de l'objet sur la carte etc... il y a quand même
	 * un aetoile de réaliser. A voir pour la perf.
	 * 
	 * @param body
	 * @param world
	 * @param worldAffichage
	 * @return
	 */
	public boolean doAction(final Body body, final World world, final World worldAffichage) {
		boolean isActionEnd = false;
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

	/**
	 * @param body
	 * @param world
	 * @param worldAffichage
	 * @return
	 */
	public boolean rechercheAction(final Body body, final World world, final World worldAffichage) {
		// doMetier();
		boolean isActionEnd = false;
		doMetier();
		if (actionEntite == null) {
			if (listeAction.size() > 0) {
				actionEntite = listeAction.get(0);
				actionEntite.initAction(world, body);
			}
		}
		return isActionEnd;
	}

	public void render(final SpriteBatch batch) {
		final float posX = this.x * WorldService.MULTIPLICATEUR;
		final float posY = this.y * WorldService.MULTIPLICATEUR;
		this.animationManager.render(batch, posX, posY, this.direction);
		if (this.etat.equals(Etat.DORT)) {
			this.animationEntite.renderAnimationDormir(batch, posX + 30, posY + 120);
		}
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

	public boolean isSurLit() {
		boolean isSurLit = false;
		if (lit != null) {
			if (lit.x == Math.round(this.x) && (int) lit.y == Math.round(this.y)) {
				isSurLit = true;
			}
		}
		return isSurLit;
	}

}
