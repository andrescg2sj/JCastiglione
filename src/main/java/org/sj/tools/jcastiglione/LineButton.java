package org.sj.tools.jcastiglione;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Color;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Vector;

/**
 * Boton que guarda un color.
 */
public class LineButton extends Component implements MouseListener {
	public static final int W_SIZE = 80;
	public static final int H_SIZE = 30;
	public static final int MAX_LINE = 10;
	public static final int GAP = 2;
	
	/** anchura de línea */
	private int linea = 1;
	
	/** escuchadores de los eventos que genera este boton*/
	private Vector escucha;


	public LineButton() {
		setSize(W_SIZE, H_SIZE);
		addMouseListener(this);
		escucha = new Vector(5, 5);
	}
	
	public void addActionListener(ActionListener al) {
		escucha.add(al);
	}
	
	
	public void setLinea(int s) {
		linea = s;
		for(int i=0; i<escucha.size(); i++) {
			ActionListener a = (ActionListener) escucha.get(i);
			Integer entero = new Integer(linea);
			a.actionPerformed(new ActionEvent(this, 0, entero.toString()));
		}
		repaint();
	}
	
	public int getLinea() {
		return linea;
	}

	public void paint(Graphics g) {
		g.setColor(Color.BLACK);		
		g.drawRect(0,0, W_SIZE-1, H_SIZE-1);
		
		for(int i=0; i<MAX_LINE; i++) {
			int x = i*W_SIZE/MAX_LINE+W_SIZE/(2*MAX_LINE);
			for(int j=0; j<i; j++) {
				g.drawLine(x+j, GAP, x+j, H_SIZE-GAP);
			}
		}
	}
	
	public Dimension getPreferredSize() {
		return getSize();
	}
	
	public void mouseClicked(MouseEvent e) {
		Point p = e.getPoint();
		int id = (int) (p.getX()*MAX_LINE/W_SIZE);
		setLinea(id);
	}
	
	public void	mouseEntered(MouseEvent e){;}
	public void	mouseExited(MouseEvent e){;}
	public void	mousePressed(MouseEvent e){;}
	public void	mouseReleased(MouseEvent e) 	{;}
}