package org.sj.tools.jcastiglione;

import java.util.Vector;

import org.sj.tools.jcastiglione.figure.Figura;
import org.sj.tools.jcastiglione.gui.SelectionItem;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 *
 * sirve para recorrer todas las figuras de una lista de capas 
 */
class FiguraIterator implements Iterator {
	private Iterator itrLayers;
	private Iterator itrFiguras;
	private Layer actual;
	private boolean bNext;
	

	public FiguraIterator(Iterator ly) {
		itrLayers = ly;
		bNext = false;
		if(!itrLayers.hasNext()) {
			return;
		}
		
		Layer l = (Layer) itrLayers.next();
		itrFiguras = l.getFigureIterator();
		
		while(itrLayers.hasNext() && !itrFiguras.hasNext()) {
			l = (Layer) itrLayers.next();
			itrFiguras = l.getFigureIterator();
		}
		if(!itrFiguras.hasNext()) {
			bNext = false;
		}
		
		
	}
	
	
	public Object next() throws NoSuchElementException {
		if(!bNext) {
			throw new NoSuchElementException("no quedan figuras");
		}
		Object obj = itrFiguras.next();
		
		if(!itrFiguras.hasNext()) {
			Layer l;
			while(itrLayers.hasNext() && !itrFiguras.hasNext()) {
				l = (Layer) itrLayers.next();
				itrFiguras = l.getFigureIterator();
			}
			if(!itrFiguras.hasNext()) {
				bNext = false;
			}
		}
		return obj;
	}
	
	public boolean hasNext() {
		return bNext;
	}
	
	public void remove() {
	}
		
}

/**
 * Gestor de capas
 * @see Layer
 */
public class LayerManager {
	
	/** lista de capas */
	private Vector lstLayers;
	
	/** indice de la capa selecionada */
	private int idActual;
	
	public LayerManager() {
		lstLayers = new Vector(10, 5);
	}
	
	/**
	 * Crea una nueva capa 
	 *
	 * @param nombre String con el nombre de la nueva capa
	 */
	public void newLayer(String nombre) {
		lstLayers.add(new Layer(nombre, true, true));
		setCurrentLast();
	}
	
	public int numLayers() {
		return lstLayers.size();
	}
	
	public void addLayers(List<Layer> layers) {
		for(Layer l: layers) {
			this.lstLayers.add(l);
		}
		setCurrentLast();
	}
	
	public void setCurrentLast() {
		setCurrent(lstLayers.size()-1);
	}

	
	/**
	 * Devuelve una referencia a la capa número index
	 *
	 * @param index int con el índice de la capa en la lista
	 * @throws ArrayIndexOutOfBoundsException
	 */
	public Layer getLayer(int index) throws ArrayIndexOutOfBoundsException {
		return (Layer) lstLayers.get(index);
	}
	
	/**
	 * devuelve una referencia a la capa seleccionada
	 */
	public Layer getCurrent() {
		return getLayer(idActual);
	}
	
	public void setCurrent(int index) {
		idActual = index;
	}
	
	public Iterator getLayerIterator() {
		return lstLayers.iterator();
	}
	
	public Iterator getFiguraIterator() {
		return new FiguraIterator(lstLayers.iterator());
	}
	
	public void borrarTodo() {
		lstLayers.removeAllElements();
	}
	
	public Figura getFigura(int layer, int figura) {
		Layer l = getLayer(layer);
		return l.getFigura(figura);
	}

	public void borrarFigura(int layer, int figura) {
		Layer l = getLayer(layer);
		l.borrarFigura(figura);
	}

	public void borrarSeleccion(SelectionItem s) {
		/* falta implementar */
	}
	
	public SelectionItem alFrente(SelectionItem s) {
		Layer l = getLayer(s.getIdLayer());
		int i = l.alFrente(s.getIdFigura());
		return new SelectionItem(s.getIdLayer(), i, l.getFigura(i));
	}

	public SelectionItem avanzar(SelectionItem s) {
		Layer l = getLayer(s.getIdLayer());
		int i = l.avanzar(s.getIdFigura());
		return new SelectionItem(s.getIdLayer(), i, l.getFigura(i));
	}

	public SelectionItem retroceder(SelectionItem s) {
		Layer l = getLayer(s.getIdLayer());
		int i = l.retroceder(s.getIdFigura());
		return new SelectionItem(s.getIdLayer(), i, l.getFigura(i));
	}
	
	public SelectionItem alFondo(SelectionItem s) {
		Layer l = getLayer(s.getIdLayer());
		int i = l.alFrondo(s.getIdFigura());
		return new SelectionItem(s.getIdLayer(), i, l.getFigura(i));
	}
	
	public SelectionItem getNextSelec(SelectionItem s) {
		Layer l = getLayer(s.getIdLayer());
		int i = (s.getIdFigura()+1) % l.numFiguras();
		return new SelectionItem(s.getIdLayer(), i, l.getFigura(i));
	}

	public SelectionItem getPrevSelec(SelectionItem s) {
		Layer l = getLayer(s.getIdLayer());
		int i = s.getIdFigura()-1;
		if(i<0) i+=l.numFiguras();
		return new SelectionItem(s.getIdLayer(), i, l.getFigura(i));
	}

	
	/********************************
	 * interface con la capa activa *
	 ********************************/

	public void addFigura(Figura f) {
		Layer l = getCurrent();
		l.addFigura(f);
	}
	
	public Figura getFigura(int index) {
		Layer l = getCurrent();
		return (Figura) l.getFigura(index);
	}

	public void borrarFigura(int index) {
		Layer l = getCurrent();
		
	}

	public void clearLayers() {
		// TODO Auto-generated method stub
		this.lstLayers.clear();
		idActual = -1;
		
	}
	
	


}