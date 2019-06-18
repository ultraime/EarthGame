package com.ultraime.game.metier;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import com.ultraime.game.utile.Parametre;

/**
 * Created by Ultraime on 21/09/2018.
 */
public class Temps implements Serializable {
	/**
	 * 
	 */
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;
	public int annee;
	public int mois;
	public int jour;
	public int numeroJour;
	public int heure;
	public int minute;

	/**
	 * @param annee
	 * @param mois
	 * @param jour
	 * @param numeroJour
	 * @param heure
	 * @param minute
	 */
	public Temps(int annee, int mois, int jour, int numeroJour, int heure, int minute) {
		super();
		this.annee = annee;
		this.mois = mois;
		this.jour = jour;
		this.numeroJour = numeroJour;
		this.heure = heure;
		this.minute = minute;
	}

	/**
	 * @param temp
	 */
	public Temps(final Temps temp) {
		this.annee = temp.annee;
		this.mois = temp.mois;
		this.jour = temp.jour;
		this.numeroJour = temp.numeroJour;
		this.heure = temp.heure;
		this.minute = temp.minute;
	}

	public StringBuilder showTemp() {
		StringBuilder sb = new StringBuilder();
		// sb.append(showJour());
		sb.append(" ");
		sb.append(this.numeroJour);
		sb.append(" ");
		sb.append(showMois());
		sb.append(" ");
		sb.append(showAnnee());
		sb.append(" ");
		sb.append(showHeure());
		sb.append(":");
		sb.append(showMinute());
		return sb;
	}

	public String showMinute() {
		String retour = null;
		if (this.minute < 10) {
			retour = "0" + Integer.toString(this.minute);
		} else {
			retour = Integer.toString(this.minute);
		}
		return retour;
	}

	private String showHeure() {
		String retour = null;
		if (this.heure < 10) {
			retour = "0" + Integer.toString(this.heure);
		} else {
			retour = Integer.toString(this.heure);
		}
		return retour;
	}

	public String showJour() {
		String retour = null;
		switch (this.jour) {
		case 0:
			retour = Parametre.bundle.get("txt.temps.lundi");
			break;
		case 1:
			retour = Parametre.bundle.get("txt.temps.mardi");
			break;
		case 2:
			retour = Parametre.bundle.get("txt.temps.mercredi");
			break;
		case 3:
			retour = Parametre.bundle.get("txt.temps.jeudi");
			break;
		case 4:
			retour = Parametre.bundle.get("txt.temps.vendredi");
			break;
		case 5:
			retour = Parametre.bundle.get("txt.temps.samedi");
			break;
		case 6:
			retour = Parametre.bundle.get("txt.temps.dimanche");
			break;

		}

		return retour;
	}

	public String showMois() {
		String retour = null;
		switch (this.mois) {
		case 0:
			retour = Parametre.bundle.get("txt.temps.janvier");
			break;
		case 1:
			retour = Parametre.bundle.get("txt.temps.fevrier");
			break;
		case 2:
			retour = Parametre.bundle.get("txt.temps.mars");
			break;
		case 3:
			retour = Parametre.bundle.get("txt.temps.avril");
			break;
		case 4:
			retour = Parametre.bundle.get("txt.temps.mai");
			break;
		case 5:
			retour = Parametre.bundle.get("txt.temps.juin");
			break;
		case 6:
			retour = Parametre.bundle.get("txt.temps.juillet");
			break;
		case 7:
			retour = Parametre.bundle.get("txt.temps.aout");
			break;
		case 8:
			retour = Parametre.bundle.get("txt.temps.septembre");
			break;
		case 9:
			retour = Parametre.bundle.get("txt.temps.octobre");
			break;
		case 10:
			retour = Parametre.bundle.get("txt.temps.novembre");
			break;
		case 11:
			retour = Parametre.bundle.get("txt.temps.decembre");
			break;
		}

		return retour;
	}

	public String showAnnee() {
		String retour = null;
		if (this.annee < 1000) {
			if (this.annee < 100) {
				if (this.annee < 10) {
					retour = "000" + Integer.toString(this.annee);
				} else {
					retour = "00" + Integer.toString(this.annee);
				}
			} else {
				retour = "0" + Integer.toString(this.annee);
			}
		} else {
			retour = Integer.toString(this.annee);
		}
		return retour;
	}

	/**
	 * @param tps
	 * @return -1 : Le temps est plus petit, 0 ils sont égaux, 1 le temp est
	 *         plus grand
	 */
	public int compare(Temps tps) {
		int retour = 0;
		if (this.annee > tps.annee) {
			retour = 1;
		} else if (this.annee < tps.annee) {
			retour = -1;
		} else {
			// this.annee = tps.annee
			if (this.mois > tps.mois) {
				retour = 1;
			} else if (this.mois < tps.mois) {
				retour = -1;
			} else {
				// this.mois = tps.mois
				if (this.numeroJour > tps.numeroJour) {
					retour = 1;
				} else if (this.numeroJour < tps.numeroJour) {
					retour = -1;
				} else {
					// this.jour = tps.jour
					if (this.heure > tps.heure) {
						retour = 1;
					} else if (this.heure < tps.heure) {
						retour = -1;
					} else {
						// this.heure = tps.heure
						if (this.minute > tps.minute) {
							retour = 1;
						} else if (this.minute < tps.minute) {
							retour = -1;
						} else {
							// this.minute = tps.minute
							retour = 0;
						}
					}
				}
			}
		}
		return retour;
	}

	public void addMinute(final int nb) {
		this.minute = this.minute + 1;
		if (this.minute >= 60) {
			this.minute = 0;
			addHeure(1);
		}
	}

	public void addHeure(final int nb) {
		this.heure = this.heure + 1;
		if (this.heure >= 24) {
			this.heure = 0;
			addJour(1);
		}

	}

	public void addJour(final int nb) {
		this.jour = this.jour + 1;
		this.numeroJour = this.numeroJour + 1;
		this.jour = 0;
		// si février
		if (this.mois == 1 && this.numeroJour > 28) {
			this.numeroJour = 1;
			addMois(1);
		} else if (this.mois < 7) {
			// Si mois < que aout, les mois paire sont de 31J
			if ((this.mois % 2) == 0 && this.numeroJour > 31) {
				this.numeroJour = 1;
				addMois(1);
			} else if (this.numeroJour > 30 && (this.mois % 2) != 0) {
				this.numeroJour = 1;
				addMois(1);
			}
		} else if (this.mois > 6) {
			// Si mois > que Juillet, les mois Impaire sont de 31J
			if ((this.mois % 2) != 0 && this.numeroJour > 31) {
				this.numeroJour = 1;
				addMois(1);
			} else if (this.numeroJour > 30 && (this.mois % 2) == 0) {
				this.numeroJour = 1;
				addMois(1);
			}
		}
	}

	public void addMois(final int nb) {
		this.mois = this.mois + 1;
		if (this.mois >= 12) {
			this.mois = 0;
			addAnnee(1);
		}
	}

	public void addAnnee(final int nb) {
		this.annee = this.annee + 1;
	}

	public void addTemps(final Temps tps) {
		for (int i = 0; i < tps.minute; i++) {
			this.addMinute(1);
		}
		for (int i = 0; i < tps.heure; i++) {
			this.addHeure(1);
		}
		for (int i = 0; i < tps.jour; i++) {
			this.addJour(1);
		}
		for (int i = 0; i < tps.mois; i++) {
			this.addMois(1);
		}
		for (int i = 0; i < tps.annee; i++) {
			this.addAnnee(1);
		}
	}

	@Override
	public void write(Json json) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		// TODO Auto-generated method stub
		
	}

}