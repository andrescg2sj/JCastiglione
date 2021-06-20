package org.sj.tools.jcastiglione.io;

import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.sj.tools.jcastiglione.JCastiglioneDoc;
import org.sj.tools.jcastiglione.Layer;
import org.sj.tools.jcastiglione.LayerManager;
import org.sj.tools.jcastiglione.figure.FigRectangle;
import org.sj.tools.jcastiglione.figure.FigText;
import org.sj.tools.jcastiglione.io.load.FigImageLoader;
import org.sj.tools.jcastiglione.io.load.FigRectangleLoader;

import java.util.logging.Level;
import java.util.logging.Logger;
/*
 * See:https://softwarecave.org/2014/02/18/parse-xml-document-using-streaming-api-for-xml-stax/
 * 
 */

public class JCgDocLoader extends JCastiglioneDoc {
	
	//Logger log = Logger.getLogger(this.getClass().getName());
	Logger log = Logger.getLogger("Graf3DocLoader");
	
	FigRectangleLoader rectLoader = new FigRectangleLoader();
	
	public JCgDocLoader() {
		//TODO: private constructor for Graf3Doc without parameters
		super(new Dimension());
		log.setLevel(Level.FINE);
	}
	
	public JCgDocLoader(Dimension d) {
		super(d);
		log.setLevel(Level.FINE);
	}

	
	   public void loadXML(String filename) throws XMLStreamException, IOException {
		   
		   File archivo = new File(filename);
		   FileInputStream fis = new FileInputStream(archivo);

		   XMLInputFactory input = XMLInputFactory.newInstance();
		   XMLStreamReader reader = input.createXMLStreamReader(fis);
		   //Graf3DocLoader docl = new Graf3DocLoader();
		   
		   deserialize(reader);
		   
		   fis.close();
		   log.fine("Finish load");

	   }

	
	
	public void deserialize(XMLStreamReader reader) throws XMLStreamException {
		  while (reader.hasNext()) {
	            int eventType = reader.next();
	            switch (eventType) {
	                case XMLStreamReader.START_ELEMENT:
	                    String elementName = reader.getLocalName();
	                    if (elementName.equals("doc")) {
	                        readLayers(reader);
	                        LayerManager lm = this.getLayerManager();
	                        
	                        log.fine("Layers: "+lm.numLayers());
	                        log.fine("current - figs: "+lm.getCurrent().numFiguras());
	                    	return;
	                    }
	                    break;
	                case XMLStreamReader.END_ELEMENT:
	                	JCgDocLoader.assertState("text", reader);
	                    break;
	            }
	        }
	        throw new XMLStreamException("Premature end of file");
	    }
	

	private void readLayers(XMLStreamReader reader) throws XMLStreamException{
	        List<Layer> layers = new ArrayList<Layer>();
	         
	        while (reader.hasNext()) {
	            int eventType = reader.next();
	            switch (eventType) {
	                case XMLStreamReader.START_ELEMENT:
	                    String elementName = reader.getLocalName();
	                    if (elementName.equals("layer")) {
	                    	log.fine("Reading layer");
	                        layers.add(readLayer(reader));
	                        log.fine("read layers: "+layers.size());
	                    } else {
	                    	log.warning("Unexpected element:"+elementName);
	                    }
	                    
	                    break;
	                case XMLStreamReader.END_ELEMENT:
	                	LayerManager lm = this.getLayerManager();
	                	log.fine("Set layers");
	                	lm.clearLayers();
	                	lm.addLayers(layers);
	                    return;
	            }
	        }
	        throw new XMLStreamException("Premature end of file");
	    }

	private String readCharacters(XMLStreamReader reader) throws XMLStreamException {
        StringBuilder result = new StringBuilder();
        while (reader.hasNext()) {
            int eventType = reader.next();
            switch (eventType) {
                case XMLStreamReader.CHARACTERS:
                case XMLStreamReader.CDATA:
                    result.append(reader.getText());
                    break;
                case XMLStreamReader.END_ELEMENT:
                	assertState("text",reader);
                    return result.toString();
            }
        }
        throw new XMLStreamException("Premature end of file");
    }
	
	private boolean parseBool(String s) {
		if(s == null) throw new NullPointerException("Trying to parse boolean from null string");
		if("true".equals(s)) return true;
		if("false".equals(s)) return false;
		throw new IllegalArgumentException("Expected 'true' or 'false' but found: "+s);
	}
	
	public static void assertState(String element, XMLStreamReader reader) {
		if(!element.equals(reader.getLocalName())) {
			throw new IllegalStateException("Wrong reader state: "+reader.getLocalName());
		}
	}

	private Layer readLayer(XMLStreamReader reader) throws XMLStreamException
	{
		assertState("layer",reader);
		String name = reader.getAttributeValue(null, "name");
		boolean active = parseBool(reader.getAttributeValue(null, "active"));
		boolean visible = parseBool(reader.getAttributeValue(null, "visible"));
		Layer ly = new Layer(name, active, visible);

       //List<Figura> figuras= new ArrayList<Layer>();
		        
        while (reader.hasNext()) {
            int eventType = reader.next();
            switch (eventType) {
                case XMLStreamReader.START_ELEMENT:
                    String elementName = reader.getLocalName();
                    log.info("element: "+elementName);
                    if (elementName.equals("rect")) {//TODO: HashMap
                    	log.info("Reading rect");
                        ly.addFigura(rectLoader.load(reader));
                    } else if(elementName.equals("textbox")) {
                    	log.info("Reading textbox");
                        ly.addFigura(readText(reader));
                    } else if(elementName.equals("img")) {
                    	ly.addFigura(FigImageLoader.load(reader));
                    }
                    log.fine("read figures: "+ly.numFiguras());
                    break;
                case XMLStreamReader.END_ELEMENT:
                	//LayerManager lm = this.getLayerManager();
            		/*if(!"layer".equals(reader.getLocalName())) 
            			throw new IllegalStateException("Wrong reader state: "+
            			reader.getLocalName());*/
            		
                	log.fine("Returning layer");
                	//lm.addLayers(layers);
                	//assertState("layer",reader);
                    return ly;
            }
        }
        throw new XMLStreamException("Premature end of file");

	}
	
	public static float stof(String s) throws ParseException {
		//TODO: make localization (decimal dot or comma) configurable
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator(',');
		DecimalFormat format = new DecimalFormat("0.#");
		format.setDecimalFormatSymbols(symbols);
		return format.parse(s).floatValue();
		//return Float.parseFloat(s);
	}
	
	

	//TODO: refactor? Different classes (Figures) in different loaders?
	private FigText readText(XMLStreamReader reader) throws XMLStreamException{
		assertState("textbox",reader);
		//TODO: store and load center ...
		try {
	        float x = stof(reader.getAttributeValue(null, "x"));
	        float y = stof(reader.getAttributeValue(null, "y"));
	        float width = stof(reader.getAttributeValue(null, "w"));
	        float height = stof(reader.getAttributeValue(null, "h"));
	        
	        //TODO: constructor with 'center' paramenters.
	        FigText tbox = new FigText(x,y,width,height);
	         
	        String buffer = "";
	        while (reader.hasNext()) {
	            int eventType = reader.next();
	            String elementName = reader.getLocalName();
	            log.fine("tb /Status:"+elementName );
	            switch (eventType) {
	                case XMLStreamReader.START_ELEMENT:
	                	
	                    if (elementName.equals("text")) {
	                    	String text = readCharacters(reader);
	                    	tbox.setText(text);
	                    }else {
	                    	throw new XMLStreamException("Unexpected element inside textbox");
	                    }
	                    break;
	
	                case XMLStreamReader.END_ELEMENT:
	                	//assertState("textbox",reader);
	                    return tbox;
	            }
	        }
		} catch(ParseException pe) {
			throw new XMLStreamException(pe);
		}

        throw new XMLStreamException("Premature end of file");
	
}

	public static int stoi(String str) {
		
		return Integer.parseInt(str);
	}


	

}
