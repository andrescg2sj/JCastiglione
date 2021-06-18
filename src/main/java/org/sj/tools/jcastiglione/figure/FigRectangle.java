package org.sj.tools.jcastiglione.figure;

import math.geom.Vector2D;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;
import java.awt.Dimension;
import java.awt.Point;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

import org.sj.tools.jcastiglione.ScrollConverter;
import org.sj.tools.jcastiglione.io.load.AnimRectLoader;


public class FigRectangle extends FigRectBounded {

	

	/**
	*
	* @param tl top-left
	* @param br bottom-right
	*/
	public FigRectangle(Vector2D tl, Vector2D br) {
		super(middle(tl,br), containerRect(tl, br));
		
	}
	
	public FigRectangle(float x, float y, float width, float height) {
		super(x,y,width,height);
		
	}

	
	public FigRectangle(FigRectangle fr) {
		super(fr);
	}
	

	/**
	 * Constructor que genera el rectÃ¡ngulo a partir de un archivo guardado
	 * @param dis DataInputStream de donde se obtienen los datos
	 */
	public FigRectangle(DataInputStream dis) throws IOException {
		super(dis);
		
		float x = dis.readFloat();
		float y = dis.readFloat();
		float w = dis.readFloat();
		float h = dis.readFloat();
		Rectangle2D tr = new Rectangle2D.Float(x, y, w, h);
		rect.setDefaultValue(tr);
	}

	public FigRectangle(Rectangle2D r) {
		super(r);
	}

	public int tipoFigura() {
		return FIGURA_RECTANGULO;
	}

	public void draw(Graphics g, ScrollConverter sc) {
		if(!esVisible()) return;
		Rectangle2D tr = rect.getDefaultValue();
		Point tl = sc.AbsToScreen(new Vector2D((float) tr.getX(), (float) tr.getY()));
		int w = sc.LogicToPixel(tr.getWidth());
		int h = sc.LogicToPixel(tr.getHeight());
		
		if(fondoVisible()) {
			g.setColor(getColorFondo());
			g.fillRect((int) tl.getX(), (int) tl.getY(), w, h);
		}
		if(lineaVisible()) {
			g.setColor(getColorLinea());
			g.drawRect((int) tl.getX(), (int) tl.getY(), w, h);
		}
	}
	
	public void draw(Graphics g, ScrollConverter sc, int time) {
		// TODO
		if(!esVisible()) return;
		Rectangle2D tr = rect.getValue(time);
		Point tl = sc.AbsToScreen(new Vector2D((float) tr.getX(), (float) tr.getY()));
		int w = sc.LogicToPixel(tr.getWidth());
		int h = sc.LogicToPixel(tr.getHeight());
		
		if(fondoVisible()) {
			g.setColor(getColorFondo());
			g.fillRect((int) tl.getX(), (int) tl.getY(), w, h);
		}
		if(lineaVisible()) {
			g.setColor(getColorLinea());
			g.drawRect((int) tl.getX(), (int) tl.getY(), w, h);
		}
	}

	
	
	/**
	 * devuelve el tamaÃ±o en bytes que necesita la figura para
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
	 * Guarda los datos de este poligono
	 */	
	protected void guardar(DataOutputStream dos) throws IOException {
		/* borde */
		//TODO: all animation data
		Rectangle2D tr = rect.getDefaultValue();
		dos.writeFloat((float) tr.getX());
		dos.writeFloat((float) tr.getY());
		dos.writeFloat((float) tr.getWidth());
		dos.writeFloat((float) tr.getHeight());
	
	}
	
	
	
	public Vector2D getCentroTmd(int time)
	{
		//return getCentro();
		throw new UnsupportedOperationException("moverCentroTmd");
		
	}
	
	public void cambiarCentroTmd(Vector2D c, int time)
	{
		
		throw new UnsupportedOperationException("moverCentroTmd");
	}
	
	public void moverCentroTmd(Vector2D u, int time)
	{
		//TODO: Animar
		throw new UnsupportedOperationException("moverCentroTmd");
	}

	
}
