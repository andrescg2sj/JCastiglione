package org.sj.tools.jcastiglione;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.Point;
import math.geom.Vector2D;

public class ScrollRegion implements ScrollConverter {
	/* superficie total, en unidades lógicas */
	public Dimension areaTotal;
	
	/* !!! ATENCION: No esta claro para qué sirve esto */
	public Dimension areaVisible;
	private int escala = 1;
	/* A ampliación. Unidades: ul/pixel 
	 * ul = unidad lógica
	 */
	private float A = 1;
	/* coordenada lógica o absoluta de la esquina superior izquierda
	 * de la ventana.
	 */
	public Vector2D origenView;
	
	public ScrollRegion(Dimension aT, Dimension aV, int esc, Vector2D o) {
		areaTotal = aT;
		areaVisible = aV;
		setEscala(esc);
		origenView = o;
	}
	
	public float getA() {
		return A;
	}
	
	private void calcA() {
		if(escala < 0) {
			A = Math.abs(escala);
		} else if(escala > 0) {
			A = (float) 1 / escala;
		}
	}
	
	public void setEscala(int e) {
		if(e == 0) e = 1;
		escala = e;
		calcA();
	}
	
	public int getEscala() {
		return escala;
	}
	
	public void aumentar() {
		escala++;
		if(escala == -1) escala = 1;
		calcA();
	}

	public void disminuir() {
		escala--;
		if(escala == 0) escala = -2;
		calcA();
	}
	/* calcula coordenadas absolutas o lógicas de un punto de la ventana */
	public Vector2D ScreenToAbs(Point p) {
		Vector2D u = new Vector2D();
		u.x = origenView.x + (float)p.getX()*A;
		u.y = origenView.y + (float)p.getY()*A;
		return u;
	}
	
	/* calcula coordenadas de la ventana de un punto lógico */
	public Point AbsToScreen(Vector2D u) {
		Point p = new Point();
		p.x = (int) ((u.x - origenView.x)/A);
		p.y = (int) ((u.y - origenView.y)/A);
		return p;
	}

	public Point AbsToScreen(double x, double y) {
		Point p = new Point();
		p.x = (int) ((x - origenView.x)/A);
		p.y = (int) ((y - origenView.y)/A);
		return p;
	}
	
	public Rectangle AbsToScreen(Rectangle2D r) {
		Point p = AbsToScreen(r.getX(), r.getY());
		int w = LogicToPixel(r.getWidth());
		int h = LogicToPixel(r.getHeight());
		return new Rectangle(p.x, p.y, w, h);
	}

	
	/**
	 * transforma una distancida dada en pixels a unidades lógicas.
	 *	@param dist int con la distancia en pixels
	 */
	public double PixelToLogic(int dist) {
		return A*dist;
	}

	/**
	 * transforma una distancida dada en unidades lógicas a pixels.
	 *	@param dist int con la distancia en unidades logicas.
	 */
	public int LogicToPixel(double dist) {
		return (int) (dist/A);
	}

	
	/**
	 * devuelve las coordenadas del origen en píxeles
	 */
	public Point getOrigen() {
	 /* !!ATENCIÓN: Revisar */
		Point p = new Point();
		p.x = (int) (origenView.x/A);
		p.y = (int) (origenView.y/A);
		return p;
	}
	
	public void setOrigen(Vector2D u) {
		origenView.copy(u);
	}

	
	public Rectangle getZonaVisible() {
	/* !!! ATENCIÓN: repasar esta rutina. Podría no estar bien.
	 * Revisar también getOrigen();
	 */
		Point p = getOrigen();
		Rectangle r = new Rectangle((int) origenView.x, (int) origenView.y, (int) (A*areaVisible.width),(int) (A*areaVisible.height));
		return r;
	}
	
	public void setAreaTotal(Dimension d) {
		if(d == null) return;
		areaTotal = d;
	}
	
	public double getAmpliacion() {
		return (double) A;
	}
	

	
}
