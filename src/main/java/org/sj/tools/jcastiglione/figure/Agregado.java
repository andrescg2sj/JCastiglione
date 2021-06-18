package org.sj.tools.jcastiglione.figure;

import java.awt.geom.Dimension2D;
import java.awt.Graphics;
import math.geom.Vector2D;
import java.awt.geom.Rectangle2D;
import java.util.Vector;

import org.sj.tools.jcastiglione.ScrollConverter;

import java.io.DataOutputStream;

public class Agregado extends FiguraSimple {


	/* cache para agilizar cÃ¡lculos */
	/** rectÃ¡ngulo que contiene al polÃ­gono */
	private Rectangle2D rect;

	private class Nodo {
		FgPolygon pol;
		int padre;
		
		public Nodo(FgPolygon p, int pa) {
			pol = p;
			padre = pa;
		}
	}
	
	private Vector polis = new Vector(10, 5);
	
	public Agregado(FgPolygon p) {
		super(p.getCentro());
		Nodo n = new Nodo(p, -1);
		polis.add(n);
		rect = p.getBounds();
	}
	

	public void guardar(DataOutputStream dos) {
	}
	
	public byte[] getBytes() {
		return null;
	}
	
	public void reescalar(Dimension2D d) {
	}
	
	public int getByteSize() {
		return 0;
	}
	
	void calcularTamano() {
		float minx, miny, maxx, maxy;
		minx = maxx = 0;
		miny = maxy = 0;
		rect = new Rectangle2D.Float(minx, miny, maxx-minx, maxy-miny);
	}
	
	/**
	 * Devuelve el mÃ­nimo rectÃ¡ngulo que contiene todos los puntos del polÃ­gono
	 */
	public Rectangle2D getBounds() {
		return rect;
	}
	
	public Rectangle2D getBounds(boolean anim, int time) {
		throw new UnsupportedOperationException(this.getClass().toString() 
		+ "." + Thread.currentThread().getStackTrace()[0].getMethodName());
		
	}
	
	
	public Dimension2D getSize() {
		return new DoubleDim2D(rect.getWidth(), rect.getHeight());
	}
	
	public void mover(Vector2D v) {
	}
	
	public boolean puntoDentro(Vector2D v) {
		return false;
	}
	

	
	public void moverTmd(Vector2D u, int time) {
		throw new UnsupportedOperationException(this.getClass().toString() 
		+ "." + Thread.currentThread().getStackTrace()[0].getMethodName());
		
	}
	
	public boolean puntoDentroTmd(Vector2D u, int time) {
		throw new UnsupportedOperationException(this.getClass().toString() + ".puntoDentroTmd");
	}

	
	public int tipoFigura() {
		return FIGURA_AGREGADO;
	}
	
	public void draw(Graphics g, ScrollConverter sc) {
	}
}