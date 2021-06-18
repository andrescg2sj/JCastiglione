package org.sj.tools.jcastiglione.io;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public interface Serializable {

	
	public void serialize(XMLStreamWriter xsw) throws XMLStreamException;
}
