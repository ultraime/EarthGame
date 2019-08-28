package com.ultraime.game.metier.pathfinding;

import java.io.Serializable;

public class Noeud implements Serializable, Comparable<Noeud> {
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