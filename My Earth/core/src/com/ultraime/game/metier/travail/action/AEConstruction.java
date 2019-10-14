package com.ultraime.game.metier.travail.action;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.ultraime.database.base.Base;
import com.ultraime.database.entite.ElementEarth;
import com.ultraime.database.entite.Materiau;
import com.ultraime.game.entite.EntiteVivante;
import com.ultraime.game.metier.ElementEarthService;
import com.ultraime.game.metier.Temps;
import com.ultraime.game.metier.TileMapService;
import com.ultraime.game.metier.pathfinding.Aetoile;
import com.ultraime.game.metier.pathfinding.AetoileDestinationBlockException;
import com.ultraime.game.metier.pathfinding.AetoileException;
import com.ultraime.game.metier.pathfinding.Noeud;
import com.ultraime.game.utile.Parametre;
import com.ultraime.music.MusicManager;

public class AEConstruction extends ActionEntite {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public List<ElementEarth> elementAconstruires;
	private Temps tempsConstruction;

	public AEConstruction(int priorite) {
		super(priorite);
		this.elementAconstruires = new ArrayList<>();
	}

	@Override
	public boolean doAction(Body body, World world, World worldAffichage) {
		boolean isActionEnd = false;
		// on regare si le personnage doit se deplacer.
		boolean isDeplacementEnd = doDeplacement(body, worldAffichage);
		final EntiteVivante ev = (EntiteVivante) body.getUserData();
		ElementEarth elem = elementAconstruires.get(0);
		if (isDeplacementEnd) {
			// déplacement Fini
			if (tempsConstruction == null) {
				// on viens juste de fini le déplacement.
				final Temps tps = Base.getInstance().getTemps();
				tempsConstruction = new Temps(tps);
				int lvlConstruction = ev.habiliter.construction;
				int temps = 11 - lvlConstruction;
				tempsConstruction.addMinute(temps);
				MusicManager.getInstance().construire(elem);
			} else {
				// on regarde si le temps de l'action est ok.
				if (Base.getInstance().getTemps().compare(tempsConstruction) == 1) {

					if (elem.type.equals(ElementEarth.action)) {
						ElementEarth earthNature = null;
						if (ElementEarth.actionDeDestruction.contains(elem.nom)) {
							if (ElementEarth.couper.equals(elem.nom)) {
								// si on coupe, on ne supprime pas les éléments dérriere. (un arbre peut être
								// sur plusieur case.
								earthNature = Base.getInstance().recupererElementEarth(elem.x, elem.y,
										ElementEarth.nature);
								TileMapService.getInstance().detruireItem(earthNature);
							} else {
								earthNature = Base.getInstance().recupererElementEarth(elem.x, elem.y);
							}

							ElementEarthService.genererElement(earthNature);
							// on supprime l'action et l'item (si ce n'est pas un type nature.Le type natrue
							// est supprimé unitairement juste au dessus
							TileMapService.getInstance().detruireItem(elem);

						}
					} else {
						// on ajoute le matériaux à l'objet.

						for (int i = 0; i < elem.materiaux_requis.size(); i++) {
							final Materiau materiau = elem.materiaux_requis.get(i);
							final List<ElementEarth> listElementInventaire = ev.inventaire
									.recupererAllElementByNom(materiau.nom);
							for (int j = 0; j < listElementInventaire.size(); j++) {
								materiau.nombreActuel += 1;
								listElementInventaire.remove(i);
								if (materiau.nombreActuel == materiau.nombreRequis) {
									break;
								}
							}
						}

						// aprés avoir ajouter les matériaux, on vérifie que
						// tout est okk
						boolean peutEtreConstruit = true;
						for (int i = 0; i < elem.materiaux_requis.size(); i++) {
							final Materiau materiau = elem.materiaux_requis.get(i);
							if (materiau.nombreActuel != materiau.nombreRequis) {
								peutEtreConstruit = false;
								break;
							}
						}
						if (peutEtreConstruit) {
							TileMapService.getInstance().construireItem(elem);
							Base.getInstance().retirerRectangleConstructible(elem.x, elem.y);
						} else {
							ajouterElementAconstruire(elem);
						}

					}
					elementAconstruires.remove(0);
					tempsConstruction = null;
					if (elementAconstruires.size() == 0) {
						isActionEnd = true;
					}
				}
			}
		}

		if (ev.getListeDeNoeudDeplacement().size() == 0 && !isActionEnd) {
			initAction(worldAffichage, body);
		}

		return isActionEnd;
	}

	@Override
	public void initAction(World world, Body body) {
		EntiteVivante ev = (EntiteVivante) body.getUserData();
		if (ev.getListeDeNoeudDeplacement().size() == 0) {
			final int xDepart = Math.round(body.getPosition().x);
			final int yDepart = Math.round(body.getPosition().y);
			Noeud noeudDepart = new Noeud(xDepart, yDepart, 0);
			// on essaie d'aller en bas sinon à gauche sinon en haut, sinon à
			// droite sinon à droite. si ce n'est pas possible on annule
			// l'action
			ElementEarth elementAconstruire = elementAconstruires.get(0);
			int xElementAconstruire = elementAconstruire.x;
			int yElementAconstuire = elementAconstruire.y;
			Aetoile aetoile = new Aetoile(world, body);
			for (int i = 0; i < 4; i++) {
				try {
					int decalageX = 0;
					int decalageY = 0;
					if (i == 0) {
						decalageY = -1;
						yElementAconstuire = elementAconstruire.y - elementAconstruire.elementY(ElementEarth.min);
					} else if (i == 1) {
						decalageX = -1;
						xElementAconstruire = elementAconstruire.x - elementAconstruire.elementX(ElementEarth.min);
					} else if (i == 2) {
						decalageY = 1;
						yElementAconstuire = elementAconstruire.y + elementAconstruire.elementY(ElementEarth.max);
					} else if (i == 3) {
						decalageX = 1;
						xElementAconstruire = elementAconstruire.x + elementAconstruire.elementX(ElementEarth.max);
						;
					}

					final Noeud noeudDestination = new Noeud((xElementAconstruire + decalageX),
							(yElementAconstuire + decalageY), 0);

					aetoile.setCollisionEntiteConstructible(true);
					ArrayDeque<Noeud> cheminPlusCourt = aetoile.cheminPlusCourt(noeudDestination, noeudDepart,
							Parametre.AETOILE_BASE_2);
					ev.setListeDeNoeudDeplacement(cheminPlusCourt);
					// si c'est ok on sort de la boucle
					break;
				} catch (AetoileException e) {
					if (i == 3) {
						System.err.println("erreur classe AECONSTRUCTION : chemin non trouvé");
					}

				} catch (AetoileDestinationBlockException e) {
					if (i == 3) {
						System.err.println("erreur classe AECONSTRUCTION : chemin non trouvé");
					}
				}
			}
		}
	}

	public void ajouterElementAconstruire(final ElementEarth elementAconstruire) {
		elementAconstruires.add(elementAconstruire);
	}

}
