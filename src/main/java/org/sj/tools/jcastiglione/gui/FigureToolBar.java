package org.sj.tools.jcastiglione.gui;

import java.util.Iterator;

import org.sj.tools.jcastiglione.JCastiglioneDoc;
import org.sj.tools.jcastiglione.figure.FgPolygon;
import org.sj.tools.jcastiglione.figure.Figura;

import java.awt.Panel;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.ScrollPane;
import java.awt.Container;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class FigureToolBar extends Panel implements ItemListener, ActionListener, SelectionListener {
	//private ProgVent padre;
	private JCastiglioneView view;
	private JCastiglioneDoc doc;
	
	private Checkbox cbVisible;
	private Checkbox cbShowLine;
	private Checkbox cbShowBackgnd;
	
	private ColorButton btnClrLine;
	private ColorButton btnClrBackgnd;
	
	private Button btnForegnd;
	private Button btnAdvance;
	private Button btnBackwards;
	private Button btnBackgnd;
	
	/** 
	 * container en que irÃ¡n los controles de acciones especÃ­ficas para
	 * cada tipo de figura
	 */
	private Container contSpecific;
	/**
	 * Panel de Scroll para mostrar todos los elementos del container
	 */
	private ScrollPane scroll;
	
	
	public FigureToolBar(JCastiglioneDoc d, JCastiglioneView v) {
		doc = d;
		view = v;
		
		/* Construir componentes */
		btnClrLine = new ColorButton(Color.BLACK);
		btnClrBackgnd = new ColorButton(Color.WHITE);
		cbVisible = new Checkbox("Visible", true);
		cbShowLine = new Checkbox("Linea", true);
		cbShowBackgnd = new Checkbox("Fondo", true);
		btnForegnd = new Button("Al frente");
		btnBackgnd = new Button("Al fondo");
		btnAdvance = new Button("Avanzar");
		btnBackwards = new Button("Retroceder");
		
		scroll = new ScrollPane();
		contSpecific = new Container();
		
		
		/* AÃ±adir componentes */
		FlowLayout fl = new FlowLayout();
		setLayout(fl);
		
		add(btnClrLine);
		add(btnClrBackgnd);
		add(cbShowLine);
		add(cbShowBackgnd);
		add(cbVisible);
		add(btnForegnd);
		add(btnBackgnd);
		add(btnAdvance);
		add(btnBackwards);
		add(scroll);
		scroll.add(contSpecific);
		contSpecific.setLayout(new GridLayout(10,1));
		
		/* escuchadores */
		btnClrLine.addActionListener(this);
		btnClrBackgnd.addActionListener(this);
		btnAdvance.addActionListener(this);
		btnBackwards.addActionListener(this);
		btnForegnd.addActionListener(this);
		btnBackgnd.addActionListener(this);

		cbVisible.addItemListener(this);
		cbShowLine.addItemListener(this);
		cbShowBackgnd.addItemListener(this);
	}
	

	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if(obj instanceof Button) {
			Button b = (Button) obj;
			String label = b.getLabel();
			
			/* generales */
			if(obj == btnForegnd) {
				JCastiglioneView v = (JCastiglioneView) view;
				v.sendToFront();
			} else if(obj == btnBackgnd) {
				JCastiglioneView v = (JCastiglioneView) view;
				v.sentToBackgnd();
			} else if(obj == btnAdvance) {
				JCastiglioneView v = (JCastiglioneView) view;
				v.advance();
			} else if(obj == btnBackwards) {
				JCastiglioneView v = (JCastiglioneView) view;
				v.goBack();
			} else if(label.equals("Nuevo vertice")) { /* especificos */
				view.setAccion(JCastiglioneView.AC_ADD_VERT);
		
			} else if(label.equals("Editar")) {
				view.setAccion(JCastiglioneView.AC_EDIT_VERT);
			
			} else if(label.equals("Borrar vertice")) {
				view.setAccion(JCastiglioneView.AC_DEL_VERT);
			}
		} else if(obj == btnClrLine) {
			Color c = btnClrLine.getColor();
			/* esto hay que cambiarlo */
			Iterator itr = view.getSelected();
			while(itr.hasNext()) {
				Figura f = (Figura) itr.next();
				f.setColorLinea(c);
			}
			view.repaint();
		} else if(obj == btnClrBackgnd) {
			System.out.println("llamada");
			Color c = btnClrBackgnd.getColor();
			/* esto hay que cambiarlo: utilizando una interface de Selection
				(o algo asÃ­) */
			Iterator itr = view.getSelected();
			while(itr.hasNext()) {
				System.out.println("Asignando color");
				Figura f = (Figura) itr.next();
				f.setColorFondo(c);
			}
			view.repaint();
		}
	}
	
	public void itemStateChanged(ItemEvent e) {
		Object obj = e.getSource();
		if(obj instanceof Checkbox) {
			Checkbox c = (Checkbox) obj;
			String label = c.getLabel();
			
			if(label.equals("Visible")) {
				/* esto hay que cambiarlo */
				Iterator itr = view.getSelected();
				while(itr.hasNext()) {
					Figura f = (Figura) itr.next();
					//f.setVisible(c.getState());
				}
				view.repaint();
		
			} else if(label.equals("Linea")) {
				/* esto hay que cambiarlo */
				Iterator itr = view.getSelected();
				while(itr.hasNext()) {
					Figura f = (Figura) itr.next();
					f.setVerLinea(c.getState());
				}
				view.repaint();
			
			} else if(label.equals("Fondo")) {
				/* esto hay que cambiarlo */
				Iterator itr = view.getSelected();
				while(itr.hasNext()) {
					Figura f = (Figura) itr.next();
					f.setVerFondo(c.getState());
				}
				view.repaint();
			 } else if(label.equals("Cerrado")) {
				/* esto hay que cambiarlo */
				Iterator itr = view.getSelected();
				while(itr.hasNext()) {
					Figura f = (Figura) itr.next();
					FgPolygon p = (FgPolygon) f;
					p.setCerrado(c.getState());
				}
				view.repaint();
			}
				
		}

	}
	

	public Dimension getPreferredSize() {
		return new Dimension(100, 100);
	}
	
	/*public void paint(Graphics g) {
		Dimension d = getSize();
		int w = (int) d.getWidth();
		int h = (int) d.getHeight();
		int N = 10;
		for(int i=0; i<N;i++) {
			g.drawLine(i*w/N, 0,i*w/N, h);
		}
		for(int j=0; j<N;j++) {
			g.drawLine(0, j*h/N, w, j*h/N);
		}
	}*/
	
	
	/**
	 * crea los controles necesarios para manejar la figura
	 * seleccionada
	 */
	public void onNewSelection(SelectionEvent e) {
		
		Object obj = e.getSelected();
		if(!(obj instanceof Figura)) return;
		
		Figura f = (Figura) obj;
		
		/* actualizar parÃ¡metros */
		cbShowLine.setState(f.lineaVisible());
		cbShowBackgnd.setState(f.fondoVisible());
		cbVisible.setState(f.esVisible());
		
		/* reiniciar contenedor */
		contSpecific.removeAll();
		
		if(f instanceof FgPolygon) {
			( (GridLayout) contSpecific.getLayout() ).setRows(4);
			Button btnNewVert = new Button("Nuevo vertice");
			Button btnEditar = new Button("Editar");
			Button btnDelVert = new Button("Borrar vertice");
			Checkbox cbCerrado = new Checkbox("Cerrado");
			contSpecific.add(btnNewVert);
			contSpecific.add(btnEditar);
			contSpecific.add(btnDelVert);
			contSpecific.add(cbCerrado);
			btnNewVert.addActionListener(this);
			btnEditar.addActionListener(this);
			btnDelVert.addActionListener(this);
			cbCerrado.addItemListener(this);
			
			scroll.validate();
			
			
		}
	}
	
	
	
	public void onNoneSelected(SelectionEvent e) {
		
		contSpecific.removeAll();
		scroll.validate();
		
	}

	
	
	
	

}