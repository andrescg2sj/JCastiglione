package org.sj.tools.jcastiglione.gui;

import java.awt.Point;

/** 
 * This class combines together a point in the screen and the corresponding position 
 * on the drawing plane.
 * 
 */
import math.geom.Vector2D;

public class LogicPoint {
	Point screenPoint;
	Vector2D absPosition;
	
	LogicPoint(Point p, Vector2D v) {
		this.screenPoint = p;
		this.absPosition = v;
	}
}
