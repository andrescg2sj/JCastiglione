package org.sj.tools.jcastiglione;

import java.awt.*;
import java.awt.event.*;


class ColorDlg extends Dialog implements ActionListener, TextListener {
	private Container m_cont;
	private ColorButton clrBtn;
	private Button m_bOk;
	private Label showColor;
	private ControlColor m_ctrl;
	private Color m_color;
	protected TextField red;
	protected TextField green;
	protected TextField blue;
	
	private class ColorSample extends Canvas {
		Color clr;
		
		public ColorSample(Color c) {
			clr = c;
		}
	}
	
	private static final int NUM_SAMPLES = 10;
	private static Color preset[] = new Color[NUM_SAMPLES];

	public ColorDlg (Frame f, ColorButton c, String t, boolean m) {
		super(f, t, m);
			clrBtn = c;
		m_color = new Color(0, 0, 0);
		m_cont = new Container();
		m_bOk = new Button("OK");
		red = new TextField("0");
		green = new TextField("0");
		blue = new TextField("0");
		showColor = new Label(textColor());
		m_ctrl = new ControlColor(this);
		m_bOk.addActionListener( this );
		red.addTextListener(this);
		green.addTextListener(this);
		blue.addTextListener(this);
		setSize(200,350);
		setResizable(false);
		m_cont.add(m_bOk);
		m_cont.add(m_ctrl);
		m_cont.add(showColor);
		m_cont.add(red);
		m_cont.add(green);
		m_cont.add(blue);
		m_bOk.setLocation(70, 10);
		m_bOk.setSize(70, 20);
		showColor.setLocation(50, 50);
		showColor.setSize(150, 20);
		red.setLocation(50, 80);
		red.setSize(70, 20);
		green.setLocation(50, 110);
		green.setSize(70, 20);
		blue.setLocation(50, 140);
		blue.setSize(70, 20);
		m_ctrl.setLocation(10, 10);
		add(m_cont);
		m_color = Color.BLACK;
	}
	
	public void setColor(Color c) {
		m_color = c;
		showColor.setText(textColor());
	}
	
	public Color getColor() {
		return m_color;
	}
	
	public String textColor() {
		return "R = " + m_color.getRed()+" G = "+m_color.getGreen()+" B = "+m_color.getBlue();
	}
	
	public void close() {
		int r, g, b;
		try {
			r = Integer.parseInt(red.getText());
			g = Integer.parseInt(green.getText());
			b = Integer.parseInt(blue.getText());
		} catch(Exception exc) {
			r = g = b = 0;
		}
		m_color = new Color(r, g, b);
		clrBtn.setColor(m_color);
		setVisible(false);
	}
	
	public void actionPerformed(ActionEvent ae) {
		close();
	}
	
	public void textValueChanged(TextEvent e) {
		int r, g, b;
		try {
			r = Integer.parseInt(red.getText());
			g = Integer.parseInt(green.getText());
			b = Integer.parseInt(blue.getText());
		} catch(Exception exc) {
			r = g = b = 0;
		}
		m_ctrl.setRed(r);
		m_ctrl.setGreen(g);
		m_ctrl.setBlue(b);
		
		m_ctrl.repaint();
	}
}

class ControlColor extends Canvas implements MouseListener {
	private int m_red, m_green, m_blue;
	private ColorDlg m_dlg;
	
	public ControlColor(ColorDlg d) {
		super();
		setSize(32, 278);
		addMouseListener(this);
		m_red = 0;
		m_green = 0;
		m_blue = 0;
		m_dlg = d;
	}
	public void setRed(int r) {
		m_red = r;
	} 
	
	public void setGreen(int r) {
		m_green = r;
	} 
	public void setBlue(int r) {
		m_blue = r;
	} 

	
	public void paint(Graphics g) {
		Color c;
		int i;
		/*
		for(i=1; i<=256; i++) {
			c = new Color(i-1,0,0);
			g.setColor(c);
			g.drawLine(1,i,10,i);
			c = new Color(0,i-1,0);
			g.setColor(c);
			g.drawLine(11,i,20,i);
			c = new Color(0,0,i-1);
			g.setColor(c);
			g.drawLine(21,i,30,i);
		}*/
		drawColor(g);
		g.setColor(Color.BLACK);
		Dimension d = getSize();
		g.drawRect(0,0, d.width-1, d.height-1);

	}

	public void drawColor(Graphics g) {
		Color c = new Color(m_red, m_green, m_blue);
		g.setColor(c);
		g.fillRect(1,257,30,276);
	}

	
	public void drawColor(Graphics g, Color c) {
		g.setColor(c);
		g.fillRect(1,257,30,276);
	}
	
	public void mouseClicked(MouseEvent e) {
		int x, y;
		x = e.getX(); y = e.getY();
		if (y > 0 && y < 257) {
			switch((x-1)/10) {
			case 0:
				m_red = y-1;
				m_dlg.red.setText(""+m_red);
				break;
			case 1:
				m_green = y-1;
				m_dlg.green.setText(""+m_green);
				break;
			case 2:
				m_blue = y-1;
				m_dlg.blue.setText(""+m_blue);
				break;
			}
			Color c = new Color(m_red, m_green, m_blue);
			m_dlg.setColor(c);
			Graphics g = getGraphics();
			if (g!=null) drawColor(g, c);
		}
	}
	
	public void mouseEntered(MouseEvent e) {;}
	public void mouseExited(MouseEvent e) {;}
	public void mousePressed(MouseEvent e) {;}
	public void mouseReleased(MouseEvent e) {;}
	

}
