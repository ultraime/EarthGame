package com.ultraime.database.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ultraime.database.ElementEarth;

public class BaseObjetSol implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<ElementEarth> elementEarthsObjetSol;

	public BaseObjetSol() {
		this.elementEarthsObjetSol = new ArrayList<ElementEarth>();
	}

	public List<ElementEarth> getElementEarthsObjetSol() {
		return elementEarthsObjetSol;
	}

	public void setElementEarthsObjetSol(List<ElementEarth> elementEarthsObjetSol) {
		this.elementEarthsObjetSol = elementEarthsObjetSol;
	}

}
