package org.sj.tools.jcastiglione.io;

import static org.junit.Assert.assertEquals;

import java.awt.Dimension;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.junit.Assert;
import org.junit.Test;
import org.sj.tools.jcastiglione.JCastiglioneDoc;
import org.sj.tools.jcastiglione.Layer;
import org.sj.tools.jcastiglione.animation.AnimableRectangle2D;
import org.sj.tools.jcastiglione.figure.FigRectangle;
import org.sj.tools.jcastiglione.figure.FigText;
import org.sj.tools.jcastiglione.figure.Figura;
import org.sj.tools.jcastiglione.io.Graf3DocLoader;
import org.sj.tools.jcastiglione.io.XMLGraf3Doc;
import org.sj.tools.jcastiglione.io.load.AnimRectLoader;
import org.sj.tools.jcastiglione.io.save.SrzAnimableRectangle2D;

import math.geom.Vector2D;


public class TestGraf3Doc {
	
	
	Vector2D rectTL = new Vector2D(100,100);
	Vector2D rectBR = new Vector2D(350,250);

	Vector2D textbTL = new Vector2D(100,100);
	Vector2D textbBR = new Vector2D(350,250);
	
	FigRectangle rect2;
	Random rnd = new Random();
	
	public TestGraf3Doc() {
		rect2 = new FigRectangle(new Vector2D(100,100),
				new Vector2D(350,250)); 

	}
	
	public Vector2D rndVect() {
		
		return new Vector2D(rnd.nextInt(), rnd.nextInt());
	}
	
	@Test
	public void testingSave() {
		JCastiglioneDoc doc = new JCastiglioneDoc(new Dimension(640,480));
		
		doc.crearRectangulo(rectTL, rectBR);
		XMLGraf3Doc xdoc = new XMLGraf3Doc(doc);
		try {
			xdoc.saveXML("test/test.xml");
		} catch(XMLStreamException xse) {
			Assert.fail();
		}catch(IOException ioe) {
			
		}
	}

	@Test
	public void testingLoad() {
		Graf3DocLoader docl = new Graf3DocLoader();
		try {
			docl.loadXML("test/test.xml");
			Layer ly = docl.getCurrentLayer();
			assertEquals("num figuras", 1, ly.numFiguras());
			
			Figura f = docl.getFigura(0);
			Assert.assertNotNull(f);
			if(f instanceof FigRectangle) {
				
				FigRectangle r = (FigRectangle) f;
				checkRectangle(r);
				
			} else {
				Assert.fail("Wrong type of figure.");
			}
		} catch(IOException ioe) {
			Assert.fail(ioe.getMessage());
		}catch(XMLStreamException xe) {
			Assert.fail(xe.getMessage());
		}catch(Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
		
		
		
	}


	@Test
	public void testingSaveText() {
		JCastiglioneDoc doc = new JCastiglioneDoc(new Dimension(640,480));
		doc.createText(textbTL,textbBR, "Prueba texto");
		XMLGraf3Doc xdoc = new XMLGraf3Doc(doc);
		try {
			xdoc.saveXML("test/testtext.xml");
		} catch(XMLStreamException xse) {
			Assert.fail();
		}catch(IOException ioe) {
			
		}
	}

	@Deprecated
	private void checkRectangle(FigRectangle rect) {
		Rectangle2D b = rect.getBounds();
		assertEquals("different X",(int) rectTL.getX(), (int) b.getX());
		assertEquals("different Y",(int) rectTL.getY(),(int) b.getY());
		assertEquals("different W",(int) (rectBR.getX()-rectTL.getX()), (int) b.getWidth());
		assertEquals("different H",(int) (rectBR.getY()-rectTL.getY()),(int) b.getHeight());
	}

	private void checkRectangle(FigRectangle exp, FigRectangle actual) {
		Rectangle2D ab = actual.getBounds();
		Rectangle2D eb = exp.getBounds();
		assertEquals("different X",(int) eb.getX(), (int) ab.getX());
		assertEquals("different Y",(int) eb.getY(),(int) ab.getY());
		assertEquals("different W",(int) eb.getWidth(), (int) ab.getWidth());
		assertEquals("different H",(int) eb.getHeight(),(int) ab.getHeight());
	}

	@Deprecated
	private void checkTextbox(FigText tb) {
		Rectangle2D b = tb.getBounds();
		assertEquals("different X",(int) textbTL.getX(), (int) b.getX());
		assertEquals("different Y",(int) textbTL.getY(),(int) b.getY());
		assertEquals("different W",(int) (textbBR.getX()-textbTL.getX()), (int) b.getWidth());
		assertEquals("different H",(int) (textbBR.getY()-textbTL.getY()),(int) b.getHeight());
		assertEquals("text","Prueba texto",tb.getText());
	}

	private void checkTextbox(FigText exp, FigText actual) {
		Rectangle2D eb = exp.getBounds();
		Rectangle2D ab = actual.getBounds();
		assertEquals("different X",(int) eb.getX(), (int) ab.getX());
		assertEquals("different Y",(int) eb.getY(),(int) ab.getY());
		assertEquals("different W",(int) eb.getWidth(), (int) ab.getWidth());
		assertEquals("different H",(int) eb.getHeight(),(int) ab.getHeight());
		assertEquals("text",exp.getText(),actual.getText());
	}

	
	
	@Test
	public void testingLoadText() {
		Graf3DocLoader docl = new Graf3DocLoader();
		try {
			docl.loadXML("test/testtext.xml");
			Layer ly = docl.getCurrentLayer();
			assertEquals("num figuras", 1, ly.numFiguras());
			
			Figura f = docl.getFigura(0);
			Assert.assertNotNull(f);
			if(f instanceof FigText) {
				FigText t = (FigText) f;
				checkTextbox(t);
				
			} else {
				Assert.fail("Wrong type of figure.");
			}
		} catch(IOException ioe) {
			Assert.fail(ioe.getMessage());
		}catch(XMLStreamException xe) {
			Assert.fail(xe.getMessage());
		}catch(Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}	

	

	public void saveTwo() {
		JCastiglioneDoc doc = new JCastiglioneDoc(new Dimension(640,480));
		doc.crearRectangulo(rectTL, rectBR);
		doc.createText(rectTL,rectBR, "Prueba texto");
		XMLGraf3Doc xdoc = new XMLGraf3Doc(doc);
		try {
			xdoc.saveXML("test/testtwo.xml");
		} catch(Exception e) {
			Assert.fail(e.getMessage());
		}
	}
	
	
	
	public FigRectangle[] genManyRect()
	{
		FigRectangle rect[] = new FigRectangle[3];
		rect[0] = new FigRectangle(rectTL, rectBR);
		rect[1] = rect2;
		rect[2] = rect[0];
		return rect;
	}
	
	@Test
	public void testingLoadManyRect() {
		String filename = "test/testtworect.xml";
		FigRectangle r[] = genManyRect();
		saveMany(filename,r);
		Graf3DocLoader docl = new Graf3DocLoader();
		try {
			docl.loadXML(filename);
			Layer ly = docl.getCurrentLayer();
			assertEquals("num figuras", r.length, ly.numFiguras());
			
			for(int i=0;i<r.length;i++) {
				Figura f = docl.getFigura(i);
				Assert.assertNotNull(f);
				if(f instanceof FigRectangle) {
					checkRectangle(r[i], (FigRectangle) f);
				} else {
					Assert.fail("f1. Wrong type of figure:"+f.getClass().getName());
				}
			}

		} catch(Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}	
	
	public void saveMany(String filename, Figura f[])
	{
		JCastiglioneDoc doc = new JCastiglioneDoc(new Dimension(640,480));
		
		for(int i=0;i<f.length;i++) 
			doc.addFigure(f[i]);
		
		//doc.addFigure(textb[2]);
		
		XMLGraf3Doc xdoc = new XMLGraf3Doc(doc);
		try {
			xdoc.saveXML(filename);
		} catch(Exception e) {
			Assert.fail(e.getMessage());
		}
		
	}


	
	public FigText[] genManyText()
	{
		FigText textb[] = new FigText[2];
		textb[0] = new FigText(textbTL, textbBR, "Texto0");
		textb[1] = new FigText(textbTL, textbBR, "Texto1");
		JCastiglioneDoc doc = new JCastiglioneDoc(new Dimension(640,480));
		

		return textb;
	}



	@Test
	public void testingLoadManyText() {
		String filename = "test/testmanytext.xml";
		FigText r[] = genManyText();
		saveMany(filename,r);
		Graf3DocLoader docl = new Graf3DocLoader();
		try {
			docl.loadXML(filename);
			Layer ly = docl.getCurrentLayer();
			assertEquals("num figuras", r.length, ly.numFiguras());
			
			for(int i=0;i<r.length;i++) {
				Figura f = docl.getFigura(i);
				Assert.assertNotNull(f);
				if(f instanceof FigText) {
					checkTextbox(r[i], (FigText) f);
				} else {
					Assert.fail("f1. Wrong type of figure:"+f.getClass().getName());
				}
			}

		} catch(Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}	

	
	@Test
	public void testingLoadTwo() throws Exception {
		saveTwo();
		Graf3DocLoader docl = new Graf3DocLoader();
		docl.loadXML("test/testtwo.xml");
		Layer ly = docl.getCurrentLayer();
		assertEquals("num figuras", 2, ly.numFiguras());
		
		Figura f1 = docl.getFigura(0);
		Figura f2 = docl.getFigura(1);
		Assert.assertNotNull(f1);
		Assert.assertNotNull(f2);
		if(f1 instanceof FigRectangle) {
			checkRectangle((FigRectangle) f1);
		} else {
			Assert.fail("f1. Wrong type of figure.");
		}
			if(f2 instanceof FigText) {
			checkTextbox((FigText) f2);
		} else {
			Assert.fail("f2. Wrong type of figure.");
		}
	}	
	


	@Test
	public void testLoadAnimRect() throws IOException, XMLStreamException {
		AnimableRectangle2D ar = testSaveAnimRect();
		   File archivo = new File("test/animrect2d.xml");
		   FileInputStream fis = new FileInputStream(archivo);

		   XMLInputFactory input = XMLInputFactory.newInstance();
		   XMLStreamReader reader = input.createXMLStreamReader(fis);

		   AnimRectLoader al = new AnimRectLoader();
	       while (reader.hasNext()) {
	    	   int eventType = reader.next();
	           switch (eventType) {
	           case XMLStreamReader.START_ELEMENT:
	        	   if(reader.getLocalName().equals("anim")) {
	        		   al.load(reader);
	        	   } else {
	        		   Assert.fail("Unexpected state. Corrupted file?");
	        	   }
		   
	           }
	       }
		   fis.close();
		   
		   Assert.assertEquals("num keyframes", ar.numKeyFrames(), al.numKeyFrames());
		   Assert.assertTrue("AnimRectangles equal", ar.equals(al));

		
	}
	
	public AnimableRectangle2D testSaveAnimRect() throws IOException, XMLStreamException {
		   File archivo = new File("test/animrect2d.xml");
		   FileOutputStream fos = new FileOutputStream(archivo);

		   XMLOutputFactory output = XMLOutputFactory.newInstance();
		   XMLStreamWriter writer = output.createXMLStreamWriter(fos);

		   writer.writeStartDocument();

		   AnimableRectangle2D testRect =
				   org.sj.tools.jcastiglione.animation.TestAnimableRectangle2D.generateAnimRectangle();
 		   SrzAnimableRectangle2D sr = 
 				   new SrzAnimableRectangle2D(testRect);
		   sr.serialize(writer);
		   //this.serialize(writer);
		   writer.writeEndDocument();
		   writer.flush();
		   fos.close();
		   
		   return sr;
	}
	
	@Test
	public void testingLoadSeveral() {
		
		Graf3DocLoader docl = new Graf3DocLoader();
		try {
			docl.loadXML("test/pruebatexto2.xml");
			Layer ly = docl.getCurrentLayer();
			assertEquals("num layers", 1, docl.getLayerManager().numLayers());
			assertEquals("num figuras", 4, ly.numFiguras());
			
			for(int i=0;i<4;i++){
				Figura f = docl.getFigura(i);
				Assert.assertNotNull(f);
			}
		} catch(IOException ioe) {
			Assert.fail(ioe.getMessage());
		}catch(XMLStreamException xe) {
			Assert.fail(xe.getMessage());
		}catch(Exception e) {
			e.printStackTrace();
			Assert.fail();
		}
	}	

}
