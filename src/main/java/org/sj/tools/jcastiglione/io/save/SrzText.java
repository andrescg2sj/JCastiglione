package org.sj.tools.jcastiglione.io.save;

import java.awt.geom.Rectangle2D;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.sj.tools.jcastiglione.figure.FigText;
import org.sj.tools.jcastiglione.io.Serializable;
import org.sj.tools.jcastiglione.io.Srz;

public class SrzText extends FigText implements Serializable {

	public SrzText(FigText ft) {
		super(ft);
	}
	
	@Override
	public void serialize(XMLStreamWriter xsw) throws XMLStreamException {
		xsw.writeStartElement("textbox");
		Rectangle2D r = this.getRect();
		// TODO: Export animated rectangle directly?
		xsw.writeAttribute("x", Srz.dtos(r.getX()));
		xsw.writeAttribute("y", Srz.dtos(r.getY()));
		xsw.writeAttribute("w", Srz.dtos(r.getWidth()));
		xsw.writeAttribute("h", Srz.dtos(r.getHeight()));
		
		xsw.writeStartElement("text");
		xsw.writeCharacters(getText());
		xsw.writeEndElement();
		
		//TODO: Color, animation
		xsw.writeEndElement();
	}

}
