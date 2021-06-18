package org.sj.tools.jcastiglione;

public interface View extends MessageReceiver {
	public Object getDoc();
	public void setDoc(Object o);
}