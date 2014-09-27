package org.xml.parsing.core.listeners;


import org.codehaus.stax2.XMLStreamReader2;
import org.xml.parsing.core.XmlNavigationPath;

import javax.xml.stream.XMLStreamException;
import java.util.List;


/**
 * Created by eros on 08/09/14.
 */
public interface ContentHandler {

    void startDocument(XMLStreamReader2 xmlStreamReader, XmlNavigationPath navigationStack);

    void startElement(XMLStreamReader2 xmlStreamReader, XmlNavigationPath navigationStack) throws XMLStreamException;

    void character(XMLStreamReader2 character, XmlNavigationPath navigationStack) throws XMLStreamException;

    void endElement(XMLStreamReader2 endElement, XmlNavigationPath navigationStack) throws XMLStreamException;

    void endDocument(XMLStreamReader2 endDocument, XmlNavigationPath navigationStack);

    void attribute(XMLStreamReader2 streamReader, XmlNavigationPath navigationStack) throws XMLStreamException;

    String getOut();
    
    
}
