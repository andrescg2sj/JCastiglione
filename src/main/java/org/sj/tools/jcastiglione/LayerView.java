package org.sj.tools.jcastiglione;

import java.awt.Panel;
import java.awt.List;
import java.awt.Checkbox;
import java.awt.Dimension;
import java.awt.event.ItemListener;

import org.sj.tools.jcastiglione.gui.JCastiglioneView;

import java.awt.event.ItemEvent;
import java.awt.FlowLayout;

public class LayerView extends Panel implements ItemListener  {

	/** controla si la capa actual estÃ¡ activa */
	private Checkbox cbActiva;
	/** controla si la capa actual es visible */
	private Checkbox cbVisible;
	
	/** lista que muestra las capas existentes */
	private List verCapas;
	
	/** gestor de capas */
	private LayerManager lyMan;
	
	/** vista del documento */
	private JCastiglioneView view;
 	
	
	public LayerView(LayerManager lm, JCastiglioneView v) {
		lyMan = lm;
		view = v;
		setSize(130, 300);
		
		/* construir componentes */
		cbActiva = new Checkbox("Activa", true);
		cbVisible = new Checkbox("Visible", true);
		verCapas = new List();

		/* aÃ±adir listeners */
		cbActiva.addItemListener(this);
		cbVisible.addItemListener(this);
		
		setLayout(new FlowLayout());
		
		add(cbActiva);
		add(cbVisible);
		add(verCapas);
	}
	
	public Dimension getPreferredSize() {
		return getSize();
	}
	/**
	 * Atiende a los eventos de:
	 * - selecciÃ³n de un elemento de la lista de capas
	 * - modificaciÃ³n de un control Checkbox
	 */
	public void itemStateChanged(ItemEvent e) {
		Object obj = e.getItem();

		/* buscar selecciÃ³n en la lista */
		int i = verCapas.getSelectedIndex();
		if(i == -1) {
			return;
		}
		Layer l = lyMan.getLayer(i);
		

		if(obj instanceof Checkbox) { /* se pulso un check box */
			
			if(obj == cbActiva) {
				l.setActiva(cbActiva.getState());
				
			} else if(obj == cbVisible) {
				l.setVisible(cbVisible.getState());
			
			}
			
		} else if(obj instanceof String) { /* se pulsÃ³ un elemento de la lista */
			
			/* actualizar los valores de los controles Checkbox */
			cbActiva.setState(l.esActiva());
			cbVisible.setState(l.esVisible());
		}
	}
	
}