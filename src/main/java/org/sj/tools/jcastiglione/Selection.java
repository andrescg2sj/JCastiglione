package org.sj.tools.jcastiglione;

import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.Vector;

import org.sj.tools.jcastiglione.figure.Figura;

public class Selection {
	
	public static final int MANEJ_LADO = 5;
	
	boolean anySelected = false;
	//private int indexSelected = -1;
	SelectionItem selected;
	Figura figuraSelected = null;
	Rectangle contorno;
	Rectangle manejador[];
	Vector selectionListeners;
	LinkedList vertices;
	int vertice; // vertice que se estÃ¡ editando en un polÃ­gono
	
	public Selection(){
		
		
	}
	
	/**
	 * Calcula el tamaÃ±o y posiciÃ³n de unos pequeÃ±os rectÃ¡ngulos
	 * que aparecen alrededor de cada figura seleccionada
	 */
	void generaManejadores(ScrollRegion region, boolean animar, int currTime) {
		contorno = region.AbsToScreen( figuraSelected.getBounds(animar, currTime) );

		manejador = new Rectangle[8];
		manejador[0] = new Rectangle(contorno.x-MANEJ_LADO-1, contorno.y-MANEJ_LADO-1,
												MANEJ_LADO, MANEJ_LADO);
		manejador[1] = new Rectangle(contorno.x+contorno.width+1, contorno.y-MANEJ_LADO-1,
												MANEJ_LADO, MANEJ_LADO);
		manejador[2] = new Rectangle(contorno.x+contorno.width+1, contorno.y+contorno.height+1,
												MANEJ_LADO, MANEJ_LADO);
		manejador[3] = new Rectangle(contorno.x-MANEJ_LADO-1, contorno.y+contorno.height+1,
												MANEJ_LADO, MANEJ_LADO);
		
	}

}
