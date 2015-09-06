package com.github.ecolangelo;

import javax.xml.stream.XMLStreamException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;


public interface XmlParser {
    void parse(InputStream inputStream) throws XMLStreamException;

    void parse(String xml, Charset charset) throws XMLStreamException;
}
