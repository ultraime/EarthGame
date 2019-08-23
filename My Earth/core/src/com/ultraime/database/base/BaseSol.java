package com.ultraime.database.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.ultraime.database.ElementEarth;

public class BaseSol implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<ElementEarth> elementEarthSol;
	
	public BaseSol(){
		this.elementEarthSol = new ArrayList<>();
	}
	
	public List<ElementEarth> getElementEarthSol() {
		return elementEarthSol;
	}

	public void setElementEarthSol(List<ElementEarth> elementEarthSol) {
		this.elementEarthSol = elementEarthSol;
	}
	

}
