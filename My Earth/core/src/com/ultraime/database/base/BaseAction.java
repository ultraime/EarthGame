package com.ultraime.database.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ultraime.database.ElementEarth;

public class BaseAction implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<ElementEarth> elementEarthAction;

	public BaseAction() {
		this.elementEarthAction = new ArrayList<ElementEarth>();
	}

	public List<ElementEarth> getElementEarthAction() {
		return elementEarthAction;
	}

	public void setElementEarthAction(List<ElementEarth> elementEartAction) {
		this.elementEarthAction = elementEartAction;
	}

}
