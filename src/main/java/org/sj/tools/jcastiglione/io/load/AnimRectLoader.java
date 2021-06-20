package org.sj.tools.jcastiglione.io.load;

import java.awt.geom.Rectangle2D;


import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.sj.tools.jcastiglione.animation.AnimableRectangle2D;
import org.sj.tools.jcastiglione.animation.ValKeyFrame;
import org.sj.tools.jcastiglione.io.JCgDocLoader;

public class AnimRectLoader extends AnimableRectangle2D {
	

	public AnimableRectangle2D load(XMLStreamReader reader) throws XMLStreamException {
		JCgDocLoader.assertState("anim", reader);
		
		AnimableRectangle2D ar = new AnimableRectangle2D();
		String status = null;
		
        while (reader.hasNext()) {
            int eventType = reader.next();
            switch (eventType) {
                case XMLStreamReader.START_ELEMENT:
                	//throw new XMLStreamException("rect: no inner elements expecetd");
                	String elementName = reader.getLocalName();
                	if(elementName.equals("rect2d")) {
                		Rectangle2D r = readDef(reader);
                	} else if(elementName.equals("keys")) {
                		status = "keys";
                	}  else if(elementName.equals("kf")) {
                		if("keys".equals(status))  {
                			ValKeyFrame<Rectangle2D> kf = readKF(reader);
                			ar.setKeyFrame(kf.time, kf.value);
                			System.out.println("loader "+ar.numKeyFrames());
                		} else {
                			throw new XMLStreamException("Incorrect structure.");
                		}
                	}
                	break;
                	
                case XMLStreamReader.END_ELEMENT:
                	//Graf3DocLoader.assertState("anim", reader);
                	if("keys".equals(status)) status = null;
                	if("anim".equals(reader.getLocalName())) {
                		this.copyAnim(ar);
                		return ar;
                	}
                	break;
            }
        }
        throw new XMLStreamException("Premature end of file");

	}

	private ValKeyFrame<Rectangle2D> readKF(XMLStreamReader reader) throws XMLStreamException {
		
		int time = Integer.parseInt(reader.getAttributeValue(null, "time"));
		  
		Rectangle2D r = null;
        while (reader.hasNext()) {
            int eventType = reader.next();
            switch (eventType) {
                case XMLStreamReader.START_ELEMENT:
                	//throw new XMLStreamException("rect: no inner elements expecetd");
                	String elementName = reader.getLocalName();
                	if(elementName.equals("rect2d")) {
                		r = FigRectangleLoader.readRectangle2D(reader);
                		System.out.println(r.toString());
                	} else {
                		//TODO: loggers
                		System.out.println("Unexpected state: "+elementName);
                	}
                	break;
                	
                case XMLStreamReader.END_ELEMENT:
                	if(r == null) throw new NullPointerException("Rectangle2D was not read");
                    return new ValKeyFrame<Rectangle2D>(time,r);
            }
        }
        throw new XMLStreamException("Premature end of file");
		
	}

	
	private Rectangle2D readDef(XMLStreamReader reader) throws XMLStreamException {
		return FigRectangleLoader.readRectangle2D(reader);
		
	}
}
