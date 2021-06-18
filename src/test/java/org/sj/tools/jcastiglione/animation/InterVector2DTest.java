package org.sj.tools.jcastiglione.animation;


//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertTrue;
import org.junit.Assert;
import org.junit.Test;
import math.geom.Vector2D;

public class InterVector2DTest {
  @Test
  public void ConstTest() {
	InterVector2D iv = new InterVector2D();
	InterVector2D ivv = new InterVector2D(new Vector2D(1,2));
	InterVector2D ivn = new InterVector2D(4,5);
	
	Assert.assertNotNull(iv);	
	Assert.assertNotNull(ivv);	
	Assert.assertNotNull(ivn);	
	
  }
  
  @Test
  public void equalityTest() {
	Vector2D v = new Vector2D(3,4);
	InterVector2D iv = new InterVector2D(3, 4);
	
	Assert.assertTrue("Vector2D doesn't recoginize IV", v.equals(iv));
	Assert.assertTrue("IV2D doesn't recoginize Vector2D", iv.equals(v));
	//assertNotNull(ivv);	
	//assertNotNull(ivn);	
	
  }
 
  
 
}
