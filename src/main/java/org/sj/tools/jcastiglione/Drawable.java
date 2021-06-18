package org.sj.tools.jcastiglione;

import java.awt.Graphics;

public interface Drawable {
	public void draw(Graphics g, ScrollConverter sr);
	public void draw(Graphics g, ScrollConverter sr, int time);
}