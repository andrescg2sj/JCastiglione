package org.sj.tools.jcastiglione.animation;

import math.geom.Vector2D;

public class InterVector2D extends Vector2D implements Interpolable<Vector2D>
{
	public InterVector2D()
	{
		super();
	}

	public InterVector2D(float x, float y)
	{
		super(x, y);
	}
	public InterVector2D(Vector2D v)
	{
		super(v);
	}

	public Vector2D interpolate(Vector2D a, Vector2D b, float factor)
	{
		if(factor == 1) {
			return b;
		} else if(factor == 0) {
			return a;
		} 
		return new Vector2D(b.getX()*factor + (1-factor)*a.getX(), b.getY()*factor+(1-factor)*a.getY());
	}
	

}