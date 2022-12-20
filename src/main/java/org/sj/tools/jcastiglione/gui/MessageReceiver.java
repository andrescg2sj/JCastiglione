package org.sj.tools.jcastiglione.gui;

public interface MessageReceiver {
	public void sendMessage(int msgIndex, Object obj);
	public Object requestObject(int objIndex);
}