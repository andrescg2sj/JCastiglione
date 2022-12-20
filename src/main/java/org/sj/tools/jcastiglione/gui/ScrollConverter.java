package org.sj.tools.jcastiglione.gui;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import math.geom.Vector2D;



/**
 * Convierte coordenadas de pantalla a documento y viceversa
 *
 */
public interface ScrollConverter {
	
	/**
	 * transforma una distancida dada en pixels a unidades lógicas.
	 *	@param dist int con la distancia en pixels
	 */
	public double PixelToLogic(int dist);
	public Vector2D ScreenToAbs(Point p);
	/**
	 * transforma una distancida dada en unidades lógicas a pixels.
	 *	@param dist int con la distancia en unidades logicas.
	 */
	public int LogicToPixel(double dist);
	public Point AbsToScreen(double x, double y);
	public Point AbsToScreen(Vector2D u);
	public Rectangle AbsToScreen(Rectangle2D r);
	
	public Point getOrigen();
	public Rectangle getZonaVisible();
	
	public double getAmpliacion();

}