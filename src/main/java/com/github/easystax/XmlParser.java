package com.github.easystax;

import com.github.easystax.core.listeners.ContentHandler;

import javax.xml.stream.XMLStreamException;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * Created by eros on 06/09/14.
 */
public interface XmlParser {

    public Map<String,String> parse(String xml, Charset charset) throws XMLStreamException;

    public void addListener(ContentHandler contentHandler);
}
