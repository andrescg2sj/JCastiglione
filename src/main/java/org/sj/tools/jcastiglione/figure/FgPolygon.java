package org.sj.tools.jcastiglione.figure;

import math.geom.Vector2D;
import java.awt.Graphics;
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;
import java.awt.Point;
import java.awt.Polygon;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

import org.sj.tools.jcastiglione.ScrollConverter;

public class FgPolygon extends FiguraSimple {
	private Vector2D vertice[];
	private boolean cerrado = true;
	
	/* cache para agilizar cÃ¡lculos */
	/** rectÃ¡ngulo que contiene al polÃ­gono */
	private Rectangle2D rect;
	
	
	public FgPolygon(Vector2D _c, Vector2D v[]) {
		super(_c);
		
		vertice = v;
		setVerFondo(false);
		calcularTamano();
	}
	
	/**
	 * Constructor que genera el polÃ­gono a partir de un archivo guardado
	 * @param dis DataInputStream de donde se obtienen los datos
	 */
	public FgPolygon(DataInputStream dis) throws IOException {
		super(dis);
		cerrado = dis.readBoolean();
		
		/* numero de vertices */
		int n = dis.readInt();
		
		/* vertices */
		vertice = new Vector2D[n];
		for(int i=0; i<n; i++) {
			float x = dis.readFloat();
			float y = dis.readFloat();
			vertice[i] = new Vector2D(x, y);
		}

		calcularTamano();
	}
	
	public int tipoFigura() {
		return FIGURA_POLIGONO;
	}

	
	void calcularTamano() {
		float minx, miny, maxx, maxy;
		minx = maxx = vertice[0].x;
		miny = maxy = vertice[0].y;
		for(int i=0; i<vertice.length; i++) {
			if(vertice[i].x < minx) minx = vertice[i].x;
			if(vertice[i].x > maxx) maxx = vertice[i].x;
			if(vertice[i].y < miny) miny = vertice[i].y;
			if(vertice[i].y > maxy) maxy = vertice[i].y;
		}
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
	
	
	/**
	 * Devuelve las dimensiones del mÃ­nimo rectÃ¡ngulo que contiene todos los puntos
	 * del polÃ­gono
	 */
	public Dimension2D getSize() {
		return new DoubleDim2D(rect.getWidth(), rect.getHeight());
	}
	
	/**
	 * devuelve el polÃ­gono en coordenadas de pantalla
	 */
	public Polygon getPolygon(ScrollConverter sc) {
		int n = vertice.length;
		int xPunto[] = new int[n];
		int yPunto[] = new int[n];
		if(xPunto == null || yPunto == null) return null;
		for(int i=0; i<n; i++) {
			Point p = sc.AbsToScreen(vertice[i]);
			xPunto[i] = (int) p.x;
			yPunto[i] = (int) p.y;
		}
		return new Polygon(xPunto, yPunto, n);
	}
	
	

	public void draw(Graphics g, ScrollConverter sc) {
		if(!esVisible()) return;
		/* calcular puntos */
		int n = vertice.length;
		int xPunto[] = new int[n];
		int yPunto[] = new int[n];
		if(xPunto == null || yPunto == null) return;
		for(int i=0; i<n; i++) {
			Point p = sc.AbsToScreen(vertice[i]);
			xPunto[i] = (int) p.x;
			yPunto[i] = (int) p.y;
		}
		
		if(fondoVisible()) {
			g.setColor(getColorFondo());
			g.fillPolygon(xPunto, yPunto, n);
		}
		if(lineaVisible()) {
			g.setColor(getColorLinea());
			//g.drawPolygon(xPunto, yPunto, n);
			for(int i=1; i<n; i++) {
				g.drawLine(xPunto[i-1], yPunto[i-1], xPunto[i], yPunto[i]);
			}
			if(cerrado) {
				g.drawLine(xPunto[n-1], yPunto[n-1], xPunto[0], yPunto[0]);
			}
		}
	}
	
	public int numVertices() {
		return vertice.length;
	}
	
	public Vector2D getVertice(int i) {
		int j = i % vertice.length;
		return vertice[j];
	}
	
	/**
	 * cambiar la posiciÃ³n de un vÃ©rtice
	 */
	public void setVertice(int i, Vector2D v) {
		int j = i % vertice.length;
		vertice[j] = v;
	}
	
	/**
	 * aÃ±adir un vertice despues del vertice i.
	 */
	public void addVertice(int i, Vector2D v) {
		int j = i % vertice.length;
		Vector2D nuevo[] = new Vector2D[vertice.length+1];
		for(int k=0; k<=j; k++) {
			nuevo[k] = vertice[k];
		}
		nuevo[j+1] = v;
		for(int k=j+1; k<vertice.length; k++) {
			nuevo[k+1] = vertice[k];
		}
		vertice = nuevo;
	}
	
	public void borrarVertice(int i) {
		int j = i % vertice.length;
		Vector2D nuevo[] = new Vector2D[vertice.length-1];
		for(int k=0; k<j; k++) {
			nuevo[k] = vertice[k];
		}
		for(int k=j+1; k<vertice.length; k++) {
			nuevo[k-1] = vertice[k];
		}
		vertice = nuevo;
	}
	
	/**
	 * determina si el punto v estÃ¡ dentro del polÃ­gono
	 */
	public boolean puntoDentro(Vector2D v) {
		float B = 0;
		int D = 0;
		
		/* primero comprobamos que estÃ¡ dentro del rectÃ¡ngulo */
		if(!rect.contains(v.x, v.y)) {
			return false;
		}
		/* calcular B (sentido de giro del polÃ­gono) 
			B < 0 sentido negativo; B > 0 sentido positivo */
		if(vertice.length <= 2) {
			return false;
		}
		int i;

		for(i=0; i<vertice.length; i++) {
			Vector2D a = getVertice(i);
			Vector2D b = getVertice(i+1);
			B += 0.5*(a.x*b.y-a.y*b.x);
		}
		
		Vector2D o = new Vector2D();
		for(i=0; i<vertice.length; i++) {
			Vector2D a = getVertice(i);
			Vector2D b = getVertice(i+1);
			float A = areaTriang(o, a, b);
			if(A > 0) {
				if(puntoDentroTriang(v, o, a, b)) {
					D++;
				}
			} else if(A < 0) {
				if(puntoDentroTriang(v, o, a, b)) {
					D--;
				}
			}
		}
		
		return (B*D > 0);
	}
	
	public void moverTmd(Vector2D u, int time) {
		throw new UnsupportedOperationException(this.getClass().toString() + ".moverTmd");
	}
	
	public boolean puntoDentroTmd(Vector2D u, int time) {
		throw new UnsupportedOperationException(this.getClass().toString() + ".puntoDentroTmd");
	}
	
	/**
	 * Calcula el Ã¡rea del triÃ¡ngulo a-b-c con signo dependiendo del 
	 * sentido de giro
	 */
	private static float areaTriang(Vector2D a, Vector2D b, Vector2D c) {
		Vector2D u = new Vector2D(b.x-a.x, b.y-a.y);
		Vector2D v = new Vector2D(c.x-a.x, c.y-a.y);
		return (float) 0.5*(u.x*v.y-u.y*v.x);
	}
		
	/**
	 * Determina si el punto p estÃ¡ dentro del triÃ¡ngulo a-b-c
	 */
	private static boolean puntoDentroTriang(Vector2D p, Vector2D a, Vector2D b, Vector2D c) {
		Vector2D u = new Vector2D(b.x-a.x, b.y-a.y);
		Vector2D v = new Vector2D(c.x-a.x, c.y-a.y);
		Vector2D t = p.getCopia();
		t.subs(a);
		float Discr = (u.x*v.y-u.y*v.x);
		if(Discr == 0) {
			return false;
		}
			
		float alpha = (t.x*v.y-t.y*v.x)/Discr;
		float beta = (u.x*t.y-u.y*t.x)/Discr;
		return (alpha >= 0) && (alpha <= 1) && (beta >=0) && (beta <= (1-alpha));
	}
	
	/**
	 * Cambia el tamaÃ±o de la figura
	 */
	public void reescalar(Dimension2D d) {
	}
	
	/**
	 * desplaza el polÃ­gono segÃºn el vector u
	 */
	public void mover(Vector2D u) {
		moverCentro(u);
		for(int i=0; i<vertice.length; i++) {
			vertice[i].add(u);
		}
		calcularTamano();
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
		/* cerrado o no*/
		dos.writeBoolean(cerrado);
		/* numero de puntos */
		dos.writeInt(vertice.length);
		
		/* vertices */
		for(int i=0; i<vertice.length; i++) {
			dos.writeFloat(vertice[i].x);
			dos.writeFloat(vertice[i].y);
		}
		
	}
	
	public boolean estaCerrado() {
		return cerrado;
	}
	
	public void setCerrado(boolean c) {
		cerrado = c;
	}
		
		
}
