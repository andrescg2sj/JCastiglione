package org.sj.tools.jcastiglione.figure;

import java.awt.Point;
import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;
import java.awt.geom.Dimension2D;
import math.geom.Vector2D;
import java.io.DataInputStream;
import java.io.IOException;

import java.util.logging.Logger;

import org.sj.tools.jcastiglione.animation.AnimableRectangle2D;
import org.sj.tools.jcastiglione.io.load.AnimRectLoader;

/**
 * Figura definida por un rectángulo.
 * @since 1.0
 */
public abstract class FigRectBounded extends FiguraSimple {
	/** borde de la figura */
	protected AnimableRectangle2D rect;
	
	Logger log = Logger.getLogger("FiguraBordeRect");
	
	static Vector2D middle(Vector2D a, Vector2D b)
	{
		Vector2D c = new Vector2D();
		c.add(a);
		c.add(b);
		c.mult(.5);
		return c;
	}
	
	static Vector2D center(float x, float y, float width, float height) {
		return new Vector2D(x+width/2, y+width/2);
	}
	
	public FigRectBounded(float x, float y, float width, float height) {
		super(center(x,y,width,height));
		init(x,y,width, height);
	}
	
	
	protected void copyColors(FigRectBounded fbr) {
		//super(fr.getCentro(),fr.getBounds());
		//init(fbr.getBounds());
		setColorFondo(fbr.getColorFondo());
		setColorLinea(fbr.getColorLinea());
	}

	static Rectangle2D containerRect(Vector2D a, Vector2D b)
	{
		float xmin = (float) Math.min((double) a.x, (double) b.x);
		float xmax = (float) Math.max((double) a.x, (double) b.x);
		float ymin = (float) Math.min((double) a.y, (double) b.y);
		float ymax = (float) Math.max((double) a.y, (double) b.y);
		return new Rectangle2D.Float(xmin, ymin, xmax-xmin, ymax-ymin);
	}
	
	void init(float x, float y, float width, float height) {
		Rectangle2D tr = new Rectangle2D.Float(x,y,width,height);
		log.fine("x = "+x);
		log.fine("y = "+y);
		log.fine("w = "+width);
		log.fine("h = "+height);
		rect = new AnimableRectangle2D(tr);
	}
	
	void init(Rectangle2D r) {
		Rectangle2D tr = new Rectangle2D.Float((float) r.getX(), (float) r.getY(),
				(float) r.getWidth(), (float) r.getHeight());
			rect = new AnimableRectangle2D(tr);		
	}
	
	public FigRectBounded(FigRectBounded fr) {
		super(fr.getCentro());
		init(fr.getBounds());
		copy(fr);
	}
	
	/*public FigRectBounded(FigRectBounded fr) {
	/	init(fr.getBounds());
	}*/


	
	public void copy(FigRectBounded r) {
		copyColors(r);
		this.rect.copyAnim(r.rect);
	}

	
	/**
	 * Constructor 
	 * @param _c Vector2D con el centro 
	 * @param r Rectangle2D que define el rectángulo exterior
	 */
	public FigRectBounded(Vector2D _c, Rectangle2D r) {
		//TODO: get rid of 'center' or define properly.
		super(_c);
		init(r);
	}
	
	
	/**
	 * Constructor 
	 * @param _c Vector2D con el centro 
	 * @param r Rectangle2D que define el rectángulo exterior
	 */
	public FigRectBounded(Rectangle2D r) {

		init(r);
	}
	

	
	FigRectBounded(DataInputStream dis) throws IOException {
		super(dis);
		// TODO ?
		rect = new AnimableRectangle2D();
	}
	
	public FigRectBounded(Vector2D c) {
		super(c);
		// TODO
		rect =new AnimableRectangle2D();
	}

	
	public FigRectBounded() {
		super(new Vector2D());
		rect = new AnimableRectangle2D();
	}
	
	
	
	/**
	 * Devuelve una copia del rectángulo exterior
	 * @return Rectangle2D que contiene la figura
	 */
	public Rectangle2D getRect() {
		return rect.getDefaultValue().getBounds();
	}

	
	/**
	 * averigua si el punto v está dentro de la figura
	 */
	public boolean puntoDentro(Vector2D v)
	{
		return rect.getDefaultValue().contains(v.x, v.y);
	}

	
	public boolean puntoDentroTmd(Vector2D v, int time)
	{
		return rect.getValue(time).contains(v.x,v.y);
	}
	
	
	public void moverTmd(Vector2D u, int time)
	{
		//TODO: ¿Mover Rectangle o centro? !!!
		Rectangle2D tempR = rect.getValue(time);
		Rectangle2D newR = new Rectangle2D.Double((float) tempR.getX()+u.x, (float) tempR.getY()+u.y, 
													(float) tempR.getWidth(), (float) tempR.getHeight());

		rect.setKeyFrame(time, newR);
	}
	
	/**
	 * Mover a la posición u
	 */
	public void mover(Vector2D u) {
		Rectangle2D tempR = rect.getDefaultValue();
		Rectangle2D newR = new Rectangle2D.Double((float) tempR.getX()+u.x, (float) tempR.getY()+u.y, 
													(float) tempR.getWidth(), (float) tempR.getHeight());
		rect.setDefaultValue(newR);
	}
	
	/**
	 *	reescala la figura para que tenga el tamaño d
	 */
	public void reescalar(Dimension2D d) {
		//TODO
		//rect.setRect(rect.getX(), rect.getY(), d.getWidth(), d.getHeight());
	}

	/**
	 *	devuelve el tamaño de la figura
	 */
	public Dimension2D getSize() {
		Rectangle2D tr = rect.getDefaultValue();
	 	return new DoubleDim2D(tr.getWidth(), tr.getHeight()); 	
	 }
	 
	/**
	 *	devuelve los límites
	 */
	public Rectangle2D getBounds() {
		Rectangle2D r = new Rectangle2D.Float();
		r.setRect(rect.getDefaultValue());
		return r;
	}

	
	public Rectangle2D getBounds(boolean anim, int time) {
		if(anim) {
			Rectangle2D r = new Rectangle2D.Float();
			r.setRect(rect.getValue(time));
			return r;
			
		} else {
			return getBounds();
		}
	}
	
	public void setRectAnimation(AnimRectLoader ar) {
		this.rect.copyAnim(ar);
	}


}	

