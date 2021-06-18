package org.sj.tools.jcastiglione.figure;

import java.awt.Point;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import math.geom.Vector2D;

import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

import org.sj.tools.jcastiglione.ScrollConverter;

/**
 * Linea
 * @since 1.0
 */
public class Linea extends FiguraSimple {

	/* extremos */

	Vector2D a, b;
	/**
	 * Constructor 
	 * @param _c Vector2D primer punto
	 */
	public Linea(Vector2D _a, Vector2D _b) {
		super(new Vector2D( (_a.x+_b.x)/2, (_a.y+_b.y/2) ) );
		a = _a;
		b = _b;
	}

	/**
	 * Constructor que genera la línea a partir de un archivo guardado
	 * @param dis DataInputStream de donde se obtienen los datos
	 */
	public Linea(DataInputStream dis) throws IOException {
		super(dis);
		a = new Vector2D(dis.readFloat(), dis.readFloat());
		b = new Vector2D(dis.readFloat(), dis.readFloat());
	}


	
	public int tipoFigura() {
		return FIGURA_LINEA;
	}


	
	/**
	 * averigua si el punto v está dentro de la figura
	 */
	public boolean puntoDentro(Vector2D v) {
		/* máximo error absoluto que se puede cometer al pulsar sobre la linea*/
		final int error = 2;
		Vector2D r = new Vector2D(v.x - a.x, v.y - a.y);
		Vector2D d = new Vector2D(b.x - a.x, b.y - a.y); // direccion
		Vector2D n = new Vector2D(a.y - b.y, b.x - a.x); // normal
		double mod_d = d.module();
		double mod_r = r.module();
		
		if (mod_r <= error) {
			return true;
		}
		
		if (mod_d == 0) {
			return false;
		}

		double px = d.prodEsc(r)/mod_d;
		double py = n.prodEsc(r)/mod_d;
		if(px < 0 || px > mod_d) {
			return false;
		}
		return (Math.abs(py) <= error);
	}

	/**
	 * Desplazar según el vector u
	 */
	public void mover(Vector2D u) {
		a.add(u);
		b.add(u);
	}
	
	
	public void moverTmd(Vector2D u, int time) {
		throw new UnsupportedOperationException(this.getClass().toString() + ".moverTmd");
	}
	
	public boolean puntoDentroTmd(Vector2D u, int time) {
		throw new UnsupportedOperationException(this.getClass().toString() + ".puntoDentroTmd");
	}
	
	public Rectangle2D getBounds(boolean anim, int time) {
		throw new UnsupportedOperationException(this.getClass().toString() 
		+ "." + Thread.currentThread().getStackTrace()[0].getMethodName());
		
	}
	

	
	/**
	 *	reescala la figura para que tenga el tamaño d
	 */
	public void reescalar(Dimension2D d) {
		b.x = a.x + (float) d.getWidth();
		b.y = a.y + (float) d.getHeight();
	}

	/**
	 *	devuelve el tamaño de la figura
	 */
	public Dimension2D getSize() {
		return new DoubleDim2D(Math.abs(b.x - a.x), Math.abs(b.y - a.y));
	}
	
	/**
	 *	devuelve los límites
	 */
	public Rectangle2D getBounds() {
		Rectangle2D r = new Rectangle2D.Float();
		double minx = Math.min(a.x, b.x);
		double miny = Math.min(a.y, b.y);
		double w = Math.abs(a.x - b.x);
		double h = Math.abs(a.y - b.y);
		r.setRect(minx, miny, w, h);
		return r;
	}
	
	/**
	 * devuelve el tamaño en bytes que necesita la figura para
	 * ser guardada en un fichero
	 */
	public int getByteSize() {
		return 0;
	}
	
	/**
	 * devuelve una cadena de bytes que representa la figura
	 * para guardarla en un fichero
	 */
	public byte[] getBytes() {
		return null;
	}
	
	/**
	 * Guarda los datos de cada tipo de figura
	 */	
	protected void guardar(DataOutputStream dos) throws IOException {
		dos.writeFloat(a.x);
		dos.writeFloat(a.y);
		dos.writeFloat(b.x);
		dos.writeFloat(b.y);
	}
	
	public void draw(Graphics g, ScrollConverter sc) {
		if(!esVisible()) return;
		Point pa = sc.AbsToScreen(a);
		Point pb = sc.AbsToScreen(b);
		
		if(lineaVisible()) {
			g.setColor(getColorLinea());
			g.drawLine(pa.x, pa.y, pb.x, pb.y);
		}

	}


}