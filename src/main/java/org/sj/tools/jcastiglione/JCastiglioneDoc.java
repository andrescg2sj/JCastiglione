package org.sj.tools.jcastiglione;

import java.util.Iterator;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.LinkedList;

import org.sj.tools.jcastiglione.animation.FFAnimationExport;
import org.sj.tools.jcastiglione.animation.PNGAnimationExport;
import org.sj.tools.jcastiglione.figure.FgPolygon;
import org.sj.tools.jcastiglione.figure.FigImage;
import org.sj.tools.jcastiglione.figure.FigRectangle;
import org.sj.tools.jcastiglione.figure.FigText;
import org.sj.tools.jcastiglione.figure.Figura;
import org.sj.tools.jcastiglione.figure.Linea;
import org.sj.tools.jcastiglione.io.XMLGraf3Doc;

import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.Color;
import math.geom.Vector2D;
import java.io.File;
import java.io.FileOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.DataInputStream;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;


public class JCastiglioneDoc implements Doc {
	
	
	
	public final static int REQUEST_DOCSIZE = 1;
	public final static int REQUEST_FIGURAS = 2;
	
	public final static int MSG_NEW_OBJ = 1;
	public final static int MSG_NEW_VERT = 2;
	public final static int MSG_CLOSE_FIGURE = 3;
	
	/** vista */
	private View view = null;
	/** tamaÃ±o del documento en pixels */
	private Dimension size;
	
	/** lista de figuras */
	//LinkedList lstFiguras = new LinkedList();
	
	/** lista de capas */
	LayerManager lyMan;

	private int indexSelected;
	private boolean selected = false;
	private boolean creating = false;
	private float actZ = 0;
	
	// 
	private int animationLength = 10;
	
	
	/* Atributos para exportar a SVG */
	/** milÃ­metros por unidad lÃ³gica (pÃ­xel) */
	double mpu = .1; 
	
	public JCastiglioneDoc(Dimension docsize)
	{
		size = docsize;
		lyMan = new LayerManager();
		lyMan.newLayer("fondo");
	}
	
	public Object getView()
	{
		return view;
	}
	
	public void setView(Object o)
	{
		if (o instanceof View) view = (View) o;
	}
	
	public Iterator getIterator()
	{
		return lyMan.getFiguraIterator();
	}
	
	// devuelve true si hay alguna figura seleccionada
	public boolean anySelected()
	{
		return selected;
	}
	
	public void selectNone()
	{
		selected = false;
	}
	
	public boolean isCreating()
	{
		return creating;
	}
	
	public int getIndexSelected()
	{
		return 0;
	}

	public Figura getFiguraSelected()
	{
		/*
		if(!selected) return null;
		return (Figura) lstFiguras.get(indexSelected);
		*/
		return null;
	}
	
	public void crearLinea(Vector2D a, Vector2D b) {
		Linea l = new Linea(a, b);
		l.setZ(actZ);
		incrZ();
		lyMan.addFigura(l);
	}
	
	void prepareAndAdd(Figura f){
		f.setZ(actZ);
		incrZ();
		lyMan.addFigura(f);	
	}

	
	public void addFigure(Figura f) {
		prepareAndAdd(f);
	}
	
	public void crearRectangulo(Vector2D tl, Vector2D br) {
		FigRectangle r = new FigRectangle(tl, br);
		prepareAndAdd(r);
	}
	
	@Deprecated
	public void createText(Vector2D tl, Vector2D br) {
		FigText t = new FigText(tl, br, "Text");
		prepareAndAdd(t);
	}

	public void createText(Vector2D tl, Vector2D br, String text) {
		FigText t = new FigText(tl, br, text);
		prepareAndAdd(t);
	}

	
	public void crearPoligono(Vector2D cent, Vector2D v[]) {
		FgPolygon p = new FgPolygon(cent, v);
		prepareAndAdd(p);
	}
	
	public void createImage(Vector2D cent, String filename) {
		FigImage i = new FigImage(cent, filename);
		/* presets */
		i.setVerLinea(false);
		prepareAndAdd(i);
	}
	
	
	public Figura getFigura(int index) {
		return lyMan.getFigura(index);
	}

	public Layer getCurrentLayer(){ 
		return lyMan.getCurrent();
	}

	public Figura getFigura(int layer, int index) {
		return lyMan.getFigura(layer, index);
	}
	
	/**
	 * borra la figura con Ã­ndice dado
	 */
	public void borrarFigura(int index) {
		lyMan.borrarFigura(index);
		
		/* compatibilidad ? */
		selected = false;
		indexSelected = -1;
	}


	public void borrarFigura(int layer, int index) {
		lyMan.borrarFigura(layer, index);
		
		/* compatibilidad ? */
		selected = false;
		indexSelected = -1;
	}


	
	public void sendMessage(int msgIndex, Object obj)
	{
		Point p;
		ScrollRegion reg;
		ScrollView sv;
		Vector2D v;
		Figura f;
		
		switch(msgIndex)
		{
		
		case MSG_NEW_OBJ:
			/*
			if(!(obj instanceof Point)) return;
			if(selected)
			{
				
				f = getFiguraSelected();
				f.setSelected(false);
				
			}
			creating = true;
			p = (Point) obj;
			sv = (ScrollView) view;
			reg = sv.getScrollRegion();
			v = reg.ScreenToAbs(p);
			f = new Figura(v);
			lyMan.addFigura(f);
			setSelected(lstFiguras.size()-1);
			view.sendMessage(Graf3View.MSG_REFRESH_FIGURA, f);
			*/
			break;
		case MSG_NEW_VERT:
			/*
			if(!(obj instanceof Point)) return;
			if(!selected) return;
			p = (Point) obj;
			sv = (ScrollView) view;
			reg = sv.getScrollRegion();
			f = getFiguraSelected();
			v = reg.ScreenToAbs(p);
			f.addPuntoRef(v);
			view.sendMessage(Graf3View.MSG_REFRESH_FIGURA, f);
			*/
			break;
		case MSG_CLOSE_FIGURE:
			/*
			if(!selected) return;
			creating = false;
			f = (Figura) getFiguraSelected();
			f.cerrar();
			view.sendMessage(Graf3View.MSG_REPAINT, null);
			*/
			break;
		}
	}
	
	
	public Object requestObject(int objIndex)
	{
		switch(objIndex)
		{
			case REQUEST_DOCSIZE:
				return size;
			case REQUEST_FIGURAS:
				return getIterator();
		
		}
		
		return null;
	}
	
	public void setSelected(int index)
	{
		/*
		Figura f;
		if(selected)
		{
			f = getFiguraSelected();
			f.setSelected(false);
			selected = false;
		}
		if(index < 0) return;
		selected = true;
		indexSelected = index;
		f = getFiguraSelected();
		f.setSelected(true);
		*/
	}
	
	
	
	/**
	 * Detecta si hay alguna figura que contenga al Vector2D v
	 * @param v Vector2D el cual se quiere saber si estÃ¡ en alguna figura
	 * @returns el Ã­ndice de la figura que lo contiene, o -1 si no hay ninguna
	 */
	public SelectionItem detectarFigura(Vector2D v, boolean anim, int time)
	{
		Iterator itrLayer, itrFigura;
		
		itrLayer = lyMan.getLayerIterator();
		
		
		int indice=-1;
		int idCapa=-1;
		Figura actual=null;
		
		int i=0;
		while(itrLayer.hasNext())
		{
			idCapa++;
			Layer l = (Layer) itrLayer.next();
			itrFigura = l.getFigureIterator();
			float zmax = -1;
			
			while(itrFigura.hasNext()) {
			
				Figura f = (Figura) itrFigura.next();

				if(f.puntoDentro(v,anim, time) && f.getZ() > zmax)
				{
					actual = f;
					indice = i;
					zmax = f.getZ();
				}
				i++;
			}
		}
		if(indice == -1) return SelectionItem.NONE;
		
		return new SelectionItem(idCapa, indice, actual);
	}
	
	public int getAnimLength()
	{
		return this.animationLength;
	}
	
	public void cargar(String nombreArchivo) throws FileNotFoundException, IOException
	{
		/*
		final int HEAD_SIZE = 8;
		final int FILE_HEAD =  0x01000100;
		final int SZCR_HEAD = 0x01000101;
		final int SZCR_SIZE = 10; //flags(2)+X(2)+Y(2)+R(4)
		final int EJES_DEFECTO = 0;
		final int FIGLIST_HEAD = 0x01000102;
		final int FIGURA_HEAD = 0x01000103;
		final int FIG_DATA_SIZE = 16; //flags(1) + color(3) + centro(8) + numVert(4)
		final int MIN_FIG_SIZE = HEAD_SIZE+FIG_DATA_SIZE; // 16
		final int VERTICE_SIZE = 12;
		
		borrarTodo();
		
	
		File archivo = new File(nombreArchivo);
		FileInputStream fis = new FileInputStream(archivo);
		DataInputStream dis = new DataInputStream(fis);
		
		// leer cabecera
		int head, tamTotal, tamChunk;
		
		head = dis.readInt();
		tamTotal = dis.readInt();
		if(head != FILE_HEAD) throw new IOException("Cabecera incorrecta");
		
		// configuracion
		head = dis.readInt();
		if(head != SZCR_HEAD) throw new IOException("Falta configuracion");
		tamChunk = dis.readInt();
		if(tamChunk != SZCR_SIZE) throw new IOException("TamaÃ±o cfg incorrecto");
		
		dis.readShort(); //config
		size.width = dis.readShort();
		size.height = dis.readShort();
		dis.readInt(); //Reservado
		
		ScrollView sv = (ScrollView) view;
		ScrollRegion reg = sv.getScrollRegion();
		reg.setAreaTotal(size);
		
		// lista de figuras
		int tamFiguras;
		head = dis.readInt();
		if(head != FIGLIST_HEAD) throw new IOException("Falta lista de figuras");
		tamFiguras = dis.readInt();
		
		while(tamFiguras > 0)
		{
			head = dis.readInt();
			tamChunk = dis.readInt();
			if(head == FIGURA_HEAD)
			{
				// 1 flags
				byte flags = dis.readByte();
				
				// 2. color
				int red = dis.readUnsignedByte();
				int green = dis.readUnsignedByte();
				int blue = dis.readUnsignedByte();
				
				// 3. centro
				float x, y;
				x = dis.readFloat();
				y = dis.readFloat();
				Vector2D v = new Vector2D(x, y);
				
				// 4. numero de vertices
				int numVert = dis.readInt();
				
				// crear la figura
				Figura f = new Figura(v);
				
				for(int i=0; i<numVert; i++)
				{
					red = dis.readUnsignedByte();
					green = dis.readUnsignedByte();
					blue = dis.readUnsignedByte();
					
					byte grosor = dis.readByte();
					
					x = dis.readFloat();
					y = dis.readFloat();					
					v = new Vector2D(x, y);
					Vertice vert = new Vertice(v);
					vert.setColor(new Color(red, green, blue));
					vert.setGrosor(grosor);
					f.addPunto(vert);
				}
				
				lyMan.addFigura(f);
			
			}
			else
			{
				dis.skipBytes(tamChunk);
			}
			tamFiguras -= HEAD_SIZE + tamChunk;
		}
		
		fis.close();
		
		Graf3View vw = (Graf3View) getView();
		vw.repaint();
		*/
	}
	
	
	public void save(String nombreArchivo)  throws FileNotFoundException, IOException {
		
		File archivo = new File(nombreArchivo);
		FileOutputStream fos = new FileOutputStream(archivo);
		DataOutputStream dos = new DataOutputStream(fos);
		
		/* tamaÃ±o del documento */
		dos.writeInt((int) size.getWidth());
		dos.writeInt((int) size.getHeight());
		
		/* numero de capas */
		int numCapas = lyMan.numLayers();
		dos.writeInt(numCapas);
		
		for(int i=0; i<numCapas; i++) {
			Layer l = lyMan.getLayer(i);
			
			/* numero de figuras */
			int numFiguras = l.numFiguras();
			dos.writeInt(numFiguras);
			
			for(int j=0; j<numFiguras; j++) {
				Figura f = l.getFigura(j);
				
				f.guardarFigura(dos);
			}
		}
		
		dos.close();

	}


	public void load(String nombreArchivo)  throws FileNotFoundException, IOException {
		
		File archivo = new File(nombreArchivo);
		FileInputStream fis = new FileInputStream(archivo);
		DataInputStream dis = new DataInputStream(fis);
		
		/* borrar documento */
		lyMan.borrarTodo();
		System.gc();
		
		/* tamaÃ±o del documento */
		int w = dis.readInt();
		int h = dis.readInt();
		size = new Dimension(w, h);
				
		/* numero capas */
		int numCapas = dis.readInt();
		System.out.println(numCapas + " capas");
		
		for(int i=0; i<numCapas; i++) {
			lyMan.newLayer("capa"+i);
			Layer l = lyMan.getLayer(i);
			
			/* numero de figuras */
			int numFiguras = dis.readInt();
			System.out.println("capa "+ i+": "+numFiguras+" figuras");
			/* Leer figuras */
			for(int j=0; j<numFiguras; j++) {
				Figura f;
				int tipo = dis.readInt();
				System.out.println("figura "+j +": tipo " + tipo);

				switch(tipo) {
				case Figura.FIGURA_RECTANGULO:
					System.out.println("Rectangulo");
					f = new FigRectangle(dis);
					break;
				case Figura.FIGURA_POLIGONO:
					System.out.println("Poligono");
					f = new FgPolygon(dis);
					break;
				case Figura.FIGURA_IMAGEN:
					System.out.println("Imagen");
					f = new FigImage(dis);
					break;
				case Figura.FIGURA_LINEA:
					System.out.println("linea");
					f = new Linea(dis);
					break;
				default:
					throw new IOException("Figura desconocida");
				}
				l.addFigura(f); 
			}
		}
		
		dis.close();
		
		/* redibujar */
		JCastiglioneView vw = (JCastiglioneView) getView();
		vw.repaint();
	}
	
	public LayerManager getLayerManager() {
		return lyMan;
	}
	
	public void exportSVG(String nombreArchivo) throws FileNotFoundException, IOException {
		System.out.println("Exportar a SVG");
		File file = new File(nombreArchivo);
		FileWriter fw = new FileWriter(file);
		
		int width = (int) (size.getWidth() * mpu);
		int height = (int) (size.getHeight() * mpu); 
		String head = "<svg width=\""+width+"mm\" height=\""+height+"mm\" version=\"1.1\" xmlns=\"http://www.w3.org/2000/svg\">\r\n";
		String end = "</svg>\r\n";

		fw.write(head, 0, head.length());
		
		/* datos */
		LayerManager lm = getLayerManager();
		Iterator itrL = lm.getLayerIterator();

		System.out.println("Escribiendo figuras:");
		int c = 0;
		while(itrL.hasNext()) {
			Layer l = (Layer) itrL.next();
			Iterator itrF = l.getFigureIterator();
			while(itrF.hasNext()) {
				System.out.println("figura: "+c);
				c++;
				Figura f = (Figura) itrF.next();
				/* escribir figura */
				switch(f.tipoFigura()) {
				case Figura.FIGURA_RECTANGULO:
					System.out.println("tipo=rectangulo");
					FigRectangle r = (FigRectangle) f;
					Rectangle2D r2d = r.getRect();
					double x = r2d.getMinX()*mpu;
					double y = r2d.getMinY()*mpu;
					double w = r2d.getWidth()*mpu;
					double h = r2d.getHeight()*mpu;
					String linea = "<rect x=\""+x+"mm\" y=\""+y+"mm\" width=\""+w+"mm\" height=\""+h+"mm\" style=\"fill:rgb(255,255,255);stroke-width:1; stroke:rgb(0,0,0)\"/>\r\n";
					fw.write(linea, 0, linea.length());
					break;
				}
			}
		}
		
		fw.write(end, 0, end.length());
		fw.close();
	}


	public void guardar(String nombreArchivo) throws FileNotFoundException, IOException
	{
		/*
		final int HEAD_SIZE = 8;
		final int FILE_HEAD =  0x01000100;
		final int SZCR_HEAD = 0x01000101;
		final int SZCR_SIZE = 10; //flags(2)+X(2)+Y(2)+R(4)
		final int EJES_DEFECTO = 0;
		final int FIGLIST_HEAD = 0x01000102;
		final int FIGURA_HEAD = 0x01000103;
		final int FIG_DATA_SIZE = 16; //flags(1) + color(3) + centro(8) + numVert(4)
		final int MIN_FIG_SIZE = HEAD_SIZE+FIG_DATA_SIZE; // 16
		final int VERTICE_SIZE = 12;
		
		ScrollRegion reg;
		//Graf2View v = (Graf2View) view;
		
		File archivo = new File(nombreArchivo);
		FileOutputStream fos = new FileOutputStream(archivo);
		DataOutputStream dos = new DataOutputStream(fos);
		// calcular longitud total y tamaÃ±o de la lista
		int tamLista = 0;
		
		
		// calcular tamaÃ±o
		Iterator itr = lstFiguras.iterator();
		
		while(itr.hasNext())
		{
			Figura f = (Figura) itr.next();
			tamLista += MIN_FIG_SIZE +f.numPuntos()*VERTICE_SIZE;
		}
			
		int tamTotal = 2*HEAD_SIZE+SZCR_SIZE+tamLista;
		
		// Escribir cabecera y longitud total
		dos.writeInt(FILE_HEAD);
		dos.writeInt(tamTotal);
		
		// Escribir tamaÃ±o y sistema de coordenadas
		dos.writeInt(SZCR_HEAD);
		dos.writeInt(SZCR_SIZE);
		
		dos.writeShort(EJES_DEFECTO);
		
		dos.writeShort(size.width);
		dos.writeShort(size.height);
		dos.writeShort(0); // Reservado
		dos.writeShort(0); // Reservado
		
		// Escribir lista de figuras
		dos.writeInt(FIGLIST_HEAD);
		dos.writeInt(tamLista);

		
		itr = lstFiguras.iterator();
		
		while(itr.hasNext())
		{
			Figura f = (Figura) itr.next();
			// Escribir datos de la figura
			dos.writeInt(FIGURA_HEAD);
			short tamFig = (short) (FIG_DATA_SIZE + f.numPuntos()*VERTICE_SIZE);
			dos.writeInt(tamFig);
			
			// datos
			// 1. flags
			byte flags = 0;
			if(!f.esTransp()) flags |= 1;
			if(!f.esVisible()) flags |= 2;
			dos.writeByte(flags);
			
			// 2. color
			Color clr = f.getColor();
			dos.writeByte((byte) clr.getRed());
			dos.writeByte((byte) clr.getGreen());
			dos.writeByte((byte) clr.getBlue());
			
			// 3.centro
			Vector2D centro = f.getCentroAbs();
			dos.writeFloat(centro.x);
			dos.writeFloat(centro.y);
			
			
			// 4. numVertices
			dos.writeInt((short) f.numPuntos());
			
			Iterator itrPuntos = f.getPuntos();
			while(itrPuntos.hasNext())
			{
				Punto p = (Punto) itrPuntos.next();
				if(p instanceof Vertice)
				{
					Vertice v = (Vertice) p;
					dos.writeByte(v.getGrosor());
				}
				else
				{
					byte grosor = 0;
					if(!p.esTransp()) grosor = 1;
					dos.writeByte(grosor);
				}
				
				clr = p.getColor();
				dos.writeByte((byte) clr.getRed());
				dos.writeByte((byte) clr.getGreen());
				dos.writeByte((byte) clr.getBlue());
					
				dos.writeFloat(p.getX());
				dos.writeFloat(p.getY());
			}
		}

		fos.close();
		*/
	}
	
	public void borrarTodo()
	{
		//lstFiguras.clear();
		lyMan.borrarTodo();
		lyMan.newLayer("fondo");
		System.gc();
	}
	
	void incrZ() {
		actZ++;
	}
	
	public SelectionItem alFrente(SelectionItem s) {
		return lyMan.alFrente(s);
	}
		
	public SelectionItem alFondo(SelectionItem s) {
		return lyMan.alFondo(s);
	}
	
	public SelectionItem avanzar(SelectionItem s) {
		return lyMan.avanzar(s);
	}
		
	public SelectionItem retroceder(SelectionItem s) {
		return lyMan.retroceder(s);
	}
	

	public void exportVideo(String total) {
		System.out.println("Exportar a video");
		
		int width = 600;
		int height = 400;
		
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		
		FFAnimationExport aexp = new FFAnimationExport(total);
		//PNGAnimationExport aexp = new PNGAnimationExport(total);
		Graphics g = img.getGraphics();
		for(int frame = 0; frame < animationLength; frame++){
			g.setColor(Color.white);
			g.fillRect(0,0,width,height);
			JCastiglioneView gvw = (JCastiglioneView) view;
			gvw.drawFiguresInFrame(this, g, frame);
			aexp.push(img);
		}
		aexp.close();
		
	}

	public void resizeAnimation(int frames) {
		// TODO Auto-generated method stub
		if(frames < this.animationLength) {
			throw new UnsupportedOperationException(
					"Reduce length: must cut keyframes");
		} else {
			animationLength = frames;
		}
		
		
	}
	
	public void copy(JCastiglioneDoc doc) {
	
		lyMan = doc.lyMan;
		
		selectNone();
		//TODO
		// copy selection
		
		//throw new UnsupportedOperationException("Copy document");
	}

	
}