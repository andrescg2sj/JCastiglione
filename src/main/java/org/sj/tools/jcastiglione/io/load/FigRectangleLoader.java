package org.sj.tools.jcastiglione.io.load;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.sj.tools.jcastiglione.figure.FigRectangle;
import org.sj.tools.jcastiglione.io.Graf3DocLoader;
import org.sj.tools.jcastiglione.io.XMLGraf3Doc;

import math.geom.Vector2D;

public class FigRectangleLoader extends FigRectangle {
	
	public FigRectangleLoader() {
		super(new Vector2D(0,0),new Vector2D(1,1));
	}
	
	public FigRectangle load(XMLStreamReader reader)throws XMLStreamException {
		return readRect(reader);
	}
	
	public static Rectangle2D readRectangle2D(XMLStreamReader reader) throws XMLStreamException{
		Graf3DocLoader.assertState("rect2d", reader);
        float x = Graf3DocLoader.stof(reader.getAttributeValue(null, "x"));
        float y = Graf3DocLoader.stof(reader.getAttributeValue(null, "y"));
        float width = Graf3DocLoader.stof(reader.getAttributeValue(null, "w"));
        float height = Graf3DocLoader.stof(reader.getAttributeValue(null, "h"));
        Rectangle2D rect = new Rectangle2D.Float(x,y,width,height);
         
        while (reader.hasNext()) {
            int eventType = reader.next();
            switch (eventType) {
                case XMLStreamReader.START_ELEMENT:
                	throw new XMLStreamException("no inner elements expecetd");

                case XMLStreamReader.END_ELEMENT:
                	Graf3DocLoader.assertState("rect2d", reader);
                    return rect;
            }
        }
        throw new XMLStreamException("Premature end of file");

	}
	
	private FigRectangle readRect(XMLStreamReader reader) throws XMLStreamException{
		//TODO: store and load center ...
		Graf3DocLoader.assertState("rect", reader);
		//Rectangle2D r = readRectangle2D(reader);
		
        float x = Graf3DocLoader.stof(reader.getAttributeValue(null, "x"));
        float y = Graf3DocLoader.stof(reader.getAttributeValue(null, "y"));
        float width = Graf3DocLoader.stof(reader.getAttributeValue(null, "w"));
        float height = Graf3DocLoader.stof(reader.getAttributeValue(null, "h"));
        Rectangle2D r = new Rectangle2D.Float(x,y,width,height);
		FigRectangle fig = new FigRectangle(r);
         
        while (reader.hasNext()) {
            int eventType = reader.next();
            switch (eventType) {
                case XMLStreamReader.START_ELEMENT:
                	
                	if("anim".equals(reader.getLocalName())) {
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
        throw new XMLStreamException("Premature end of file");
	}
}

	
