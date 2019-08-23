package com.ultraime.database.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ultraime.database.ElementEarth;

public class BaseMeuble implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<ElementEarth> elementEarthMeubles;
	
	public BaseMeuble() {
		this.elementEarthMeubles = new ArrayList<>();
	}

	public List<ElementEarth> getElementEarthMeubles() {
		return elementEarthMeubles;
	}

	public void setElementEarthMeubles(List<ElementEarth> elementEarthMeubles) {
		this.elementEarthMeubles = elementEarthMeubles;
	}
	
	
	
}
