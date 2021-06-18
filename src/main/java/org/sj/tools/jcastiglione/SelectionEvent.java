package org.sj.tools.jcastiglione;

public class SelectionEvent {
	Object selected;
	public SelectionEvent(Object obj) {
		selected = obj;
	}
	
	public Object getSelected() {
		return selected;
	}
}