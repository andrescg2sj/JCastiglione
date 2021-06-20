package org.sj.tools.jcastiglione.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.junit.Assert;
import org.junit.Test;
import org.sj.tools.jcastiglione.figure.FigImage;
import org.sj.tools.jcastiglione.io.load.AnimRectLoader;
import org.sj.tools.jcastiglione.io.load.FigImageLoader;
import org.sj.tools.jcastiglione.io.save.SrzImage;

import math.geom.Vector2D;

public class TestFigImage {
	
	FigImage testImage;
	String testXMLFile = "test/image.xml";
	
	public TestFigImage() throws IOException {
		testImage = new FigImage(new Vector2D(20,10),"test/image.png");
	}

	@Test
	public void testingSave() {
		SrzImage si = new SrzImage(testImage);
		File archivo = new File(testXMLFile);
		try{ 

			FileOutputStream fos = new FileOutputStream(archivo);

			XMLOutputFactory output = XMLOutputFactory.newInstance();
			XMLStreamWriter writer = output.createXMLStreamWriter(fos);

			writer.writeStartDocument();
			si.serialize(writer);
			//this.serialize(writer);
			writer.writeEndDocument();
			writer.flush();
			fos.close();
		} catch(IOException ioe) {
			Assert.fail(ioe.getMessage());
		} catch (XMLStreamException xe) {
			xe.printStackTrace();
			Assert.fail(xe.getMessage());
		}
		Assert.assertTrue(archivo.exists());
	}
	
	@Test
	public void testingLoad() throws XMLStreamException, IOException {
		//FigImageLoader stub = new FigImageLoader();
		FigImage img;
		
		   File archivo = new File(testXMLFile);
		   FileInputStream fis = new FileInputStream(archivo);

		   XMLInputFactory input = XMLInputFactory.newInstance();
		   XMLStreamReader reader = input.createXMLStreamReader(fis);
		   
		   if(!reader.hasNext()) 
			   throw new XMLStreamException("Test file is empty.");
    	   if(reader.next() != XMLStreamReader.START_ELEMENT)
    		   throw new XMLStreamException("Test file is not correct XML.");
       	   if(!reader.getLocalName().equals("img"))
    		   throw new XMLStreamException("Unexpected test file structure:"+
    				   reader.getLocalName());
	       img = FigImageLoader.load(reader);
		   fis.close();
		   img.saveImage("test/myImage.png");
		   Assert.assertTrue("Bounds", testImage.getBounds().equals(img.getBounds()));
		   //TODO: 
		   //Assert.assertTrue("Images", testImage.equals(img));


	}
}
