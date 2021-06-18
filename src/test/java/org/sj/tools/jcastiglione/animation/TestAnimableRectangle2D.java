package org.sj.tools.jcastiglione.animation;


import static org.junit.Assert.assertEquals;

import java.awt.geom.Rectangle2D;

import org.junit.Assert;
import org.junit.Test;
import org.sj.tools.jcastiglione.animation.AnimableRectangle2D;

public class TestAnimableRectangle2D {

	public static AnimableRectangle2D generateAnimRectangle() {
		AnimableRectangle2D r = new AnimableRectangle2D();   
		r.setDefaultValue(new Rectangle2D.Float(10,20,30,40));
		r.setKeyFrame(10, new Rectangle2D.Float(30,40,50,60));
		r.setKeyFrame(20, new Rectangle2D.Float(50,60,70,80));
		return r;
	}

	
	@Test
	public void testingCopyAnim() {
		AnimableRectangle2D testRect = generateAnimRectangle();
		AnimableRectangle2D s = new AnimableRectangle2D();
		s.copyAnim(testRect);
		
		Assert.assertEquals("num keyframes", testRect.numKeyFrames(), 
				s.numKeyFrames());

	}
	
	@Test
	public void testingEquals() {
		AnimableRectangle2D testRect = generateAnimRectangle();
		AnimableRectangle2D s = new AnimableRectangle2D();
		/*Assert.assertNotEquals("num keyframes", testRect.numKeyFrames(), 
				s.numKeyFrames());*/
		
		Assert.assertFalse(testRect.equals(s));
		Assert.assertFalse(s.equals(testRect));
		s.copyAnim(testRect);
		Assert.assertTrue(testRect.equals(s));
		Assert.assertTrue(s.equals(testRect));

	}
}
