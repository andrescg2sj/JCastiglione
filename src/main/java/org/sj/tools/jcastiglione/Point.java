package org.sj.tools.jcastiglione;

import java.awt.Color;
import math.geom.Vector2D;

public interface Point {
	public float getX();
	public float getY();
	public Vector2D getAbsPos(Vector2D o, Vector2D u);
	public Color getColor();
	public boolean esTransp();
	
}