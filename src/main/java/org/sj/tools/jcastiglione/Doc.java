package org.sj.tools.jcastiglione;

import java.util.Iterator;

import org.sj.tools.jcastiglione.gui.MessageReceiver;

public interface Doc extends MessageReceiver {
	public Object getView();
	public void setView(Object o);
	public Iterator getIterator();
}