package org.sj.tools.jcastiglione.gui;

import java.awt.Canvas;
import java.awt.Scrollbar;
import java.awt.Point;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.awt.Graphics;
import java.awt.Frame;
import java.awt.Container;
import java.awt.Image;
import java.awt.Color;
import java.awt.Polygon;
import java.util.Vector;

import org.sj.tools.jcastiglione.Doc;
import org.sj.tools.jcastiglione.JCastiglioneDoc;
import org.sj.tools.jcastiglione.Layer;
import org.sj.tools.jcastiglione.LayerManager;
import org.sj.tools.jcastiglione.figure.FgPolygon;
import org.sj.tools.jcastiglione.figure.Figura;

import java.util.LinkedList;
import java.util.Iterator;
import math.geom.Vector2D;
import java.awt.FileDialog;


//TODO: move selection methods to class Selection

public class JCastiglioneView extends Canvas implements ScrollView, ViewIdentifiers
{

	
	
	/* Atributos */

	private Doc document = null;
	private int action;
	private ScrollRegion region;
	private MouseDispather gMouse;
	private Scrollbar hscroll, vscroll;
	
	
	/* atributos para dibujar */
	// vector para selection.vertices de poligonos
	private Vector verts;
	private int numPoints = 0;
	private boolean chosenCenter = false;
	
	

	private Polygon poligono;
	
	/* atributos de seleccion */
	Selection selection = new Selection();
	
	/* atributos sobre malla */
	private boolean verMalla = true;
	private int tipoMalla = MALLA_TRI;
	private boolean snapToGrid = true;
	private float gridSize = 10;
	
	/* animaciÃ³n */
	private boolean animar = false;
	private int numFrames = 100;
	private int currTime = 0;
	
	/* parametros */
	final int error = 2; // distancia minima para seleccionar un punto
	
	
	/*---------------------------------------------------------------------*/
	

	LogicScreen getPressedPoint(MouseEvent e) 
	{
		/* punto en que se ha pulsado */
		Point p = e.getPoint();
		
		/* coordenadas lÃ³gicas de superficie */
		Vector2D v = region.ScreenToAbs(p);
	
		if(snapToGrid) {
			v = snapToGrid(v);
			p = region.AbsToScreen(v);
		}
		return new LogicScreen(p,v);
	}
	
	
	
	public void newPoly(MouseEvent e)
	{
		Point p = getPressedPoint(e).p;
		
		if(e.getButton() == e.BUTTON1) {
			Graphics g = getGraphics();
			if(!chosenCenter) {
				gMouse.ptInicial = p;
				
				if(g != null) drawCross(g, p, 3, Color.BLACK);
				
				chosenCenter = true;
			} else {
				verts.add(p);
				if(numPoints > 0) {
					/* dibujar una linea */
					if(g != null) {
						g.setColor(Color.GRAY);
						g.drawLine( (int) gMouse.ptAnterior.getX(), (int) gMouse.ptAnterior.getY(),
										(int) p.getX(), (int) p.getY());
				
					}
				}
				gMouse.ptAnterior = p;
				numPoints++;
			}
		} else if(e.getButton() == e.BUTTON3) {
			/* cerrar poligono y aÃ±adir al documento */
			Vector2D vrt[] = new Vector2D[numPoints];
			for(int i=0; i<numPoints; i++) {
				vrt[i] = region.ScreenToAbs((Point) verts.get(i));
			}
			
			Vector2D centro = region.ScreenToAbs(gMouse.ptInicial);
			
			JCastiglioneDoc doc = (JCastiglioneDoc) document;
			doc.crearPoligono(centro, vrt);
			
			/* redibujar */
			repaint();
			
			/* reestablecer valores */
			verts.removeAllElements();
			chosenCenter = false;
			numPoints = 0;
		}
	}
	
	void createNewImage(LogicScreen ls)
	{
		Container cnt = (Container) this.getParent();
		Frame parent = (Frame) cnt.getParent();
		FileDialog dlg = new FileDialog(parent, "Cargar Imagen", FileDialog.LOAD);
		dlg.show();
		if(dlg.getFile() == null) {
			System.out.println("CANCELADO");
			return;
		}
		String total;
		total = dlg.getDirectory() + dlg.getFile();
		
		System.out.println(total);
		
		Vector2D centro = ls.v;
			
		JCastiglioneDoc doc = (JCastiglioneDoc) document;
		doc.createImage(centro, total);

	}
	
	void actAddVert(LogicScreen ls)
	{
		System.out.println("add selection.vertice");
		int i=0;
		selection.vertice = -1;
		FgPolygon pol = (FgPolygon) selection.figuraSelected;
		Point a = region.AbsToScreen(pol.getVertice(0));
		Point b = a;
		while(i < pol.numVertices() && selection.vertice == -1) {
			a = b;
			Vector2D u = (Vector2D) pol.getVertice(i+1);
			b = region.AbsToScreen(u);
			if(colision(a, b, ls.p)) {
				/* Ã©xito: se pulsÃ³ un segmento */
				pol.addVertice(i, u);
				selection.vertice = i + 1;
				setAccion(AC_MOVE_VERT);
				gMouse.ptInicial = ls.p;
				return;
			}
			i++;
		}
		
	}
	
	void actEditVert(LogicScreen ls)
	{
		int i=0;
		selection.vertice = -1;
		FgPolygon pol = (FgPolygon) selection.figuraSelected;
		while(i < pol.numVertices() && selection.vertice == -1) {
			Vector2D u = (Vector2D) pol.getVertice(i);
			Point q = region.AbsToScreen(u);
			if(colision(ls.p,q)) {
				/* Ã©xito: se encontrÃ³ un selection.vertice */
				selection.vertice = i;
				setAccion(AC_MOVE_VERT);
				gMouse.ptInicial = ls.p;
				return;
			}
			i++;
		}
	}
	
	void actDelVert(LogicScreen ls)
	{
		int i=0;
		selection.vertice = -1;
		FgPolygon pol = (FgPolygon) selection.figuraSelected;
		while(i < pol.numVertices() && selection.vertice == -1) {
			Vector2D u = (Vector2D) pol.getVertice(i);
			Point q = region.AbsToScreen(u);
			if(colision(ls.p,q)) {
				/* Ã©xito: se encontrÃ³ un selection.vertice */
				pol.borrarVertice(i);
				repaint();
				return;
			}
			i++;
		}

	}
	
	
	void actSelect(LogicScreen ls, Point mouse)
	{
		JCastiglioneDoc d = (JCastiglioneDoc) document;
		Vector2D w = region.ScreenToAbs(mouse);
		if(selection.anySelected)
		{
			/* si hay alguna figura seleccionada */
			SelectionItem nuevaSel = d.detectarFigura(w,animar,currTime);
			if(nuevaSel.equals(selection.selected))
			{
				/* si la que estoy pulsando es la seleccionada,
				 me preparo para moverla */
			
				gMouse.ptInicial = ls.p;
				gMouse.ptAnterior = (Point) gMouse.ptInicial.clone();
				setAccion(AC_SEL_MOVER);
				
				Graphics g = getGraphics();
				if (g != null) {
					g.setXORMode(Color.GRAY);
					g.drawRect(selection.contorno.x, selection.contorno.y, selection.contorno.width, selection.contorno.height);
				}
				
				}
			else
			{
				/* si no es la misma, selecciono la nueva */
				setSelected(nuevaSel);
				repaint();
			}
			
		}
		else
		{
			/* si no hay ninguna figura seleccionada */
			SelectionItem sel = d.detectarFigura(w,animar,currTime);
			setSelected(sel);
			if(sel.equals(SelectionItem.NONE)) {
				/* no se ha pulsado otra figura:
				preparar para seleccionar cuadro */
				gMouse.ptInicial = mouse;
				gMouse.ptAnterior = gMouse.ptInicial;
				setAccion(AC_SELEC_DRAG);
			} else {
				repaint();
			}
			
		}
		

	}
	
	void releaseNewLine(LogicScreen ls)
	{
		Graphics g = getGraphics();
		if (g != null) {
			g.setXORMode(Color.GRAY);
			g.drawLine((int)gMouse.ptInicial.getX(), (int)gMouse.ptInicial.getY(), (int) ls.p.getX(), (int) ls.p.getX());
			
			/* restablecer modo de dibujo normal */
			g.setPaintMode();
			
			/* pintar lÃ­nea final */
			g.drawLine((int)gMouse.ptInicial.getX(), (int)gMouse.ptInicial.getY(),(int) ls.p.getX(), (int) ls.p.getY());
			repaint();
		}

		/* Crear nueva figura */
		
		Vector2D p1, p2;
		p1 = region.ScreenToAbs(gMouse.ptInicial);
		p2 = region.ScreenToAbs(ls.p);
		
		JCastiglioneDoc doc = (JCastiglioneDoc) document;
		doc.crearLinea(p1, p2);
	}
	
	void releaseNewRect(Point p) 
	{
		Graphics g = getGraphics();
		if (g != null) {
			g.setXORMode(Color.GRAY);
			int rect_w = (int) (p.getX() - gMouse.ptInicial.getX());
			int rect_h = (int) (p.getY() - gMouse.ptInicial.getY());
			g.drawRect((int)gMouse.ptInicial.getX(), (int)gMouse.ptInicial.getY(), rect_w, rect_h);
			
			/* restablecer modo de dibujo normal */
			g.setPaintMode();
			
			/* pintar rectÃ¡ngulo final */
			g.drawRect((int)gMouse.ptInicial.getX(), (int)gMouse.ptInicial.getY(), rect_w, rect_h);
			repaint();
		}
		
		/* Crear nueva figura */
		Vector2D tl,br;
		Point pTopLeft = new Point((int) Math.min(gMouse.ptInicial.getX(), p.getX()),
											(int) Math.min(gMouse.ptInicial.getY(), p.getY()));
		Point pBotRight = new Point((int) Math.max(gMouse.ptInicial.getX(), p.getX()),
											(int) Math.max(gMouse.ptInicial.getY(), p.getY()));
		tl = region.ScreenToAbs(pTopLeft);
		br = region.ScreenToAbs(pBotRight);
		
		JCastiglioneDoc doc = (JCastiglioneDoc) document;
		doc.crearRectangulo(tl, br);

	}

	void releaseNewText(Point p) 
	{
		Graphics g = getGraphics();
		if (g != null) {
			g.setXORMode(Color.GRAY);
			int rect_w = (int) (p.getX() - gMouse.ptInicial.getX());
			int rect_h = (int) (p.getY() - gMouse.ptInicial.getY());
			g.drawRect((int)gMouse.ptInicial.getX(), (int)gMouse.ptInicial.getY(), rect_w, rect_h);
			
			/* restablecer modo de dibujo normal */
			g.setPaintMode();
			
			/* pintar rectÃ¡ngulo final */
			//g.drawRect((int)gMouse.ptInicial.getX(), (int)gMouse.ptInicial.getY(), rect_w, rect_h);
			repaint();
		}
		
		/* Crear nueva figura */
		Vector2D tl,br;
		Point pTopLeft = new Point((int) Math.min(gMouse.ptInicial.getX(), p.getX()),
											(int) Math.min(gMouse.ptInicial.getY(), p.getY()));
		Point pBotRight = new Point((int) Math.max(gMouse.ptInicial.getX(), p.getX()),
											(int) Math.max(gMouse.ptInicial.getY(), p.getY()));
		tl = region.ScreenToAbs(pTopLeft);
		br = region.ScreenToAbs(pBotRight);
		
		JCastiglioneDoc doc = (JCastiglioneDoc) document;
		doc.createText(tl, br);

	}

	
	void releaseMove(Point p)
	{
		Graphics g = getGraphics();
		if (g != null) {
			g.setXORMode(Color.GRAY);
			g.drawRect(selection.contorno.x, selection.contorno.y, selection.contorno.width, selection.contorno.height);
		}
		
		int dx = (int) (p.getX()-gMouse.ptInicial.getX());
		int dy = (int) (p.getY()-gMouse.ptInicial.getY());
		Vector2D desp = new Vector2D();
		desp.x = (float) region.PixelToLogic(dx);
		desp.y = (float) region.PixelToLogic(dy);
		selection.contorno.x += (int) (p.getX()-gMouse.ptAnterior.getX());
		selection.contorno.y += (int) (p.getY()-gMouse.ptAnterior.getY());
		selection.generaManejadores(region, animar, currTime);
		
		//Graf3Doc doc = (Graf3Doc) document;
		selection.figuraSelected.mover(desp, animar, currTime);
		setAccion(AC_SELECCIONAR);
		repaint();
		
		// limpieza				
		
		gMouse.ptAnterior = null;
		gMouse.ptInicial = null;
		poligono = null;
		System.gc();

	}
	
	void releaseSelecDrag(Point p)
	{
		Graphics g = getGraphics();
		if (g != null) {
			g.setXORMode(Color.GRAY);
			int rect_w = (int) (p.getX() - gMouse.ptInicial.getX());
			int rect_h = (int) (p.getY() - gMouse.ptInicial.getY());
			g.drawRect((int)gMouse.ptInicial.getX(), (int)gMouse.ptInicial.getY(), rect_w, rect_h);
			
			/* restablecer modo de dibujo normal */
			g.setPaintMode();
			setAccion(AC_SELECCIONAR);
		}

	}
	

	/*---------------------------------------------------------------------*/
	
		
	
	public JCastiglioneView(int w, int h) {
		gMouse = new MouseDispather(this);
		setSize(w, h);
		region = new ScrollRegion(new Dimension(w, h), new Dimension(w, h), 1, new Vector2D(0, 0));
		addMouseListener(gMouse);
		addMouseMotionListener(gMouse);
		verts = new Vector(10, 10);
		selection.selectionListeners = new Vector();
	}
	
	
	public int getCurrAction()
	{
		return action;
	}
	
	public Object getDoc() {
		return document;
	}
	
	public void setDoc(Object o) {
		if (o instanceof Doc) {
			document = (Doc) o;
			Object s = document.requestObject(JCastiglioneDoc.REQUEST_DOCSIZE);
			if (s instanceof Dimension) {
				region.setAreaTotal((Dimension) s);
			}
		}
	}
	
	public void setTime(int time)
	{
		currTime = time;
		if(animar) {
			repaint();
		}
	}
	
	public void drawFigures(JCastiglioneDoc myDoc, Graphics g)
	{
		/* dibujar todas las figuras */
		LayerManager lm = myDoc.getLayerManager();
		Iterator itrL = lm.getLayerIterator();
		//Iterator itr = (Iterator) myDoc.getIterator();
		//int index = 0;
		
		//int time = 0;

		while(itrL.hasNext()) {
			Layer l = (Layer) itrL.next();
			Iterator itrF = l.getFigureIterator();
			while(itrF.hasNext()) {
				Figura f = (Figura) itrF.next();
				f.draw(g, region);
			}
		}
	}
	
	public void drawFiguresInFrame(JCastiglioneDoc myDoc, Graphics g, int time)
	{
		/* dibujar todas las figuras */
		LayerManager lm = myDoc.getLayerManager();
		Iterator itrL = lm.getLayerIterator();
		//Iterator itr = (Iterator) myDoc.getIterator();
		//int index = 0;
		
		//int time = 0;

		while(itrL.hasNext()) {
			Layer l = (Layer) itrL.next();
			Iterator itrF = l.getFigureIterator();
			while(itrF.hasNext()) {
				Figura f = (Figura) itrF.next();
				f.draw(g, region, time);
			}
		}		
	}

	
	public void paint(Graphics g) {
		Rectangle d = getBounds();
		JCastiglioneDoc myDoc = (JCastiglioneDoc) getDoc();
		
		/* dibujar malla */
		if(verMalla) {
			Rectangle r = region.getZonaVisible();
			int imin = (int) Math.ceil(r.getMinX() / gridSize);
			int jmin = (int) Math.ceil(r.getMinY() / gridSize);
			int imax = (int) Math.floor(r.getMaxX() / gridSize);
			int jmax = (int) Math.floor(r.getMaxY() / gridSize);
			switch(tipoMalla) {
			case MALLA_CUAD:
				for(int i = imin; i<=imax; i++) {
					for(int j = jmin; j<=jmax; j++) {
						Vector2D v = new Vector2D(i*gridSize, j*gridSize);
						Point p = region.AbsToScreen(v);
						g.drawLine(p.x, p.y, p.x, p.y);
					}
				}
				break;
			case MALLA_TRI:
				for(int i = imin; i<=imax; i++) {
					for(int j = jmin; j<=jmax; j++) {
						Vector2D v = new Vector2D((i + (j&1)/(float)2)*gridSize, j*gridSize*0.866f);
						Point p = region.AbsToScreen(v);
						g.drawLine(p.x, p.y, p.x, p.y);
					}
				}
				break;
			}
		}

		if(animar) {
			drawFiguresInFrame(myDoc, g, currTime);
		} else {
			drawFigures(myDoc, g);
		}
		
		/* dibujar la figura seleccionada, si la hay */
		if(selection.anySelected) {
			selection.generaManejadores(region, animar, currTime);
			drawHandlers(g);

			if(selection.figuraSelected instanceof FgPolygon) {
				FgPolygon pol = (FgPolygon) selection.figuraSelected;
				for(int i=0; i<pol.numVertices(); i++) {
					Point p = region.AbsToScreen(pol.getVertice(i));
					g.drawRect(p.x-error, p.y-error, 2*error, 2*error);
				}
			}
		}
		
		
		g.drawRect(0, 0, d.width-1, d.height-1);
	}
	
	public ScrollRegion getScrollRegion() {
		return region;
	}

	public void addScroll(Scrollbar hbar, Scrollbar vbar) {
		hscroll = hbar;
		vscroll = vbar;
		initializeBars();
	}
	
	private void initializeBars() {
		Rectangle d = getBounds();
		hscroll.setLocation(0, d.height+1);
		hscroll.setSize(d.width, 15);
		vscroll.setLocation(d.width+1, 0);
		vscroll.setSize(15, d.height);
		hscroll.setMaximum(region.areaTotal.width);
		vscroll.setMaximum(region.areaTotal.height);
		hscroll.setUnitIncrement(region.areaTotal.width/10);
		vscroll.setUnitIncrement(region.areaTotal.height/10);
		adjustBars();
	}
	
	private void adjustBars() {
		Rectangle r = region.getZonaVisible();
		hscroll.setVisibleAmount(r.width);
		vscroll.setVisibleAmount(r.height);
		// TRAZAS:
		//System.out.println(" X : ("+r.x + ", "+ region.areaTotal.width+ ")");
		//System.out.println(" Y : ("+r.y + ", "+ region.areaTotal.height+ ")");
		hscroll.setValue(r.x);
		vscroll.setValue(r.y);
	}
	
	/**
	 * AtenciÃ³n a mensajes
	 */
	public void sendMessage(int msgIndex, Object obj) {
		Vector2D u;
		switch(msgIndex) {
		case SEND_BACKGROUND:
			/*
			if(obj instanceof Image) {
				fondo = (Image) obj;
				Rectangle r = getBounds();
				region.setOrigen(new Vector2D(0, 0));
				int w = fondo.getWidth(this);
				int h = fondo.getHeight(this);
				if(w > region.areaTotal.width) {
					region.areaTotal.width = w;
					hscroll.setMaximum(region.areaTotal.width);
				}
				if(h > region.areaTotal.height) {
					region.areaTotal.height = h;
					vscroll.setMaximum(region.areaTotal.height);
				}
				if(r.width > region.areaVisible.width) {
					region.areaVisible.width = (int) Math.min(r.width, region.areaTotal.width);
				}
				if(r.height > region.areaVisible.height) {
					region.areaVisible.height = (int) Math.min(r.height, region.areaTotal.height);
				}
				ajustarBarras();
				repaint();
			}
			*/
			break;
		case MSG_AUMENTAR:
			region.aumentar();
			adjustBars();
			repaint();
			break;
		case MSG_DISMINUIR:
			region.setOrigen(new Vector2D(0,0));
			region.disminuir();
//			if(region.getEscala() < 1) {
//				region.setOrigen(new Vector2D(0,0));
//			}
			adjustBars();
			repaint();
			break;
		case MSG_CHG_SCROLL:
			u = new Vector2D(hscroll.getValue(), vscroll.getValue());
			//System.out.println("SCROLL: "+u.toString());
			region.setOrigen(u);
			//System.out.println("SCROLL: "+u.toString());
			repaint();
			break;
		case MSG_AJUSTAR_FONDO:
			/*
			Rectangle r = getBounds();
			int w = fondo.getWidth(this);
			int h = fondo.getHeight(this);
			region.setOrigen(new Vector2D(0, 0));
			region.areaTotal.width = w;
			region.areaTotal.height = h;
			if(w < region.areaVisible.width) region.areaVisible.width = w;
			if(h < region.areaVisible.height) region.areaVisible.height = h;
			hscroll.setVisibleAmount(1);
			vscroll.setVisibleAmount(1);
			hscroll.setMaximum(region.areaTotal.width);
			vscroll.setMaximum(region.areaTotal.height);
			ajustarBarras();
			repaint();
			*/
			break;
		case MSG_REFRESH_FIGURA:
			if(!(obj instanceof Figura)) return;
			Figura f = (Figura) obj;
			refreshFigure(f);
			break;
		case MSG_SET_ACCION:
			if(!(obj instanceof Integer)) return;
			Integer i = (Integer) obj;
			setAccion(i.intValue());
			break;
		case MSG_REPAINT:
			repaint();
			break;
		}
		
	
	}
	
	public void refreshFigure(Figura f) {
		Vector2D u;
		System.out.println("Refresh");
		Graphics g = getGraphics();
		drawFigure(g, f);
		u = f.getCentro();
		Point pt = region.AbsToScreen(u);
		drawCross(g, pt, 3, Color.WHITE);
	}
	
	
	public Object requestObject(int objIndex) {
		return null;
	}
	
	public void setAccion(int idAccion) {
		switch(idAccion) {
		case AC_NEW_VERT:
			if(idAccion != action) {
				document.sendMessage(JCastiglioneDoc.MSG_CLOSE_FIGURE, null);
			}
			break;
		case AC_EDIT_VERT:
			break;
		case AC_BORRAR:
			borrarSelected();
			action = AC_SELECCIONAR;
			return;
		}
		action = idAccion;
	}
	
	private void drawCross(Graphics g, Point p, int radio, Color color) {
		g.setColor(color);
		Color c = new Color(255-color.getRed(), 255-color.getGreen(), 255-color.getBlue());
		g.drawLine(p.x, p.y-radio, p.x, p.y+radio);
		g.drawLine(p.x-radio, p.y, p.x+radio, p.y);
		g.setColor(c);
		g.drawLine(p.x-radio, p.y-radio, p.x+radio, p.y+radio);
		g.drawLine(p.x-radio, p.y+radio, p.x+radio, p.y-radio);
	}
	
	public void drawFigure(Graphics g, Figura f) {
		// obtengo el polÃ­gono, con coordenadas relativas a la
		// vista
		/*
		Polygon poli = f.getPolygon(region);
		if(poli == null) return;
		if(!f.isSelected()) {
			// si la figura no esta seleccionada:
			if(!f.esTransp())
			{
				// si no es transparente pinto el relleno
				g.setColor(f.getColor());
				g.fillPolygon(poli);
			}
			
			// obtengo una lista de los vertices
			// para saber los colores
			// (las coordenadas de esta lista no me sirven porque
			// son relativas al centro de la figura, pero ya las
			// tengo en el poligono poly
			Iterator itr = f.getPuntos();
						
			int max = f.numPuntos()-1;
			Punto p;
			for(int i=0; i<max; i++)
			{
				p = (Punto) itr.next();
				if(!p.esTransp())
				{
					// si el lado i no es transparente, lo pinto
					g.setColor(p.getColor());
					g.drawLine(poli.xpoints[i], poli.ypoints[i], poli.xpoints[i+1],poli.ypoints[i+1]);
				}
			}
			// ultimo lado
			p = (Punto) itr.next();
			if(!p.esTransp())
			{
				g.setColor(p.getColor());
				g.drawLine(poli.xpoints[max], poli.ypoints[max], poli.xpoints[0],poli.ypoints[0]);
			}
			
		} else {
			// si la figura estÃ¡ seleccionada
			g.setColor(Color.YELLOW);
			if(f.numPuntos() == 1) {
				pintarCruz(g, new Point(poli.xpoints[0], poli.ypoints[0]), 2, Color.YELLOW);
			} else if(f.numPuntos() > 1) {
				int ax = poli.xpoints[0];
				int ay = poli.ypoints[0];
				for(int i=0; i<poli.npoints; i++) {
					g.setColor(Color.YELLOW);
					g.drawLine(ax, ay, poli.xpoints[i],poli.ypoints[i]);
					ax = poli.xpoints[i];
					ay = poli.ypoints[i];
					pintarCruz(g, new Point(ax, ay), 2, Color.YELLOW);
				}
				if(f.estaTerminada()) {
					g.drawLine(ax, ay, poli.xpoints[0],poli.ypoints[0]);
				}
			}
		}
		
		Vector2D u = f.getCentroAbs();
		Point pt = region.AbsToScreen(u);
		pintarCruz(g, pt, 3, Color.WHITE);
		*/
		
	}
	
	
	/**
	 * Number of selected Figures
	 */
	public int numSelected() {
		return 1;//TODO: multiple selection
	}
	
	/**
	 * devuelve un iterador con la lista de figuras seleccionadas
	 */
	public Iterator getSelected() {
		/* ATENCIÃN: No estÃ¡ completo.  */
		Vector v = new Vector();
		if(selection.anySelected) {
			JCastiglioneDoc d = (JCastiglioneDoc) document;
			v.add(d.getFigura(selection.selected.getIdLayer(), selection.selected.getIdFigura()));
		}
		return v.iterator();
	}
	
	public void setSelected(SelectionItem s) {
		if(s == null || s.isNothing()) {
			setNoneSelected();
			return;
		}
		
		selection.anySelected = true;
		//indexSelected = index;

		JCastiglioneDoc d = (JCastiglioneDoc) document;
		selection.figuraSelected = d.getFigura(s.getIdLayer(), s.getIdFigura());
		selection.selected = s;
		selection.generaManejadores(region, animar, currTime);
		generarSelectEvents();
	}
	
	public void setSelected(int index) {
		//System.out.println("setSelected");
		if(index < 0) {
			setNoneSelected();
			return;
		}
		selection.anySelected = true;
		//indexSelected = index;

		JCastiglioneDoc d = (JCastiglioneDoc) document;
		selection.figuraSelected = d.getFigura(index);
		
		seleccionando();
	}
	
	/* realiza operaciones comunes a una selecciÃ³n */
	protected void seleccionando() {
		selection.contorno = region.AbsToScreen( selection.figuraSelected.getBounds() );
		selection.generaManejadores(region, animar, currTime);
		generarSelectEvents();
		
		if(selection.figuraSelected instanceof FgPolygon) {
			FgPolygon p = (FgPolygon) selection.figuraSelected;
			selection.vertices = new LinkedList();
			for(int i=0; i<p.numVertices(); i++) {
				selection.vertices.add(p.getVertice(i));
			}
		}
	}
	
	public void setNoneSelected() {
			selection.anySelected = false;
			//indexSelected = -1;
			selection.selected = SelectionItem.NONE;
			selection.figuraSelected = null;
			generarSelectEvents();
			
	}
	
	private void generarSelectEvents() {
		SelectionEvent e = new SelectionEvent(selection.figuraSelected);
		for(int i=0; i<selection.selectionListeners.size(); i++) {
			SelectionListener sl = (SelectionListener) selection.selectionListeners.get(i);
			
			if(selection.anySelected) {
				sl.onNewSelection(e);
			} else {
				sl.onNoneSelected(e);
			}
		}
	}
	
	
	void drawHandlers(Graphics g) {
		for(int i=0; i<selection.manejador.length; i++) {
			if(selection.manejador[i] != null) {
				Rectangle r = selection.manejador[i];
				g.setColor(Color.WHITE);
				g.fillRect(r.x, r.y, r.width, r.height);
				g.setColor(Color.BLACK);
				g.drawRect(r.x, r.y, r.width, r.height);
			}
		}
	}
	
	boolean isSnapToGridOn()
	{
		return snapToGrid;
	}
	
	/**
	 * borrar figura seleccionada (si la hay) 
	 */
	private void borrarSelected() {
		if(!selection.anySelected) {
			return;
		}
		
		JCastiglioneDoc d = (JCastiglioneDoc) document;
		d.borrarFigura(selection.selected.getIdLayer(), selection.selected.getIdFigura());

		selection.anySelected = false;
		//indexSelected = -1;
		selection.selected = SelectionItem.NONE;
		selection.figuraSelected = null;
		repaint();
	}
	
	public Vector2D snapToGrid(Vector2D v) {
		final float h = 0.866f; /* relaciÃ³n altura/lado de un triang. equilÃ¡tero*/
		float x,y;
		switch(tipoMalla) {
		case MALLA_CUAD:
			x = gridSize * Math.round(v.x/gridSize);
			y = gridSize * Math.round(v.y/gridSize);
			break;
		case MALLA_TRI:
			int j = Math.round(v.y / (gridSize*h));
			float dx = (float) (j&1)/(float)2;
			x = gridSize * (Math.round(v.x/gridSize - dx)+dx);
			y = j*gridSize*h;
			break;
		default:
			return v;
		}
		return new Vector2D(x, y);
	}
	
	public void addSelectionListener(SelectionListener l) {
		selection.selectionListeners.add(l);
	}
	
	public void setMesh(int tipo, boolean visible) {
		verMalla = visible;
		tipoMalla = tipo;
		snapToGrid = (tipo == MALLA_NO) ? false : true;
		repaint();
	}
	
	public boolean getAnimateStatus() {
		return animar;
	}
	
	public void setAnimateStatus(boolean a) {
		animar = a;
		repaint();
	}
	
	/**
	 * detecta si el ratÃ³n ha pulsado suficientemente cerca de un punto
	 * como para seleccionarlo
	 */
	public boolean colision(Point p, Point q) {
		return (Math.abs(q.getX() - p.getX()) < error) && (Math.abs(q.getY() - p.getY()) < error);
	}
	
	/**
	 * detecta si el ratÃ³n ha pulsado suficientemente cerca de un segmento
	 * como para seleccionarlo
	 */
	public boolean colision(Point a, Point b, Point p) {
		int rx = p.x - a.x;
		int ry = p.y - a.y;
		int vx = b.x - a.x;
		int vy = b.y - a.y;
		int nx = -vy;
		int ny = vx;
		int x = rx*vx + ry*vy;
		int y = rx*nx + ry*ny;
		double mod = Math.sqrt(vx*vx + vy*vy);
		double ux =  x / mod;
		double uy = Math.abs(y/mod);
		System.out.println("("+ux+", "+uy+")");
		return (uy < error) && (ux > 0) && (ux < mod);
		
	}
	
	/**
	 * poner la seleccion en primera posicion
	 */
	public void sendToFront() {
		if(selection.anySelected) {
			JCastiglioneDoc doc = (JCastiglioneDoc) document;
			setSelected(doc.alFrente(selection.selected));
			repaint();
		}
	}
	
	public void sentToBackgnd() {
		if(selection.anySelected) {
			JCastiglioneDoc doc = (JCastiglioneDoc) document;
			setSelected(doc.alFondo(selection.selected));
			repaint();
		}
	}

	public void advance() {
		if(selection.anySelected) {
			JCastiglioneDoc doc = (JCastiglioneDoc) document;
			setSelected(doc.avanzar(selection.selected));
			repaint();
		}
	}

	public void goBack() {
		if(selection.anySelected) {
			JCastiglioneDoc doc = (JCastiglioneDoc) document;
			setSelected(doc.retroceder(selection.selected));
			repaint();
		}
	}

	public void nextSelection() {
		if(selection.anySelected) {
			JCastiglioneDoc doc = (JCastiglioneDoc) document;
			LayerManager lm = doc.getLayerManager();
			SelectionItem s = lm.getNextSelec(selection.selected);
			setSelected(s);
			repaint();
			
		}
	}

	public void previousSelection() {
		if(selection.anySelected) {
			JCastiglioneDoc doc = (JCastiglioneDoc) document;
			LayerManager lm = doc.getLayerManager();
			SelectionItem s = lm.getPrevSelec(selection.selected);
			setSelected(s);
			repaint();
		}
	}

}