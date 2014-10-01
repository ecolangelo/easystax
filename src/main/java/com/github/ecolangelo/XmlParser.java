package com.github.ecolangelo;

import javax.xml.stream.XMLStreamException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * Created by eros on 06/09/14.
 */
public interface XmlParser {

    public Map<String,String> parse(String xml, Charset charset) throws XMLStreamException;

    public Map<String, String> parse(InputStream input) throws XMLStreamException;

}
