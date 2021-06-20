package org.sj.tools.jcastiglione.io.save;

import java.awt.geom.Rectangle2D;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.sj.tools.jcastiglione.figure.FigRectangle;
import org.sj.tools.jcastiglione.io.Serializable;
import org.sj.tools.jcastiglione.io.Srz;
import org.sj.tools.jcastiglione.io.XMLGraf3Doc;

public class SrzRectangle extends FigRectangle implements Serializable {
	
	public SrzRectangle(FigRectangle fr) {
		super(fr);
	}
	
	
	//@Override
	public void serialize(XMLStreamWriter xsw) throws XMLStreamException {
		xsw.writeStartElement("rect");
		Rectangle2D r = this.getRect();
		// TODO: Export animated rectangle directly?
		xsw.writeAttribute("x", Srz.dtos(r.getX()));
		xsw.writeAttribute("y", Srz.dtos(r.getY()));
		xsw.writeAttribute("w", Srz.dtos(r.getWidth()));
		xsw.writeAttribute("h", Srz.dtos(r.getHeight()));
		

		/* Background color */
		XMLGraf3Doc.serializeColor(getColorFondo(), xsw);
		//TODO: line color

		/* First, check AnimRectLoader and FigRectangleLoader */
		SrzAnimableRectangle2D ar2d = new SrzAnimableRectangle2D(this.rect);
		ar2d.serialize(xsw);
		
		//TODO: Color, 
		xsw.writeEndElement();
	}

	

}
