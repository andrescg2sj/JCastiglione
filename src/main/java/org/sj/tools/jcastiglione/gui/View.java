package org.sj.tools.jcastiglione.gui;

public interface View extends MessageReceiver {
	public Object getDoc();
	public void setDoc(Object o);
}