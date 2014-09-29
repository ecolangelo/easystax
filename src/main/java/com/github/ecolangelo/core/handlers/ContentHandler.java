package com.github.ecolangelo.core.handlers;


import org.codehaus.stax2.XMLStreamReader2;
import com.github.ecolangelo.core.XmlNavigationPath;

import javax.xml.stream.XMLStreamException;


/**
 * Created by eros on 08/09/14.
 */
public interface ContentHandler {

    void startElement(XMLStreamReader2 xmlStreamReader, XmlNavigationPath navigationStack) throws XMLStreamException;

    void character(XMLStreamReader2 character, XmlNavigationPath navigationStack) throws XMLStreamException;

    void endElement(XMLStreamReader2 endElement, XmlNavigationPath navigationStack) throws XMLStreamException;

    void attribute(XMLStreamReader2 streamReader, XmlNavigationPath navigationStack) throws XMLStreamException;

    String getOut();

    String getId();
    
    
}
