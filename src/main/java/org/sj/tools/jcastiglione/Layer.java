package org.sj.tools.jcastiglione;


import java.util.LinkedList;

import org.sj.tools.jcastiglione.figure.Figura;

import java.util.Iterator;

/** 
 * Representa una capa de un dibujo vectorial 
 * Las capas son agrupaciones de figuras. 
 * Las capas se dibujan por orden. Las primeras son las
 * que estÃ¡n mÃ¡s atras, ocultas por las Ãºltimas.
 *
 */
public class Layer {
	/** lista de figuras contenidas en esta capa */
	private LinkedList<Figura> lstFiguras;
	
	/** true si la capa debe mostrarse */
	private boolean visible;
	
	/** true si la capa puede ser modificada */
	private boolean activa;
	
	/** nombre de la capa */
	private String nombre;
	
	/**
	 * Constructor
	 *
	 * @param n String con el nombre de la capa
	 * @param a boolean que indica si estÃ¡ activa
	 * @param v boolean que indica si es visible
	 */
	public Layer(String n, boolean a, boolean v) {
		nombre = n;
		activa = a;
		visible = v;
		lstFiguras = new LinkedList<Figura>();
	}
	
	
	public int numFiguras() {
		return lstFiguras.size();
	}
	
	public void addFigura(Figura f) {
		//lstFiguras.addLast(f);
		lstFiguras.add(f);
	}
	
	public Figura getFigura(int index) {
		return (Figura) lstFiguras.get(index);
	}

	public Figura borrarFigura(int index) {
		return (Figura) lstFiguras.remove(index);
	}


	
	public void setVisible(boolean v) {
		visible = v;
	}
	
	public boolean esVisible() {
		return visible;
	}

	public void setActiva(boolean a) {
		activa = a;
	}
	
	public boolean esActiva() {
		return activa;
	}
	
	/**
	 * Devuelve un iterador para recorrer la lista de figuras
	 *
	 * @return iterator con la lista de figuras
	 */
	public Iterator<Figura> getFigureIterator() {
		return lstFiguras.iterator();
	}
	
	public Iterable<Figura> figures()
	{
		return lstFiguras;
	}
	
	public int alFrente(int i) {
		Figura f = (Figura) lstFiguras.remove(i);
		lstFiguras.addLast(f);
		return lstFiguras.size()-1;
	}

	public int avanzar(int i) {
		if(i >= lstFiguras.size()-1) return lstFiguras.size()-1;
		Figura f = (Figura) lstFiguras.remove(i);
		lstFiguras.add(i+1, f);
		return i+1;
	}

	public int retroceder(int i) {
		if(i <= 0) return 0;
		Figura f = (Figura) lstFiguras.remove(i);
		lstFiguras.add(i-1, f);
		return i-1;
	}

	public int alFrondo(int i) {
		Figura f = (Figura) lstFiguras.remove(i);
		lstFiguras.addFirst(f);
		return 0;
	}


	public String getName() {
		// TODO Auto-generated method stub
		return nombre;
	}

}