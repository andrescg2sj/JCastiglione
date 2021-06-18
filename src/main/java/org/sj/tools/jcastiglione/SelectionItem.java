package org.sj.tools.jcastiglione;

public class SelectionItem {
	Object elemento;
	int idCapa;
	int idFigura;
	
	public SelectionItem(int capa, int idfig, Object obj) {
		idCapa = capa;
		idFigura = idfig;
		elemento = obj;
	}
	
	public int getIdLayer() {
		return idCapa;
	}
	
	public int getIdFigura() {
		return idFigura;
	}
	
	public boolean isNothing() {
		return equals(NONE);
	}
	
	public boolean equals(SelectionItem s) {
		return (s.idFigura == idFigura) && (s.idCapa == idCapa) && (s.elemento == elemento);
	}


	public static final SelectionItem NONE = new SelectionItem(-1, -1, null);
}