package com.github.ecolangelo.core.handlers;

import com.github.ecolangelo.core.XmlNavigationPath;
import org.codehaus.stax2.XMLStreamReader2;

import javax.xml.stream.XMLStreamException;

/**
 * Created by eros on 30/09/14.
 */
public class AttributeValueExtractorHandler extends SubXmlExtractorHandler{


    private String attributeValue;
    private String attributeName;

    AttributeValueExtractorHandler(String id, String path, String attributeName) {
        super(id, path);
        this.attributeName = attributeName;
    }

    AttributeValueExtractorHandler(String id, String path) {
        super(id, path);
    }

    AttributeValueExtractorHandler(String id) {
        super(id);
    }

    @Override
    public void startElement(XMLStreamReader2 xmlStreamReader, XmlNavigationPath navigationStack) throws XMLStreamException {
        if(path.equals(navigationStack.resolvePath())) {
            startRecording();
        }
    }

    @Override
    public void endElement(XMLStreamReader2 endElement, XmlNavigationPath navigationStack) throws XMLStreamException {
        if(path.equals(navigationStack.resolvePath())){
            stopRecording();
            writer2.closeCompletely();
        }
    }

    @Override
    public void character(XMLStreamReader2 character, XmlNavigationPath navigationStack) throws XMLStreamException {

    }

    @Override
    public void attribute(XMLStreamReader2 streamReader, XmlNavigationPath navigationStack) throws XMLStreamException {

       if(isRecording()){
           String value = streamReader.getAttributeValue(null,attributeName);
           if(value != null) {
               attributeValue = value;
           }
       }
    }

    @Override
    public String getOut() {
        return attributeValue;
    }
}
