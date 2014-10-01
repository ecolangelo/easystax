package com.github.ecolangelo.core.handlers;

import com.github.ecolangelo.core.XmlNavigationPath;
import org.codehaus.stax2.XMLStreamReader2;

import javax.xml.stream.XMLStreamException;

/**
 * Created by eros on 30/09/14.
 */
public class TagContentExtractorHandler extends SubXmlExtractorHandler {

    String tagContent = "";


    TagContentExtractorHandler(String id, String path) {
        super(id, path);
    }

    @Override
    public void startElement(XMLStreamReader2 xmlStreamReader, XmlNavigationPath navigationStack) throws XMLStreamException {
        if(path.equals(navigationStack.resolvePath())) {
            startRecording();
        }
    }

    @Override
    public void character(XMLStreamReader2 character, XmlNavigationPath navigationStack) throws XMLStreamException {
        if(isRecording())tagContent = character.getText();
    }

    @Override
    public void endElement(XMLStreamReader2 endElement, XmlNavigationPath navigationStack) throws XMLStreamException {
        if(path.equals(navigationStack.resolvePath())){
            stopRecording();
        }
    }

    @Override
    public void attribute(XMLStreamReader2 streamReader, XmlNavigationPath navigationStack) throws XMLStreamException {

    }



    @Override
    public String getOut() {
        return tagContent;
    }
}
