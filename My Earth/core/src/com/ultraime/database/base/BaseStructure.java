package com.ultraime.database.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ultraime.database.ElementEarth;

public class BaseStructure implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<ElementEarth> elementEarthStructure;

	public BaseStructure() {
		this.elementEarthStructure = new ArrayList<>();
	}
	
	public List<ElementEarth> getElementEarthStructure() {
		return elementEarthStructure;
	}

	public void setElementEarthStructure(List<ElementEarth> elementEarthStructure) {
		this.elementEarthStructure = elementEarthStructure;
	}

}
