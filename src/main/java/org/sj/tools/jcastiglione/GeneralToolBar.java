package org.sj.tools.jcastiglione;

import java.awt.Panel;
import java.awt.Button;
import java.awt.Scrollbar;
import java.awt.Label;
import java.awt.Checkbox;
import java.awt.Choice;
import java.awt.Dimension;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.ItemListener;

import org.sj.tools.jcastiglione.dialog.AnimationDialog;

import java.awt.event.ItemEvent;


public class GeneralToolBar extends Panel implements ActionListener, ItemListener, AdjustmentListener {
	public static final String ETI_INCREASE = "+";
	public static final String ETI_DECREASE = "-";
	public static final String ETI_ADJUST = "1:1";
	public static final String ETI_NEW = "Nuevo";
	public static final String ETI_SELEC = "Seleccion";
	public static final String ETI_NEXT = ">";
	public static final String ETI_PREVIOUS = "<";
	public static final String ETI_DELETE = "Borrar";
	
	public static final String ETI_ANIMATE = "Animar";
	public static final String ETI_ANIMSETTINGS = "Ajustes...";
	
	//private ProgVent padre;
	private JCastiglioneView view;
	private JCastiglioneDoc doc;
	private Frame parentWin;

	private Button btnIncrease;
	private Button btnDecrease;
	private Button btnAdjust;
	private Button btnNObj;
	private Button btnSelec;
	private Button btnNext;
	private Button btnPrevious;
	private Button btnDelete;
	private Choice figureType;
	private Scrollbar sbFrame;
	private Label lblFrame;
	private Button btnAnimSets;
	
	
	private Checkbox cbAnima;
	
	
	public GeneralToolBar(JCastiglioneDoc d, JCastiglioneView v, Frame pwin) {
		view = v;
		doc = d;
		parentWin = pwin;
		
		/* Construir componentes */
		btnIncrease = new Button(ETI_INCREASE);
		btnDecrease = new Button(ETI_DECREASE);
		btnAdjust = new Button(ETI_ADJUST);
		btnNObj = new Button(ETI_NEW);
		btnSelec = new Button(ETI_SELEC);
		btnNext = new Button(ETI_NEXT);
		btnPrevious = new Button(ETI_PREVIOUS);
		btnDelete = new Button(ETI_DELETE);
				
		figureType = new Choice();
		figureType.add("Rectangulo");
		figureType.add("Linea");
		figureType.add("Poligono");
		figureType.add("Imagen");
		figureType.add("Texto");
		
		cbAnima = new Checkbox(ETI_ANIMATE);
		btnAnimSets = new Button(ETI_ANIMSETTINGS);

		/* Añadir componentes */
		btnIncrease.addActionListener(this);
		btnDecrease.addActionListener(this);
		btnAdjust.addActionListener(this);
		btnNObj.addActionListener(this);
		btnSelec.addActionListener(this);
		btnNext.addActionListener(this);
		btnPrevious.addActionListener(this);
		btnDelete.addActionListener(this);
		
		cbAnima.addItemListener(this);
		
		/* Añadir componentes */
		Panel up = new Panel();
		Panel down = new Panel();
		GridLayout gl = new GridLayout(2,1);
		setLayout(gl);
		
		//FlowLayout fl = new FlowLayout();
		up.setLayout(new FlowLayout());
		down.setLayout(new FlowLayout());

		down.add(btnIncrease);
		down.add(btnDecrease);
		down.add(btnAdjust);

		down.add(figureType);
		down.add(btnNObj);
		down.add(btnSelec);
		down.add(btnNext);
		down.add(btnPrevious);
		down.add(btnDelete);
		
		sbFrame = new Scrollbar(Scrollbar.HORIZONTAL, 0, 1, 0, doc.getAnimLength());
		//sbFrame.set
		lblFrame = new Label("1");
		sbFrame.setPreferredSize(new Dimension(200, 20));
		sbFrame.addAdjustmentListener(this);
		up.add(sbFrame);
		up.add(lblFrame);
		up.add(cbAnima);
		up.add(btnAnimSets);
		
		btnAnimSets.addActionListener(new ActionListener() {


	         public void actionPerformed(ActionEvent e) {
		    	 
	            AnimationDialog animdlg = new AnimationDialog(parentWin, 
	            		doc.getAnimLength());
	            animdlg.setVisible(true);
	            if(animdlg.getFrames() != doc.getAnimLength())
	            {
	            	doc.resizeAnimation(animdlg.getFrames());
	            }
	         }
	      });
		
		add(up);
		add(down);
	}
	
	
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if(obj instanceof Button) {
			Button b = (Button) obj;
			String label = b.getLabel();
			
			if(label.equals(ETI_INCREASE)) {
				view.sendMessage(JCastiglioneView.MSG_AUMENTAR, null);
		
			} else if(label.equals(ETI_DECREASE)) {
				view.sendMessage(JCastiglioneView.MSG_DISMINUIR, null);
			
			} else if(label.equals(ETI_ADJUST)) {
			
			} else if(label.equals(ETI_NEW)) {
				String tipo = figureType.getSelectedItem();
				if(tipo.equals("Rectangulo")) {
					view.setAccion(JCastiglioneView.AC_NEW_RECT);
				} else if(tipo.equals("Poligono")) {
					view.setAccion(JCastiglioneView.AC_NEW_POLI);
				} else if(tipo.equals("Imagen")) {
					view.setAccion(JCastiglioneView.AC_NEW_IMG);
				} else if(tipo.equals("Linea")) {
					view.setAccion(JCastiglioneView.AC_NEW_LINEA);
				} else if(tipo.equals("Texto")) {
					view.setAccion(JCastiglioneView.AC_NEW_TEXT);
				}
				
				

			} else if(label.equals(ETI_SELEC)) {
				view.setAccion(JCastiglioneView.AC_SELECCIONAR);
			} else if(label.equals(ETI_NEXT)) {
				JCastiglioneView v = (JCastiglioneView) view;
				v.nextSelection();
			} else if(label.equals(ETI_PREVIOUS)) {
				JCastiglioneView v = (JCastiglioneView) view;
				v.previousSelection();
			} else if(label.equals(ETI_DELETE)) {
				view.setAccion(JCastiglioneView.AC_BORRAR);
			}
		}
		}
		
	public void adjustmentValueChanged(AdjustmentEvent e)
	{
		if(e.getSource() == sbFrame) {
			String text = "" + sbFrame.getValue();
			lblFrame.setText(text);
			view.setTime(e.getValue());
		}
	}
	
	public void itemStateChanged(ItemEvent e) {
		if(e.getSource() == cbAnima) {
			view.setAnimateStatus(cbAnima.getState());
		}
	}
	
	

}