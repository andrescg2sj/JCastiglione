package org.sj.tools.jcastiglione.figure;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Color;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;

import math.geom.Vector2D;

import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

import org.sj.tools.jcastiglione.ScrollConverter;
import org.sj.tools.jcastiglione.animation.AnimableVector2D;
import org.sj.tools.jcastiglione.animation.InterVector2D;

class DoubleDim2D extends Dimension2D {
	double w;
	double h;

	public DoubleDim2D(double _w, double _h) {
		w = _w;
		h = _h;
	}
	
	public double getWidth() {
		return w;
	}
	
	public double getHeight() {
		return h;
	}
	
	public void setSize(double _w, double _h) {
		w = _w;
		h = _h;
	}

}


/**
 * Figura de dibujo vectorial.
 * @since 1.0
 */
public abstract class FiguraSimple implements Figura {
	public static final int FIGURA_RECTANGULO = 101;
	public static final int FIGURA_POLIGONO = 201;
	public static final int FIGURA_IMAGEN = 301;
	public static final int FIGURA_LINEA = 401;
	public static final int FIGURA_AGREGADO = 501;

	
	/** punto de anclaje de la figura */
	//private Vector2D centro;
	private AnimableVector2D centro;
	
	/** indica si el fondo es visible (true) o transparente (false) */
	private boolean verFondo = true;

	/** indica si el borde es visible (true) o transparente (false) */
	private boolean verLinea = true;
	
	/** indica si la figura es visible (true) o invisible (false) */
	private boolean visible = true;
	
	/** color de fondo */
	private Color fondo = null;
	
	/** color del borde */
	private Color linea = null;
	
	/** grosor de lÃ­nea */
	private float grosor = 1;
	
	/** orden */
	private float z = 0;

	/**
	 * Constructor 
	 */
	public FiguraSimple() {
		centro = new AnimableVector2D(new Vector2D());
		fondo = Color.WHITE;
		linea = Color.BLACK;
	}


	/**
	 * Constructor 
	 * @param _c Vector2D con el centro 
	 */
	public FiguraSimple(Vector2D _c) {
		centro = new AnimableVector2D(_c);
		fondo = Color.WHITE;
		linea = Color.BLACK;
	}
	

	/**
	 * Constructor que genera la figura a partir de un archivo guardado
	 * @param dis DataInputStream de donde se obtienen los datos
	 */
	public FiguraSimple(DataInputStream dis) throws IOException {
		/* el tipo NO se lee en el constructor */

		/* centro */
		float x = dis.readFloat();
		float y = dis.readFloat();
		centro = new AnimableVector2D(new Vector2D(x, y));
		
		/* visibilidad */
		verFondo = dis.readBoolean();
		verLinea = dis.readBoolean();
		visible = dis.readBoolean();
		
		/* colores */
		int rgb = dis.readInt();
		fondo = new Color(rgb);
		rgb = dis.readInt();
		linea = new Color(rgb);
	}
	
		
	public abstract int tipoFigura();
	
	/**
	 *	devuelve el centro
	 * @return centro de la figura
	 */
	public Vector2D getCentro() {
		return centro.getDefaultValue();
	}
	
	/**
	 *	indica si el fondo es visible
	 *
	 */
	public boolean fondoVisible()
	{
		return verFondo;
	}
	
	public boolean lineaVisible()
	{
		return verLinea;
	}
	
	public void setVerFondo(boolean v) {
		verFondo = v;
	}

	public void setVerLinea(boolean v) {
		verLinea = v;
	}
	
	public float getGrosorLinea() {
		return grosor;
	}
	
	public void setGrosorLinea(float g) {
		grosor = g;
	}
	
	public void setZ(float _z) {
		z = _z;
	}

	public float getZ() {
		return z;
	}

	public boolean esVisible()
	{
		return visible;
	}
	
	public Color getColorFondo()
	{
		return fondo;
	}
	
	public Color getColorLinea()
	{
		return linea;
	}

	public void setColorFondo(Color c)
	{
		fondo = c;
	}
	
	public void setColorLinea(Color c)
	{
		linea = c;
	}

	/**
	 * Mover el centro al punto c
	 */
	public void cambiarCentro(Vector2D c) {
		centro.setDefaultValue(new InterVector2D(c));
	}

	/**
	 * Mover el centro segÃºn el vector u ( centro = centro + u )
	 */
	public void moverCentro(Vector2D u) {
		Vector2D c = centro.getDefaultValue();
		c.add(u);// TODO: referencias
		centro.setDefaultValue(new InterVector2D(c));
	}
	
	/**
	 * averigua si el punto v estÃ¡ dentro de la figura
	 */
	abstract public boolean puntoDentro(Vector2D v);

	/**
	 * Desplazar segÃºn el vector u
	 */
	abstract public void mover(Vector2D u);
	
	/**
	 *	reescala la figura para que tenga el tamaÃ±o d
	 */
	public abstract void reescalar(Dimension2D d);

	/**
	 *	devuelve el tamaÃ±o de la figura
	 */
	public abstract Dimension2D getSize();
	
	/**
	 *	devuelve los lÃ­mites
	 */
	public abstract Rectangle2D getBounds();
	
	/**
	 * devuelve el tamaÃ±o en bytes que necesita la figura para
	 * ser guardada en un fichero
	 */
	public abstract int getByteSize();
	
	/**
	 * devuelve una cadena de bytes que representa la figura
	 * para guardarla en un fichero
	 */
	public abstract byte[] getBytes();
	
	/**
	 * Guarda los datos de cada tipo de figura
	 */	
	protected abstract void guardar(DataOutputStream dos) throws IOException;

	public void guardarFigura(DataOutputStream dos) throws IOException {
		/*	tipo figura */
		dos.writeInt(tipoFigura());

		/* centro */
		Vector2D c = centro.getDefaultValue();
		dos.writeFloat(c.x);
		dos.writeFloat(c.y);
		
		/* visibilidad */
		dos.writeBoolean(verFondo);
		dos.writeBoolean(verLinea);
		dos.writeBoolean(visible);
		
		/* colores */
		dos.writeInt(fondo.getRGB());
		dos.writeInt(linea.getRGB());
		
		/* guardar el resto */
		guardar(dos);
	}
	
	
	//abstract void cambiarCentroTmd(Vector2D c, int time);
	//abstract void moverCentroTmd(Vector2D c, int time);
	abstract boolean puntoDentroTmd(Vector2D c, int time);
	abstract void moverTmd(Vector2D c, int time);
	
	public Vector2D getCentro(boolean anim, int time)
	{
		if(!anim) return getCentro();
		return centro.getValue(time);
	}
	
	
	/*public void cambiarCentro(Vector2D c, boolean anim,int time)
	{
		if(!anim) cambiarCentro(c);
		cambiarCentroTmd(c, time);
	}
	
	public void moverCentro(Vector2D u, boolean anim, int time)
	{
		if(!anim) moverCentro(u);
		
		moverCentroTmd(u, time);
	}*/
	
	public boolean puntoDentro(Vector2D v,boolean anim, int time)
	{
		if(!anim) return puntoDentro(v);
		
		return puntoDentroTmd(v,time);
	}
	
	public void mover(Vector2D u, boolean anim,int time)
	{
		if(!anim) mover(u);
		moverTmd(u,time);
	}
	
	public void draw(Graphics g, ScrollConverter sr, int time)
	{
		//TODO: animar
		draw(g, sr);
	}


}