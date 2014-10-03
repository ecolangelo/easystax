package com.github.ecolangelo.core.handlers;

import com.github.ecolangelo.core.DummyClosure;
import com.github.ecolangelo.core.XmlNavigationPath;
import org.codehaus.stax2.XMLStreamReader2;

import javax.xml.stream.XMLStreamException;

/**
 * Created by eros on 04/10/14.
 */
public class StreamTagContentHandler extends TagContentExtractorHandler {

    DummyClosure<String> contentHandler;

    public StreamTagContentHandler(String id, String path, DummyClosure<String> contentHandler) {
        super(id,path);
        this.contentHandler = contentHandler;
    }


    @Override
    public void startElement(XMLStreamReader2 xmlStreamReader, XmlNavigationPath navigationStack) throws XMLStreamException {

    }

    @Override
    public void character(XMLStreamReader2 character, XmlNavigationPath navigationStack) throws XMLStreamException {

    }

    @Override
    public void endElement(XMLStreamReader2 endElement, XmlNavigationPath navigationStack) throws XMLStreamException {

    }

    @Override
    public void attribute(XMLStreamReader2 streamReader, XmlNavigationPath navigationStack) throws XMLStreamException {

    }

    @Override
    public String getOut() {
        return super.getOut();
    }
}
