package org.sj.tools.jcastiglione.figure;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.io.DataOutputStream;
import java.io.IOException;

import org.sj.tools.jcastiglione.ScrollConverter;

import math.geom.Vector2D;

public class FigText extends FigRectBounded {
	
	
	String text;
	
	public FigText(Vector2D tl, Vector2D br, String text)
	{
		
		super(middle(tl,br),containerRect(tl,br));
		this.text = text;
	}
	
	
	public FigText(float x, float y, float width, float height) {
		super(x, y, width, height);
	}

	protected FigText(FigText ft) {
		// TODO Auto-generated constructor stub
		//throw new UnsupportedOperationException("Constructor from FigText");
		super(ft.getCentro(), ft.getBounds());
		setText(ft.getText());
		copyColors(this);

	}

	private static final String PARAGRAPH_SPLIT_REGEX = "(?m)(?=^\\s{4})";
	
	public void draw(Graphics g, ScrollConverter sc) {
		if(!esVisible()) return;
		Rectangle2D tr = rect.getDefaultValue();
		Point tl = sc.AbsToScreen(new Vector2D((float) tr.getX(), (float) tr.getY()));
		
		// TODO: Scroll & zoom
		FontMetrics fm = g.getFontMetrics();
		Vector2D lineHeight = new Vector2D(0, fm.getHeight());
		//
		
		//int w = sc.LogicToPixel(tr.getWidth());
		//int h = sc.LogicToPixel(tr.getHeight());
		
		// TODO: scale text
		// TODO: Fonts and alignment
		
		//FIXME: Not splitting 
		/* https://stackoverflow.com/questions/26367015/splitting-text-into-paragraphs-with-regex-java */
		String[] paragraphs = text.split(PARAGRAPH_SPLIT_REGEX);
		//g.drawString(text, (int) tl.getX(), (int) tl.getY());
		float y = (float) tl.getY();
		for(String para : paragraphs) {
			g.drawString(para, (int) tl.getX(), (int) y);
			//g.drawString(text, (int) tl.getX(), (int) tl.getY());
			//System.out.println(para);
			y +=  lineHeight.getY(); // TODO: Scroll & zoom
		}
	}

	//TODO refactorize dibujar
	public void draw(Graphics g, ScrollConverter sc, int time) {
		if(!esVisible()) return;
		Rectangle2D tr = rect.getValue(time);
		Point tl = sc.AbsToScreen(new Vector2D((float) tr.getX(), (float) tr.getY()));
		//int w = sc.LogicToPixel(tr.getWidth());
		//int h = sc.LogicToPixel(tr.getHeight());
		
		// TODO: scale text
		g.drawString(text, (int) tl.getX(), (int) tl.getY());

	}
	
	public void setText(String t) {
		text = t;
	}
	
	public String getText()
	{
		return text;
	}
	
	@Override
	public int tipoFigura() {
		// TODO Auto-generated method stub
		return FIGURA_TEXTO;
	}

	@Override
	public int getByteSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public byte[] getBytes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void guardar(DataOutputStream dos) throws IOException {
		// TODO Auto-generated method stub

	}

}
