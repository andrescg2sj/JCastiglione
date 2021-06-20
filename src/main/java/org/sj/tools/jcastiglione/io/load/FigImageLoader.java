package org.sj.tools.jcastiglione.io.load;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;

import javax.imageio.ImageIO;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.sj.tools.jcastiglione.figure.FigImage;
import org.sj.tools.jcastiglione.figure.FigRectangle;
import org.sj.tools.jcastiglione.io.JCgDocLoader;
import org.sj.tools.jcastiglione.io.XMLGraf3Doc;

import math.geom.Vector2D;

public class FigImageLoader extends FigImage {

	public FigImageLoader() {
		super(null, new Rectangle2D.Float());
	}
	
	public static FigImage load(XMLStreamReader reader)throws XMLStreamException {
		return readImage(reader);
	}
	
	
	private static FigImage readImage(XMLStreamReader reader) throws XMLStreamException {
		//TODO: store and load center ...
		JCgDocLoader.assertState("img", reader);
		//Rectangle2D r = readRectangle2D(reader);
		
		try {
	        float x = JCgDocLoader.stof(reader.getAttributeValue(null, "x"));
	        float y = JCgDocLoader.stof(reader.getAttributeValue(null, "y"));
	        float width = JCgDocLoader.stof(reader.getAttributeValue(null, "w"));
	        float height = JCgDocLoader.stof(reader.getAttributeValue(null, "h"));
	        Rectangle2D r = new Rectangle2D.Float(x,y,width,height);
	        FigImage fig = new FigImage(null, r);
			//FigRectangle fig = new FigRectangle(r);
	         
	        while (reader.hasNext()) {
	            int eventType = reader.next();
	            switch (eventType) {
	                case XMLStreamReader.START_ELEMENT:
	                	if("bitmap".equals(reader.getLocalName())) {
	                		BufferedImage img = readBitmap(reader);
	                		fig.setImage(img);
	                	} else if("anim".equals(reader.getLocalName())) {
	                		AnimRectLoader arload = new AnimRectLoader();
	                		arload.load(reader);
	                		fig.setRectAnimation(arload);
	                	} else if("color".equals(reader.getLocalName())) {
	                		Color c = XMLGraf3Doc.readColor(reader);
	                		fig.setColorFondo(c);
	                	} else {
	                		throw new XMLStreamException("Unexpected element inside rect: "+reader.getLocalName());
	                	}
	
	                case XMLStreamReader.END_ELEMENT:
	                	if("img".equals(reader.getLocalName())) {
	                		return fig;
	                	}
	            }
	        }
		} catch(ParseException pe) {
			throw new XMLStreamException(pe);
		}
        throw new XMLStreamException("Premature end of file");
	}
	
	public static BufferedImage readBitmap(XMLStreamReader reader) throws XMLStreamException
	{
		JCgDocLoader.assertState("bitmap", reader);
		//String encodedImage = "";
		StringBuilder result = new StringBuilder();
		
        while (reader.hasNext()) {
            int eventType = reader.next();
            switch (eventType) {
                case XMLStreamReader.START_ELEMENT:
                	
               		throw new XMLStreamException("Unexpected element inside bitmap: "+reader.getLocalName());

                case XMLStreamReader.END_ELEMENT:
                	if("bitmap".equals(reader.getLocalName())) {
                		Base64 b = new Base64();
						Object obj = b.decode(result.toString());
						if(!(obj instanceof byte[])) {
							throw new XMLStreamException("Problem decoding image");
						}
						byte[] bytes = (byte[]) obj; 
						try{
							BufferedImage image = ImageIO.read(new ByteArrayInputStream(bytes));
							return image;
						} catch(IOException xe) {
							throw new XMLStreamException("Image data corrupted.", xe);
						}
                	} else {
                		throw new XMLStreamException("Unexpected closing element: "+reader.getLocalName());
                	}
                	
                	
                	
                case XMLStreamReader.CHARACTERS:
                case XMLStreamReader.CDATA:
                	result.append(reader.getText());
                	break;
            }
        }
        throw new XMLStreamException("Premature end of file");

	}

}
