package org.sj.tools.jcastiglione.figure;

import java.awt.Image;
import math.geom.Vector2D;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;

import java.io.File;
import java.io.FileOutputStream;

import javax.imageio.ImageIO;

import org.sj.tools.jcastiglione.ScrollConverter;


public class FigImage extends FigRectBounded {
	
	protected Image imgData;
	
	/* nombre del archivo */
	private String nombre;
	
	
	/** El constructor carga la imagen
	 * @param cent			Vector2D centro del dibujo
	 * @param filename	 String con el nombre del archivo
	 *							que contiene la imagen
	 */
	public FigImage(Vector2D topleft, String filename) throws IOException {
			
		nombre = filename;
		/* cargar imagen y guardar en img */
		//try {
			System.out.println("ARCHIVO:"+filename);
			File imgFile = new File(filename);
			imgData = ImageIO.read(imgFile);
		
			if(imgData == null) {
				System.out.println("img NULL");
				return;
			}
			Rectangle2D r = new Rectangle2D.Float(topleft.getX(),topleft.getY(), imgData.getWidth(null), imgData.getHeight(null));
			init(r);


		/*} catch(Exception ex) {
			System.out.println("Fallo cargando imagen: \n"+ex.toString());
			imgData = null;
		}*/
		
		
	}
	

	public FigImage(BufferedImage img, Rectangle2D bounds) {
		init(bounds);
		imgData = img;		
	}

	
	public FigImage(FigImage i) {
		super(i);
		copyImg(i);		
	}
	
	public void copyImg(FigImage fig) {
		imgData = fig.imgData;
	}
	
	public void setImage(BufferedImage img) {
		imgData = img;
	}
	

	/**
	 * Constructor que genera la imagen a partir de un archivo guardado
	 * @param dis DataInputStream de donde se obtienen los datos
	 */
	public FigImage(DataInputStream dis) throws IOException {
		super(dis);
		
	}
	
	
	public int tipoFigura() {
		return FIGURA_IMAGEN;
	}

	
	public void draw(Graphics g, ScrollConverter sc) {
		/* calcular esquina superior izquierda */
		Rectangle2D tr = rect.getDefaultValue();
		Point tl = sc.AbsToScreen(new Vector2D((float) tr.getX(), (float) tr.getY()));
		int w = sc.LogicToPixel(imgData.getWidth(null));
		int h = sc.LogicToPixel(imgData.getHeight(null));
		
		if(lineaVisible()) {
			g.setColor(getColorLinea());
			g.drawRect((int) tl.getX(), (int) tl.getY(), w, h);
		}

		/* calcular escala */
		/* dibujar */
		if(imgData == null) {
			g.drawString("<NULL>", (int) tl.getX(), (int) tl.getY());
			return;
		}
		
	
		if(sc.getAmpliacion() == 1) {
			g.drawImage(imgData, (int) tl.getX(), (int) tl.getY(), null);
		} else {
			g.drawImage(imgData, (int) tl.getX(), (int) tl.getY(), w, h, null);
		}
	}
	
	public void draw(Graphics g, ScrollConverter sc, int time) {
		// TODO
		if(!esVisible()) return;
		Rectangle2D tr = rect.getValue(time);
		Point tl = sc.AbsToScreen(new Vector2D((float) tr.getX(), (float) tr.getY()));
		int w = sc.LogicToPixel(tr.getWidth());
		int h = sc.LogicToPixel(tr.getHeight());

		if(lineaVisible()) {
			g.setColor(getColorLinea());
			g.drawRect((int) tl.getX(), (int) tl.getY(), w, h);
		}
		
		if(sc.getAmpliacion() == 1) {
			g.drawImage(imgData, (int) tl.getX(), (int) tl.getY(), null);
		} else {
			g.drawImage(imgData, (int) tl.getX(), (int) tl.getY(), w, h, null);
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

	public void saveImage(String filename) throws IOException {
		if(imgData instanceof RenderedImage) {
			File f = new File(filename);
			FileOutputStream fos = new FileOutputStream(f);
			RenderedImage ri = (RenderedImage) imgData;
			ImageIO.write(ri, "png", fos);
		} else { 
			throw new UnsupportedOperationException("Not a raster image.");
		}
	}

	public boolean equals(FigImage fig) {
		if(!getBounds().equals(fig.getBounds())) return false;
		if(!imgData.equals(fig.imgData)) return false;
		//TODO: animation, etc
		return true;
	}
	/**
	 * Guarda los datos de este poligono
	 */	
	protected void guardar(DataOutputStream dos) throws IOException {
	}
	
}