package com.github.ecolangelo.core.handlers;

import com.github.ecolangelo.core.XmlNavigationPath;
import org.codehaus.stax2.XMLStreamReader2;

import javax.xml.stream.XMLStreamException;

/**
 * Created by eros on 30/09/14.
 */
public interface IContentHandler {
    void startElement(XMLStreamReader2 xmlStreamReader, XmlNavigationPath navigationStack) throws XMLStreamException;

    void character(XMLStreamReader2 character, XmlNavigationPath navigationStack) throws XMLStreamException;

    void endElement(XMLStreamReader2 endElement, XmlNavigationPath navigationStack) throws XMLStreamException;

    void attribute(XMLStreamReader2 streamReader, XmlNavigationPath navigationStack) throws XMLStreamException;

    String getOut();

    String getId();
}
