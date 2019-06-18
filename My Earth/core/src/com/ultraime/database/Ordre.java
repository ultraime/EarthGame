package com.ultraime.database;

public class Ordre {
	public static int construire = 1;
	public ElementEarth elementEarth;
	public int id;

	public Ordre(final int id, final ElementEarth elementEarth) {
		super();
		this.elementEarth = elementEarth;
		this.id = id;
	}

	public Ordre(final int id) {
		this(id, null);
	}
}
