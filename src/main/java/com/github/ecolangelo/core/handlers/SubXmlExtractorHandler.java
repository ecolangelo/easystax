package com.github.ecolangelo.core.handlers;

import com.github.ecolangelo.core.*;
import com.github.ecolangelo.core.builders.BuilderInitializationException;
import com.github.ecolangelo.core.builders.Content;
import com.github.ecolangelo.core.builders.XmlPath;
import org.codehaus.stax2.XMLStreamReader2;
import org.codehaus.stax2.XMLStreamWriter2;

import javax.xml.stream.XMLStreamException;
import java.io.StringWriter;

public class SubXmlExtractorHandler implements IContentHandler {

    protected String id;

    protected String path;

    protected boolean recording;

    protected XMLStreamWriter2 writer2;

    protected XmlNavigationPath navigationStack = new XmlNavigationPath();

    protected StringWriter w = new StringWriter();

    public SubXmlExtractorHandler(String id, String path) {
        this.path = path;
        this.id = id;
    }

    public SubXmlExtractorHandler(String id) {
        this.id = id;
    }

    @Override
    public void startDocument(XMLStreamReader2 xmlStreamReader) {

    }

    @Override
    public void startElement(XMLStreamReader2 xmlStreamReader) throws XMLStreamException {
        String tagName = xmlStreamReader.getName().getLocalPart();
        navigationStack.pushTag(tagName);
        if(path.equals(navigationStack.resolvePath())) {
            startRecording();
        }
        copyIfRecordingEnabled(xmlStreamReader);
    }

    @Override
    public void character(XMLStreamReader2 character) throws XMLStreamException {
        copyIfRecordingEnabled(character);
    }

    @Override
    public void endElement(XMLStreamReader2 endElement) throws XMLStreamException {

        if(path.equals(navigationStack.resolvePath())){
            stopRecording();
            writer2.closeCompletely();
        }
        copyIfRecordingEnabled(endElement);
    }

    @Override
    public void attribute(XMLStreamReader2 streamReader) throws XMLStreamException {
        copyIfRecordingEnabled(streamReader);
    }

    protected void copyIfRecordingEnabled(XMLStreamReader2 xmlStreamReader) throws XMLStreamException {
        if(isRecording()) {
            writer2.copyEventFromReader(xmlStreamReader, true);
            writer2.flush();
        }
    }

    protected boolean isRecording() {
        return recording;
    }

    protected void startRecording() {
        try {
            writer2 = (XMLStreamWriter2) WoodstoxFactory.getOutputFactory().createXMLStreamWriter(w);
        } catch (XMLStreamException e) {
            throw new BuilderInitializationException(e);
        }
        this.recording = true;
    }

    protected void stopRecording() {
        this.recording = false;
    }

    @Override
    public String getOut() {
        return w.toString();
    }

    @Override
    public String getId() {return id;}

    @Override
    public void endDocument(XMLStreamReader2 xmlStreamReader) {

    }

    public static Builder handler(String id) {
        return new Builder(id);
    }

    public static class Builder implements XmlPath, Content{

        private String id;
        private String path;
        Action contentHandler;



        Builder(String id) {
            this.id = id;
        }



        @Override
        public IContentHandler stream(OnXmlSubPart resultHandler) {
            this.contentHandler =  resultHandler;
            return new StreamSubXmlContentHandler(id,path, (OnXmlSubPart) resultHandler);
        }

        @Override
        public IContentHandler stream(OnMatch resultHandler) {
            this.contentHandler = resultHandler;
            return new StreamPayloadContentHandler(id, path, resultHandler);
        }

        @Override
        public Content path(String path) {
            this.path = path.endsWith("/")?path:path+"/";
            return this;
        }
    }
}
