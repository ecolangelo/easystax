package com.github.easystax.core.listeners;

import com.github.easystax.core.WoodstockInputFactory;
import com.github.easystax.core.XmlNavigationPath;
import com.github.easystax.core.builders.BuilderInitializationException;
import org.codehaus.stax2.XMLStreamReader2;
import org.codehaus.stax2.XMLStreamWriter2;

import javax.xml.stream.XMLStreamException;
import java.io.StringWriter;

/**
 * Created by eros on 08/09/14.
 */
public class EnclosingTextExtractorHandler implements ContentHandler {

    final protected String id;

    final protected String path;

    final private String lastElement;

    private boolean recording;

    private XMLStreamWriter2 writer2;

    protected StringWriter w = new StringWriter();

    EnclosingTextExtractorHandler(String id,String path) {
        this.path = path;
        this.lastElement = parseLastElement(path);
        try {
            writer2 = (XMLStreamWriter2) WoodstockInputFactory.getOutputFactory().createXMLStreamWriter(w);
        } catch (XMLStreamException e) {
            throw new BuilderInitializationException(e);
        }
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

    private void copyIfRecordingEnabled(XMLStreamReader2 xmlStreamReader) throws XMLStreamException {
        if(isRecording()) {
            writer2.copyEventFromReader(xmlStreamReader, true);
            writer2.flush();
        }
    }

    boolean isRecording() {
        return recording;
    }

    private void startRecording() {
        this.recording = true;
    }

    private void stopRecording() {
        this.recording = false;
    }


    private String parseLastElement(String path) {
        String[] splitPath = path.split("/");
        return splitPath[splitPath.length-1];
    }

    @Override
    public String getOut() {
        return w.toString().replaceAll("<[^a-zA-Z]?"+this.lastElement+"[^<>]*?>","");
    }

    @Override
    public String getId() {return id;}
}
