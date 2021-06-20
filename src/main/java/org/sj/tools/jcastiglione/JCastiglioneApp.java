package org.sj.tools.jcastiglione;

import java.awt.Dimension;
import java.awt.Frame;

public class JCastiglioneApp {
	// tamaÃ±o por defecto de la vista
	public static final int VIEW_WIDTH = 475;
	public static final int VIEW_HEIGHT = 450;
	
	JCastiglioneView mView;
	JCastiglioneDoc mDoc;
	ProgWnd pv;
	
	private static JCastiglioneApp instance;
	
	private static JCastiglioneApp init() {
		if(instance == null) {
			instance = new JCastiglioneApp();
			return instance;
		}
		throw new RuntimeException("App already initialized");
	}
	
	public static JCastiglioneApp getInstance() {
		if(instance != null) {
			return instance;
		}
		throw new RuntimeException("App not initialized.");
	}
	
	private JCastiglioneApp() {
		// crea el objeto vista y documento
		JCastiglioneView view = new JCastiglioneView(VIEW_WIDTH, VIEW_HEIGHT);
		Dimension d = new Dimension(VIEW_WIDTH, VIEW_HEIGHT); 
		JCastiglioneDoc doc = new JCastiglioneDoc(d);
		mDoc = doc;
		mView = view;
		// los comunica entre sÃ­
		view.setDoc(doc);
		doc.setView(view);
		
		// Crea la ventana principal y la muestra
		ProgWnd pv = new ProgWnd("JCastiglione", doc, view);
		pv.setVisible(true);
	}
	
	public Frame getFrame() {
		return pv;
	}
	
	
	public static void main(String arg[]) {

		JCastiglioneApp app = JCastiglioneApp.init();
		
	}
}