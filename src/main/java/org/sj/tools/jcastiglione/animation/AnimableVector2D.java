package org.sj.tools.jcastiglione.animation;

import math.geom.Vector2D;
import java.util.NoSuchElementException;

import java.util.Vector;

public class AnimableVector2D extends AnimableImpl<Vector2D>
{


	public AnimableVector2D(Vector2D def)
	{
		super(def);
	}
	
	public Vector2D interpolate(Vector2D a, Vector2D b, float factor)
	{
		if(factor == 1) {
			return b;
		} else if(factor == 0) {
			return a;
		} 
		return new Vector2D(interFloat(a.getX(), b.getX(), factor), interFloat(a.getY(), b.getY(), factor));
	}

}