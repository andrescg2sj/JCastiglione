package org.sj.tools.jcastiglione;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import org.sj.tools.jcastiglione.dialog.TextInputDlg;
import org.sj.tools.jcastiglione.figure.FgPolygon;
import org.sj.tools.jcastiglione.figure.FigText;

import math.geom.Vector2D;

public class MouseDispather extends MouseAdapter implements MouseMotionListener, ViewIdentifiers
{
	JCastiglioneView view;
	Doc document;
	Selection selection;
	
	/* atributos relacionados con arrastre */
	Point ptAnterior, ptInicial, ptSiguiente;
	
	public MouseDispather(JCastiglioneView v)
	{
		view = v;
		selection = view.selection;
	}
	
	public void mousePressed(MouseEvent e)
	{
		LogicScreen ls = view.getPressedPoint(e);
		int i=0; // contador general
		
		
		Graphics g;
		switch(view.getCurrAction())
		{
		case AC_NEW_LINEA:
			ptInicial = ls.p;
			ptAnterior = ptInicial;
			break;
		case AC_NEW_RECT:
			ptInicial = ls.p;
			ptAnterior = ptInicial;
			break;
		case AC_NEW_TEXT:
			ptInicial = ls.p;
			ptAnterior = ptInicial;
			break;
		case AC_NEW_POLI:
			view.newPoly(e);
			
			break;
		case AC_NEW_IMG:
			view.createNewImage(ls);
			view.repaint();
			view.setAccion(AC_NINGUNA);
			break;
		
		case AC_NEW_VERT:
			if(e.getButton() == e.BUTTON1) {
				document.sendMessage(JCastiglioneDoc.MSG_NEW_VERT, ls.p);
				System.out.println("VERTICE NUEVO");
			} else {
				document.sendMessage(JCastiglioneDoc.MSG_CLOSE_FIGURE, null);
				view.repaint();
				System.out.println("OBJETO CERRADO");
				view.setAccion(AC_SELECCIONAR);
			}

			break;
		case AC_ADD_VERT:
			view.actAddVert(ls);
			break;
		case AC_EDIT_VERT:
			view.actEditVert(ls);
			break;
		case AC_DEL_VERT:
			view.actDelVert(ls);
			break;

		case AC_SELECCIONAR:
			view.actSelect(ls, e.getPoint());
			break;

		case AC_EDIT_TEXT:
			/* */
			//TODO: set Text
			view.setAccion(AC_SELECCIONAR);
			break;

		}
	}
	
	public void mouseClicked(MouseEvent e)
	{
		if(e.getClickCount() == 2) {
			if(selection.anySelected) {
				// if text element, show input box
				// TextField ?
				if(selection.figuraSelected instanceof FigText) {
					//view.setAccion(AC_EDIT_TEXT);
					//TODO: show input box or editable text field over figure
					Frame f = JCastiglioneApp.getInstance().getFrame();
					FigText ft = (FigText) selection.figuraSelected;
					
					TextInputDlg tidlg = new TextInputDlg(f,ft.getText());
					tidlg.setVisible(true);
					if(tidlg.getStatus()){
						ft.setText(tidlg.getText());
						view.repaint();
					}
				}
			}
		}
	}
	
	public void mouseReleased(MouseEvent e)
	{
		LogicScreen ls = view.getPressedPoint(e);
		
		switch(view.getCurrAction())
		{
		case AC_NEW_LINEA:
			view.releaseNewLine(ls);
			view.setAccion(AC_NINGUNA);
			break;
		case AC_NEW_RECT:
			view.releaseNewRect(ls.p);
			view.setAccion(AC_NINGUNA);
			break;
		case AC_NEW_TEXT:
			view.releaseNewText(ls.p);
			view.setAccion(AC_NINGUNA);
			break;
		case AC_SEL_MOVER:
			view.releaseMove(ls.p);				
			break;
		case AC_SELEC_DRAG:
			view.releaseSelecDrag(ls.p);
			break;
		case AC_MOVE_VERT: /* modificar un vertice de un poligono */
			FgPolygon pol = (FgPolygon) selection.figuraSelected;
			pol.setVertice(selection.vertice, ls.v);
			view.setAccion(AC_EDIT_VERT);
			view.repaint();
			break;
		}
	}
		
	public void mouseDragged(MouseEvent e)
	{
		Graphics g;
		Point p = e.getPoint();	/* punto en que se ha pulsado */
		Vector2D v = view.getScrollRegion().ScreenToAbs(p); /* coordenadas lÃ³gicas de superficie */
		
		
		
		if(view.isSnapToGridOn()) {
			v = view.snapToGrid(v);
			p = view.getScrollRegion().AbsToScreen(v);
		}
		
		switch(view.getCurrAction()) {
		case AC_NEW_LINEA:
			g = view.getGraphics();
			if (g != null) {
				g.setXORMode(Color.GRAY);
				g.drawLine((int)ptInicial.getX(), (int)ptInicial.getY(),(int) ptAnterior.getX(), (int) ptAnterior.getY());
				g.drawLine((int)ptInicial.getX(), (int)ptInicial.getY(),(int) p.getX(), (int) p.getY());
			}
			ptAnterior = p;
			break;
		case AC_NEW_RECT:
			g = view.getGraphics();
			if (g != null) {
				g.setXORMode(Color.GRAY);
				int pre_w = (int) (ptAnterior.getX() - ptInicial.getX());
				int pre_h = (int) (ptAnterior.getY() - ptInicial.getY());
				int new_w = (int) (p.getX() - ptInicial.getX());
				int new_h = (int) (p.getY() - ptInicial.getY());
				g.drawRect((int)ptInicial.getX(), (int)ptInicial.getY(), pre_w, pre_h);
				g.drawRect((int)ptInicial.getX(), (int)ptInicial.getY(), new_w, new_h);
			}
			ptAnterior = p;
			break;
		case AC_SEL_MOVER:
			g = view.getGraphics();
			if (g != null) {
				g.setXORMode(Color.GRAY);
				/* dibujar rectangulo*/
				if(selection == null) throw new NullPointerException("Selection");
				if(selection.contorno == null) throw new NullPointerException("contorno");
				g.drawRect(selection.contorno.x, selection.contorno.y, 
						selection.contorno.width, selection.contorno.height);

				/* calcular desplazamiento */
				int dx, dy;
				dx = (int) (p.getX()-ptAnterior.getX());
				dy = (int) (p.getY()-ptAnterior.getY());
				ptAnterior = p;
				selection.contorno.x += dx;
				selection.contorno.y += dy;
			
				/* dibujar rectangulo*/
				g.drawRect(selection.contorno.x, selection.contorno.y, selection.contorno.width, selection.contorno.height);
			}
			
			break;
		case AC_SELEC_DRAG:
			g = view.getGraphics();
			if (g != null) {
				g.setXORMode(Color.GRAY);
				int pre_w = (int) (ptAnterior.getX() - ptInicial.getX());
				int pre_h = (int) (ptAnterior.getY() - ptInicial.getY());
				int new_w = (int) (p.getX() - ptInicial.getX());
				int new_h = (int) (p.getY() - ptInicial.getY());
				g.drawRect((int)ptInicial.getX(), (int)ptInicial.getY(), pre_w, pre_h);
				g.drawRect((int)ptInicial.getX(), (int)ptInicial.getY(), new_w, new_h);
			}
			ptAnterior = p;
			
			break;
		case AC_MOVE_VERT: /* modificar un vertice de un poligono */
			
			//setAccion(AC_EDIT_VERT);
			break;

		}
	}
	
	
	public void mouseMoved(MouseEvent e)
	{
	;
	}
		
}

