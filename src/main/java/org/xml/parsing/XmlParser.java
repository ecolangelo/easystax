package org.xml.parsing;

import org.xml.parsing.core.listeners.ContentHandler;

import javax.xml.stream.XMLStreamException;
import java.nio.charset.Charset;

/**
 * Created by eros on 06/09/14.
 */
public interface XmlParser {

    public void parse(String xml, Charset charset) throws XMLStreamException;

    public void addListener(ContentHandler contentHandler);
}
