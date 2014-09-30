package com.github.ecolangelo.core.handlers;

import com.github.ecolangelo.core.WoodstockFactory;
import com.github.ecolangelo.core.XmlNavigationPath;
import com.github.ecolangelo.core.builders.BuilderInitializationException;
import com.github.ecolangelo.core.builders.Content;
import com.github.ecolangelo.core.builders.XmlPath;
import org.codehaus.stax2.XMLStreamReader2;
import org.codehaus.stax2.XMLStreamWriter2;

import javax.xml.stream.XMLStreamException;
import java.io.StringWriter;

/**
 * Created by eros on 08/09/14.
 */
public class SubXmlExtractorHandler implements IContentHandler {

    protected String id;

    protected String path;

    private boolean recording;

    protected XMLStreamWriter2 writer2;

    protected StringWriter w = new StringWriter();

    SubXmlExtractorHandler(String id, String path) {
        this.path = path;
        this.id = id;
    }

    SubXmlExtractorHandler(String id) {
        this.id = id;
    }


    @Override
    public void startElement(XMLStreamReader2 xmlStreamReader, XmlNavigationPath navigationStack) throws XMLStreamException {
        if(path.equals(navigationStack.resolvePath())) {
            startRecording();
        }
        copyIfRecordingEnabled(xmlStreamReader);
    }

    @Override
    public void character(XMLStreamReader2 character, XmlNavigationPath navigationStack) throws XMLStreamException {
        copyIfRecordingEnabled(character);
    }

    @Override
    public void endElement(XMLStreamReader2 endElement, XmlNavigationPath navigationStack) throws XMLStreamException {
        copyIfRecordingEnabled(endElement);
        if(path.equals(navigationStack.resolvePath())){
            stopRecording();
            writer2.closeCompletely();
        }
    }

    @Override
    public void attribute(XMLStreamReader2 streamReader, XmlNavigationPath navigationStack) throws XMLStreamException {
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
            writer2 = (XMLStreamWriter2) WoodstockFactory.getOutputFactory().createXMLStreamWriter(w);
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



    public static Builder build(String id) {
        return new Builder(id);
    }

    public static class Builder implements XmlPath,Content {

        private String id;
        private String path;

        Builder(String id) {
            this.id = id;
        }

        @Override
        public IContentHandler text() {
            return new TagContentExtractorHandler(id, path);
        }

        @Override
        public IContentHandler subXml() {
            return new SubXmlExtractorHandler(id,path);
        }

        @Override
        public IContentHandler attribute(String attributeName) {
            return new AttributeValueExtractorHandler(id,path,attributeName);
        }

        @Override
        public Content withPath(String path) {
            this.path = path;
            return this;
        }
    }
}
