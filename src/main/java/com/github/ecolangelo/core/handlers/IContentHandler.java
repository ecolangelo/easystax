package com.github.ecolangelo.core.handlers;

import com.github.ecolangelo.core.XmlNavigationPath;
import org.codehaus.stax2.XMLStreamReader2;

import javax.xml.stream.XMLStreamException;

/**
 * Created by eros on 30/09/14.
 */
public interface IContentHandler {

    void startDocument(XMLStreamReader2 xmlStreamReader);

    void startElement(XMLStreamReader2 xmlStreamReader) throws XMLStreamException;

    void character(XMLStreamReader2 character) throws XMLStreamException;

    void endElement(XMLStreamReader2 endElement) throws XMLStreamException;

    void attribute(XMLStreamReader2 streamReader) throws XMLStreamException;

    void endDocument(XMLStreamReader2 xmlStreamReader);


}
