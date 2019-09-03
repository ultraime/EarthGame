package com.ultraime.database.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ultraime.database.ElementEarth;

public class BaseNature implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<ElementEarth> elementEarthNature;

	public BaseNature() {
		this.elementEarthNature = new ArrayList<ElementEarth>();
	}

	public List<ElementEarth> getElementEarthNature() {
		return elementEarthNature;
	}

	public void setElementEarthNature(List<ElementEarth> elementEartNature) {
		this.elementEarthNature = elementEartNature;
	}

}
