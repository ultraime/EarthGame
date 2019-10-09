package com.ultraime.database.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ultraime.database.entite.ElementEarth;

public class BaseNature implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<ElementEarth> elementEarthNature;
	private List<ElementEarth> elementEarthEau;

	public BaseNature() {
		this.elementEarthNature = new ArrayList<ElementEarth>();
		this.elementEarthEau = new ArrayList<ElementEarth>();
	}

	public List<ElementEarth> getElementEarthNature() {
		return elementEarthNature;
	}

	public void setElementEarthNature(List<ElementEarth> elementEartNature) {
		this.elementEarthNature = elementEartNature;
	}

	public List<ElementEarth> getElementEarthEau() {
		return elementEarthEau;
	}

	public void setElementEarthEau(List<ElementEarth> elementEarthEau) {
		this.elementEarthEau = elementEarthEau;
	}

}
