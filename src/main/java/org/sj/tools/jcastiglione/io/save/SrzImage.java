package org.sj.tools.jcastiglione.io.save;

import java.awt.geom.Rectangle2D;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.codec.binary.Base64;
import org.sj.tools.jcastiglione.figure.FigImage;
import org.sj.tools.jcastiglione.figure.FigRectangle;
import org.sj.tools.jcastiglione.io.Serializable;
import org.sj.tools.jcastiglione.io.Srz;
import org.sj.tools.jcastiglione.io.XMLGraf3Doc;

public class SrzImage extends FigImage implements Serializable {
	
	public SrzImage(FigImage fr) {
		super(fr);
		
	}
	
	
	//@Override
	public void serialize(XMLStreamWriter xsw) throws XMLStreamException {
		xsw.writeStartElement("img");
		Rectangle2D r = this.getBounds();
		// TODO: Export animated rectangle directly?
		xsw.writeAttribute("x", Srz.dtos(r.getX()));
		xsw.writeAttribute("y", Srz.dtos(r.getY()));
		xsw.writeAttribute("w", Srz.dtos(r.getWidth()));
		xsw.writeAttribute("h", Srz.dtos(r.getHeight()));
		

		/* Serialize bitmap */
		try {
			serializeBitmap(xsw);
			
		} catch(XMLStreamException xe) {
			throw xe;
		} catch(Exception e) {
			throw new XMLStreamException(e);
		}
		

		/* First, check AnimRectLoader and FigRectangleLoader */
		SrzAnimableRectangle2D ar2d = new SrzAnimableRectangle2D(this.rect);
		ar2d.serialize(xsw);
		
		//TODO: Color, 
		xsw.writeEndElement();
	}
	
	/**
	 * 
	 * Ref: https://stackoverflow.com/questions/1312832/java-image-encoding-in-xml
	 * 
	 * @param xsw
	 */
	public void serializeBitmap(XMLStreamWriter xsw) 
			throws UnsupportedOperationException, IOException, XMLStreamException 
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		if(!(this.imgData instanceof RenderedImage)) {
			throw new UnsupportedOperationException("Image is not rendered.");
		}
		RenderedImage rimg = (RenderedImage) imgData;
		 ImageIO.write(rimg, "png", baos);    
		 baos.flush();
		 Base64 b = new Base64();
		 String encodedImage = b.encodeToString(baos.toByteArray());
		 baos.close(); // should be inside a finally block
		 //node.setTextContent(encodedImage); // store it inside node
		 xsw.writeStartElement("bitmap");
		 xsw.writeCData(encodedImage);
		 xsw.writeEndElement();
	}

	

}
