package org.sj.tools.jcastiglione.animation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import math.geom.Vector2D;

public class AnimableVector2DTest {

  @Test
  public void ConstructorTest() {
	AnimableVector2D av = new AnimableVector2D(new Vector2D());
	assertNotNull("AnimableVector2D was not constructed.", (Object) av);
  }
  
  
    
    @Test
  public void defaultTest() {
	Vector2D v = new Vector2D(3,4);
	
	AnimableVector2D av = new AnimableVector2D(v);
	assertEquals("Different Values", v, av.getDefaultValue());
  }
  
  
  

}