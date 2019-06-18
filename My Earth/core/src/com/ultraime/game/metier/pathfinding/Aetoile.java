package com.ultraime.game.metier.pathfinding;

import java.io.Serializable;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.ultraime.database.Base;
import com.ultraime.game.entite.EntiteVivante;
import com.ultraime.game.entite.EntiteVivante.TypeShape;
import com.ultraime.game.utile.Parametre;

public class Aetoile implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static transient Rectangle rectangleHitbox = new Rectangle(0, 0, 64, 64);
	private static final int TAILLE_X = 10000;
	private static final int TAILLE_Y = 10000;

	private World world;
	// private Body body;

	public Boolean isCollision;

	// pour les collisions
	public Circle circleBody;

	/**
	 * On regarde si les objets à construire sont à prendre en compte dans la
	 * collision
	 */
	private boolean isCollisionEntiteConstructible = false;

	public Aetoile(final World world, final Body body) {
		this.world = world;

		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.KinematicBody;
		bodyDef.position.set(body.getPosition().x, body.getPosition().y);

		// this.body = world.createBody(bodyDef);

		EntiteVivante entiteVivante = (EntiteVivante) body.getUserData();
		if (entiteVivante.typeShape == TypeShape.CERCLE) {
			circleBody = new Circle(0, 0, 0.4f);
		}

	}

	public static Noeud creerNoeud(final int x, final int y, final double cout) {
		return new Noeud(x, y, cout);
	}

	public static class Noeud implements Serializable, Comparable<Noeud> {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public Noeud(final int x, final int y, final double cout) {
			this.x = x;
			this.y = y;
			this.cout = cout;
			this.isVisit = false;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + x;
			result = prime * result + y;
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Noeud other = (Noeud) obj;
			if (x != other.x)
				return false;
			if (y != other.y)
				return false;
			return true;
		}

		public int x, y;
		public double cout, heuristique;
		public boolean isVisit;
		public Noeud noeudParent;

		@Override
		public int compareTo(Noeud noeud) {
			if (this.heuristique < noeud.heuristique) {
				return -1;
			} else if (this.heuristique == noeud.heuristique) {
				return 0;
			}
			return 1;
		}

	}

	public List<Noeud> recupererNoeudVoisin(int x, int y) {
		List<Noeud> listDeNoeud = new ArrayList<Noeud>();

		// La case du noeud de départ : sont x et y
		final int taillePixel = 1;
		for (int i = x - taillePixel; i <= x + taillePixel; i++) {
			for (int j = y - taillePixel; j <= y + taillePixel; j++) {
				// si on est pas sur lui même.
				if (!(i == x && j == y)) {
					if (i > 0 && j > 0 && i < TAILLE_X && j < TAILLE_Y) {
						int xN = i;
						int yN = j;
						Boolean isCollision = false;
						if (i == x - taillePixel && j == y - taillePixel) {
							// si on va en diagonale bas à
							isCollision = isCollisionDiagonalBasGauche(xN, yN, taillePixel);
						} else if (i == x + taillePixel && j == y - taillePixel) {
							isCollision = isCollisionDiagonalBasDroite(xN, yN, taillePixel);
						} else if (i == x - taillePixel && j == y + taillePixel) {
							// si on va en diagonale bas à
							isCollision = isCollisionDiagonalHautGauche(xN, yN, taillePixel);
						} else if (i == x + taillePixel && j == y + taillePixel) {
							isCollision = isCollisionDiagonalHautDroite(xN, yN, taillePixel);
						}

						Noeud n = creerNoeud(xN, yN, 0);
						if (!isCollision(n) && !isCollision) {
							listDeNoeud.add(n);
						}

					}

				}
			}
		}

		return listDeNoeud;

	}

	/**
	 * @param objectif
	 * @param depart
	 * @param isControlDestination
	 * @return
	 * @throws AetoileException
	 * @throws AetoileDestinationBlockException
	 */
	public ArrayDeque<Noeud> cheminPlusCourt(final Noeud objectif, final Noeud depart, boolean isControlDestination)
			throws AetoileException, AetoileDestinationBlockException {
		// avant de commencer on regarde si la destination est vraiment
		// accesible
		ArrayDeque<Noeud> listDeNoeudRetour = new ArrayDeque<Noeud>();
		if (!isCollision(objectif) || !isControlDestination) {
			int iSecurity = 0;
			final int SECURITY = 1000;
			final int POSITIONARRIVE = 1;
			List<Noeud> listDeNoeudOpen = new ArrayList<Noeud>();
			List<Noeud> listDeNoeudClose = new ArrayList<Noeud>();
			listDeNoeudOpen.add(depart);
			// tant que openList n'est pas vide
			while (!listDeNoeudOpen.isEmpty() && iSecurity < SECURITY) {
				iSecurity++;
				Collections.sort(listDeNoeudOpen);
				Noeud noeudCourrant = listDeNoeudOpen.get(0);
				int testPositionX = Math.abs(noeudCourrant.x - objectif.x);
				int testPositionY = Math.abs(noeudCourrant.y - objectif.y);
				// si objectif
				if (noeudCourrant.equals(objectif)
						|| (testPositionX < POSITIONARRIVE && testPositionY < POSITIONARRIVE)) {
					// reconstituer chemin.
					// sortir de la boucle.
					listDeNoeudRetour.addFirst(noeudCourrant);
					boolean breakloop = false;
					while (!breakloop) {
						if (noeudCourrant.noeudParent != null) {
							listDeNoeudRetour.addFirst(noeudCourrant.noeudParent);
							noeudCourrant = noeudCourrant.noeudParent;
						} else {
							breakloop = true;
						}

					}
					break;
				} else {

					// pour chaque voisin noeudVoisin de noeudCourrant
					List<Noeud> listDeNoeudVoisin = recupererNoeudVoisin(noeudCourrant.x, noeudCourrant.y);
					for (int i = 0; i < listDeNoeudVoisin.size(); i++) {
						Noeud noeudVoisin = listDeNoeudVoisin.get(i);
						// si noeudVoisin existe dans closedList avec un cout
						// inférieur
						// ou si noeudVoisin existe dans openList avec un cout
						// inférieur
						boolean isNeRienFaire = false;
						if (listDeNoeudClose.contains(noeudVoisin)) {
							for (Iterator<Noeud> itr = listDeNoeudClose.iterator(); itr.hasNext();) {
								Noeud noeudTemporaire = itr.next();
								if (noeudVoisin.equals(noeudTemporaire)) {
									if (noeudTemporaire.cout > noeudVoisin.cout) {
										isNeRienFaire = true;
										break;
									}
								}
							}
						}
						if (!isNeRienFaire) {
							if (listDeNoeudOpen.contains(noeudVoisin)) {
								for (Iterator<Noeud> itr = listDeNoeudOpen.iterator(); itr.hasNext();) {
									Noeud noeudTemporaire = itr.next();
									if (noeudVoisin.equals(noeudTemporaire)) {
										if (noeudTemporaire.cout > noeudVoisin.cout) {
											isNeRienFaire = true;
											break;
										}
									}
								}
							}
						}
						if (!isNeRienFaire) {
							noeudVoisin.cout = noeudCourrant.cout + 1;
							noeudVoisin.heuristique = calculHeuristic(objectif, noeudVoisin);
							noeudVoisin.noeudParent = noeudCourrant;
							listDeNoeudOpen.add(noeudVoisin);
						}
						listDeNoeudClose.add(noeudVoisin);
					}
				}
				listDeNoeudOpen.remove(0);
			}

			if (iSecurity >= SECURITY || listDeNoeudRetour.size() == 0) {
				if (Parametre.MODE_DEBUG) {
					System.err.println("SECURITY : " + SECURITY + " listDeNoeudOpen.size()=" + listDeNoeudOpen.size()
							+ "  listDeNoeudRetour.size()=" + listDeNoeudRetour.size());
				}
				throw new AetoileException();
			}
		} else {
			throw new AetoileDestinationBlockException();
		}
		return listDeNoeudRetour;
	}

	private boolean isCollision(final Noeud noeud) {
		isCollision = false;
		try {
			Array<Body> bodies = new Array<Body>();
			this.world.getBodies(bodies);
			circleBody.setX(noeud.x);
			circleBody.setY(noeud.y);

			List<Rectangle> rectangleBodies = Base.getInstance().getRectangleBodies();
			for (Rectangle rectangle : rectangleBodies) {
				isCollision = Intersector.overlaps(circleBody, rectangle);
				if (isCollision) {
					break;
				}
			}

			if (isCollisionEntiteConstructible && !isCollision) {
				List<Rectangle> rectangleBodiesAConstruire = Base.getInstance().getRectangleBodiesAConstruire();
				for (Rectangle rectangle : rectangleBodiesAConstruire) {
					isCollision = Intersector.overlaps(circleBody, rectangle);
					if (isCollision) {
						break;
					}
				}
			}
		} catch (GdxRuntimeException e) {
			if (Parametre.MODE_DEBUG) {
				e.printStackTrace();
			}
			isCollision(noeud);
		}
		return isCollision;
	}

	private boolean isCollisionDiagonalBasGauche(final int x, final int y, final int taillePixel) {
		isCollision = false;
		final Noeud noeudGauche = new Noeud(x + taillePixel, y, 0);
		isCollision = isCollision(noeudGauche);
		if (!isCollision) {
			final Noeud noeudBas = new Noeud(x, y - taillePixel, 0);
			isCollision = isCollision(noeudBas);
		}
		return isCollision;
	}

	private Boolean isCollisionDiagonalBasDroite(int xN, int yN, int taillePixel) {
		isCollision = false;
		final Noeud noeudDroite = new Noeud(xN - taillePixel, yN, 0);
		isCollision = isCollision(noeudDroite);
		if (!isCollision) {
			final Noeud noeudBas = new Noeud(xN, yN - taillePixel, 0);
			isCollision = isCollision(noeudBas);
		}
		return isCollision;
	}

	private Boolean isCollisionDiagonalHautDroite(int xN, int yN, int taillePixel) {
		isCollision = false;
		final Noeud noeudDroite = new Noeud(xN - taillePixel, yN, 0);
		isCollision = isCollision(noeudDroite);
		if (!isCollision) {
			final Noeud noeudHaut = new Noeud(xN, yN + taillePixel, 0);
			isCollision = isCollision(noeudHaut);
		}
		return isCollision;
	}

	private boolean isCollisionDiagonalHautGauche(final int x, final int y, final int taillePixel) {
		isCollision = false;
		final Noeud noeudGauche = new Noeud(x + taillePixel, y, 0);
		isCollision = isCollision(noeudGauche);
		if (!isCollision) {
			final Noeud noeudHaut = new Noeud(x, y + taillePixel, 0);
			isCollision = isCollision(noeudHaut);
		}
		return isCollision;
	}

	private static double calculHeuristic(final Noeud objectif, final Noeud noeud) {
		int dx = Math.abs(noeud.x - objectif.x);
		int dy = Math.abs(noeud.y - objectif.y);
		return (dx + dy) + noeud.cout;
	}

	public boolean isCollisionEntiteConstructible() {
		return isCollisionEntiteConstructible;
	}

	public void setCollisionEntiteConstructible(boolean isCollisionEntiteConstructible) {
		this.isCollisionEntiteConstructible = isCollisionEntiteConstructible;
	}

}