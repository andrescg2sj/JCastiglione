package org.sj.tools.jcastiglione.figure;

import java.awt.Point;
import java.awt.Color;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import math.geom.Vector2D;

import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

import org.sj.tools.jcastiglione.Drawable;

public interface Figura extends Drawable {
	public static final int FIGURA_RECTANGULO = 101;
	public static final int FIGURA_POLIGONO = 201;
	public static final int FIGURA_IMAGEN = 301;
	public static final int FIGURA_LINEA = 401;
	public static final int FIGURA_TEXTO = 501;
	

		
	public int tipoFigura();
	
	/**
	 *	devuelve el centro
	 * @return centro de la figura
	 */
	public Vector2D getCentro();
	
	
	
	/**
	 *	indica si el fondo es visible
	 *
	 */
	public boolean fondoVisible();
	public boolean lineaVisible();
	public void setVerFondo(boolean v);

	public void setVerLinea(boolean v);
	
	public float getGrosorLinea();
	public void setGrosorLinea(float g);
	
	public void setZ(float _z);

	public float getZ();
	public boolean esVisible();
	public Color getColorFondo();
	public Color getColorLinea();

	public void setColorFondo(Color c);
	public void setColorLinea(Color c);

	/**
	 * Mover el centro al punto c
	 */
	//public void cambiarCentro(Vector2D c);
	/**
	 * Mover el centro según el vector u ( centro = centro + u )
	 */
	//public void moverCentro(Vector2D u);
	
	/**
	 * averigua si el punto v está dentro de la figura
	 */
	public boolean puntoDentro(Vector2D v);
	

	/**
	 * Desplazar según el vector u
	 */
	public void mover(Vector2D u);
	
	public Vector2D getCentro(boolean anim, int time);
	//public void cambiarCentro(Vector2D c, boolean anim, int time);
	//public void moverCentro(Vector2D c,boolean anim, int time);
	public boolean puntoDentro(Vector2D v, boolean anim,int time);
	public void mover(Vector2D u,boolean anim, int time);
	
	
	/**
	 *	reescala la figura para que tenga el tamaño d
	 */
	public void reescalar(Dimension2D d);

	/**
	 *	devuelve el tamaño de la figura
	 */
	public Dimension2D getSize();
	
	/**
	 *	devuelve los límites
	 */
	public Rectangle2D getBounds();
	
	public Rectangle2D getBounds(boolean anim, int time);
	
	/**
	 * devuelve el tamaño en bytes que necesita la figura para
	 * ser guardada en un fichero
	 */
	public int getByteSize();
	
	/**
	 * devuelve una cadena de bytes que representa la figura
	 * para guardarla en un fichero
	 */
	public byte[] getBytes();
	
	public void guardarFigura(DataOutputStream dos) throws IOException;
}