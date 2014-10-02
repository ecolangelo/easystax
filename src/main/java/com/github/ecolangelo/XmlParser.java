package com.github.ecolangelo;

import javax.xml.stream.XMLStreamException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;


public interface XmlParser {
    public Map<String,String> parse(InputStream inputStream) throws XMLStreamException;

    public Map<String,String> parse(String xml, Charset charset) throws XMLStreamException;
}
