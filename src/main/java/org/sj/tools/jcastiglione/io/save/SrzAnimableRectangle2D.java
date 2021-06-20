package org.sj.tools.jcastiglione.io.save;

import java.awt.geom.Rectangle2D;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.sj.tools.jcastiglione.animation.AnimableRectangle2D;
import org.sj.tools.jcastiglione.io.Serializable;
import org.sj.tools.jcastiglione.io.Srz;

public class SrzAnimableRectangle2D extends AnimableRectangle2D implements Serializable {

	public SrzAnimableRectangle2D(AnimableRectangle2D r) {
		this.setDefaultValue(r.getDefaultValue());
		this.copyAnim(r);
	}
	
	public void serialize(XMLStreamWriter xsw) throws XMLStreamException {
		xsw.writeStartElement("anim");
		//writeDefault(xsw);
		writeKeys(xsw);
		xsw.writeEndElement();
	}

	void writeRectangle2D(XMLStreamWriter xsw, Rectangle2D r) throws XMLStreamException {
		xsw.writeStartElement("rect2d");
		xsw.writeAttribute("x", Srz.dtos(r.getX()));
		xsw.writeAttribute("y", Srz.dtos(r.getY()));
		xsw.writeAttribute("w", Srz.dtos(r.getWidth()));
		xsw.writeAttribute("h", Srz.dtos(r.getHeight()));
		xsw.writeEndElement();
	}

	
	void writeDefault(XMLStreamWriter xsw) throws XMLStreamException {
		//xsw.writeStartElement("def");
		writeRectangle2D(xsw, this.getDefaultValue());
		//xsw.writeEndElement();
	}
	
	void writeKeys(XMLStreamWriter xsw) throws XMLStreamException {
		xsw.writeStartElement("keys");
		System.out.println("Writing: "+ numKeyFrames()+" keyframes");

		for(int i=0;i<numKeyFrames();i++){
			int time = this.getTimeForKey(i);
			Rectangle2D r = this.getValueForKey(i);
			writeKeyFrame(xsw, time, r);
		}
		xsw.writeEndElement();
	}
	
	void writeKeyFrame(XMLStreamWriter xsw, int time, Rectangle2D r) throws XMLStreamException {
		xsw.writeStartElement("kf");
		xsw.writeAttribute("time", Srz.itos(time));
		writeRectangle2D(xsw, r);
		xsw.writeEndElement();
	}
}
