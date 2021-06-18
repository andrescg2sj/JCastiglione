package org.sj.tools.jcastiglione.animation;

//import math.geom.Vector2D;
import java.util.NoSuchElementException;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;


import java.util.Vector;

public class AnimableRectangle2D extends AnimableImpl<Rectangle2D>
{

	public AnimableRectangle2D()
	{
		super(new Rectangle2D.Float());
	}


	public AnimableRectangle2D(Rectangle2D def)
	{
		super(def);
	}
	
	public Rectangle2D interpolate(Rectangle2D a, Rectangle2D b, float factor)
	{
		if(factor == 1) {
			return b;
		} else if(factor == 0) {
			return a;
		} 
		return new Rectangle2D.Float((float) interDouble(a.getX(), b.getX(), factor), (float) interDouble(a.getY(), b.getY(), factor), 
							(float) interDouble(a.getWidth(), b.getWidth(), factor), (float) interDouble(a.getHeight(), b.getHeight(), factor));

	}

}