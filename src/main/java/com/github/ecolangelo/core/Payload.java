package com.github.ecolangelo.core;

import org.codehaus.stax2.XMLStreamReader2;

import javax.xml.stream.XMLStreamReader;
import java.util.Map;

/**
 * Created by eros on 27/12/14.
 */
public class Payload {

    private Map<String,String> attributes;

    private String xml;

    private String text;

    private XMLStreamReader xmlStreamReader;

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = xml;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public XMLStreamReader getXmlStreamReader() {
        return xmlStreamReader;
    }

    public void setXmlStreamReader(XMLStreamReader xmlStreamReader) {
        this.xmlStreamReader = xmlStreamReader;
    }
}
