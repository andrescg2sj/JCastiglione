package org.sj.tools.jcastiglione.io.load.svg;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.text.ParseException;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.sj.tools.jcastiglione.figure.FigRectangle;
import org.sj.tools.jcastiglione.io.JCgDocLoader;
import org.sj.tools.jcastiglione.io.XMLGraf3Doc;
import org.sj.tools.jcastiglione.io.load.AnimRectLoader;

import math.geom.Vector2D;

public class FigRectangleLoader extends FigRectangle {
	
	
	public FigRectangleLoader() {
		super(new Vector2D(0,0),new Vector2D(1,1));
	}
	
	public FigRectangle load(XMLStreamReader reader)throws XMLStreamException {
		return readRect(reader);
	}
	/*
	public static Rectangle2D readRectangle2D(XMLStreamReader reader) throws XMLStreamException{
		JCgDocLoader.assertState("rect", reader);
		try {
	        float x = JCgDocLoader.stof(reader.getAttributeValue(null, "x"));
	        float y = JCgDocLoader.stof(reader.getAttributeValue(null, "y"));
	        float width = JCgDocLoader.stof(reader.getAttributeValue(null, "width"));
	        float height = JCgDocLoader.stof(reader.getAttributeValue(null, "height"));
	        Rectangle2D rect = new Rectangle2D.Float(x,y,width,height);
	         
	        while (reader.hasNext()) {
	            int eventType = reader.next();
	            switch (eventType) {
	                case XMLStreamReader.START_ELEMENT:
	                	throw new XMLStreamException("no inner elements expecetd");
	
	                case XMLStreamReader.END_ELEMENT:
	                	JCgDocLoader.assertState("rect", reader);
	                    return rect;
	            }
	        }
        } catch(ParseException pe) {
            throw new XMLStreamException(pe);
        }
		
        throw new XMLStreamException("Premature end of file");

	}
	*/
	
	private FigRectangle readRect(XMLStreamReader reader) throws XMLStreamException{
		//TODO: store and load center ...
		JCgDocLoader.assertState("rect", reader);
		//Rectangle2D r = readRectangle2D(reader);
		try { 
	        float x = JCgDocLoader.stof(reader.getAttributeValue(null, "x"));
	        float y = JCgDocLoader.stof(reader.getAttributeValue(null, "y"));
	        float width = JCgDocLoader.stof(reader.getAttributeValue(null, "width"));
	        float height = JCgDocLoader.stof(reader.getAttributeValue(null, "heigth"));
	        Rectangle2D r = new Rectangle2D.Float(x,y,width,height);
			FigRectangle fig = new FigRectangle(r);
	         
	        while (reader.hasNext()) {
	            int eventType = reader.next();
	            switch (eventType) {
	                case XMLStreamReader.START_ELEMENT:
	                	
	                	if("animate".equals(reader.getLocalName())) {
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
	                	if("rect".equals(reader.getLocalName())) {
	                		return fig;
	                	}
	            }
	        }
		} catch(ParseException pe) {
			throw new XMLStreamException(pe);
		}
        throw new XMLStreamException("Premature end of file");
	}

}
