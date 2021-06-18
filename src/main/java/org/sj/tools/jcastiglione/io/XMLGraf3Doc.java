package org.sj.tools.jcastiglione.io;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

import org.sj.tools.jcastiglione.JCastiglioneDoc;
import org.sj.tools.jcastiglione.Layer;
import org.sj.tools.jcastiglione.LayerManager;
import org.sj.tools.jcastiglione.figure.FigImage;
import org.sj.tools.jcastiglione.figure.FigRectangle;
import org.sj.tools.jcastiglione.figure.FigText;
import org.sj.tools.jcastiglione.figure.Figura;
import org.sj.tools.jcastiglione.io.save.SrzImage;
import org.sj.tools.jcastiglione.io.save.SrzRectangle;
import org.sj.tools.jcastiglione.io.save.SrzText;


public class XMLGraf3Doc implements Serializable {
	
	
	JCastiglioneDoc doc;
	
	public XMLGraf3Doc(JCastiglioneDoc doc)
	{
		this.doc = doc;
	}

	  public void saveXML(String filename) throws XMLStreamException, IOException
	   {
		   File archivo = new File(filename);
		   FileOutputStream fos = new FileOutputStream(archivo);

		   XMLOutputFactory output = XMLOutputFactory.newInstance();
		   XMLStreamWriter writer = output.createXMLStreamWriter(fos);

		   writer.writeStartDocument();
		   /*writer.setPrefix("c","http://c");
		   writer.setDefaultNamespace("http://c");
		   writer.writeStartElement("http://c","a");
		   writer.writeAttribute("b","blah");
		   writer.writeNamespace("c","http://c");
		   writer.writeDefaultNamespace("http://c");
		   writer.setPrefix("d","http://c");
		   writer.writeEmptyElement("http://c","d");*/
		   //XMLGraf3Doc xd = new XMLGraf3Doc(this);
		   this.serialize(writer);
		   writer.writeEndDocument();
		   writer.flush();
		   fos.close();
	   }

	
	@Override
	public void serialize(XMLStreamWriter xsw) throws XMLStreamException {
		//xsw.writeStartElement("http://test", doc);
		xsw.writeStartElement("doc");
		//TODO: animation
		
		/* Figures */
		serializeLayerManager(xsw);
		xsw.writeEndElement();

	}
	
	void serializeLayerManager(XMLStreamWriter xsw) throws XMLStreamException
	{
		LayerManager lyman = doc.getLayerManager();
		Iterator itr = lyman.getLayerIterator();
		while(itr.hasNext()) {
			Layer ly = (Layer) itr.next();
			serializeLayer(ly, xsw);
		}
		
	}
	

	void serializeLayer(Layer ly, XMLStreamWriter xsw) throws XMLStreamException
	{
		xsw.writeStartElement("layer");
		xsw.writeAttribute("name", ly.getName());
		xsw.writeAttribute("active", Srz.btos(ly.esActiva()));
		xsw.writeAttribute("visible", Srz.btos(ly.esVisible()));
		for(Figura f: ly.figures()) {
			serializeFigure(f, xsw);
		}
	 //TODO	
		xsw.writeEndElement();
	}
	
	void serializeFigure(Figura f, XMLStreamWriter xsw) throws XMLStreamException
	{
		//TODO:exemplars Pattern
		if(f instanceof FigRectangle) {
			FigRectangle r = (FigRectangle) f;
			SrzRectangle sr = new SrzRectangle(r);
			sr.serialize(xsw);
		} if(f instanceof FigText) {
			FigText t = (FigText) f;
			SrzText st = new SrzText(t);
			st.serialize(xsw);
		} if(f instanceof FigImage) {
			FigImage t = (FigImage) f;
			SrzImage st = new SrzImage(t);
			st.serialize(xsw);
			
		}
	}
	
	
	public static void serializeColor(Color c, XMLStreamWriter xsw) throws XMLStreamException 
	{
		xsw.writeStartElement("color");
		xsw.writeAttribute("R", Srz.itos(c.getRed()));
		xsw.writeAttribute("G", Srz.itos(c.getGreen()));
		xsw.writeAttribute("B", Srz.itos(c.getBlue()));
		xsw.writeEndElement();
	}

	public static Color readColor(XMLStreamReader reader) throws XMLStreamException 
	{
		Graf3DocLoader.assertState("color",reader);
        int R = Graf3DocLoader.stoi(reader.getAttributeValue(null, "R"));
        int G = Graf3DocLoader.stoi(reader.getAttributeValue(null, "G"));
        int B = Graf3DocLoader.stoi(reader.getAttributeValue(null, "B"));
         
        while (reader.hasNext()) {
            int eventType = reader.next();
            switch (eventType) {
                case XMLStreamReader.START_ELEMENT:
                	throw new XMLStreamException("Color: no inner elements expeceted.");

                case XMLStreamReader.END_ELEMENT:
                	Graf3DocLoader.assertState("color", reader);
                    return new Color(R,G,B);
            }
        }
        throw new XMLStreamException("Premature end of file");

	}

	
}
