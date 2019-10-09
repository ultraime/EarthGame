package com.ultraime.game.metier.travail.action;

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
						if (ElementEarth.actionDeDestruction.contains(elem.nom)) {
							if (elem.nom.equals(ElementEarth.couper)) {
								// quand on veut couper, on récupére un objet de
								// type nature.
								// peut nous donner une ressource quand il est
								// coupé
								// TODO gestion couper.
								final ElementEarth earthNature = Base.getInstance().recupererElementEarth(elem.x,
										elem.y);
								ElementEarthService.genererElement(earthNature);
							}
							TileMapService.getInstance().detruireItem(elem);

						}
					} else {
						// on ajoute le matériaux à l'objet.

						for (int i = 0; i < elem.materiaux_requis.size(); i++) {
							final Materiau materiau = elem.materiaux_requis.get(i);
							final List<ElementEarth> listElementInventaire = ev.inventaire
									.recupererAllElementByNom(materiau.nom);
							for (int j = 0; j < listElementInventaire.size(); j++) {
								materiau.nombreActuel = +1;
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
		// TODO a décommenter ?
		// MetierConstructeur.verifierAccessibilite(false,
		// elementAconstruires.get(0), (EntiteVivante) body.getUserData());
	}

	public void ajouterElementAconstruire(final ElementEarth elementAconstruire) {
		elementAconstruires.add(elementAconstruire);
	}

}
