package com.github.ecolangelo.core.handlers;

import com.github.ecolangelo.core.*;
import com.github.ecolangelo.core.builders.BuilderInitializationException;
import org.codehaus.stax2.XMLStreamReader2;
import org.codehaus.stax2.XMLStreamWriter2;

import javax.xml.stream.XMLStreamException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class NodeBasedContentHandler implements IContentHandler {

    private NodeMatchingStrategy matchingStrategy;
    private Action<ParsingResult> handler;
    private Node referenceNode;
    private Node currentNode;

    public NodeBasedContentHandler(Node referenceNode, NodeMatchingStrategy matchingStrategy, Action<ParsingResult> handler) {
        this.referenceNode = referenceNode;
        this.matchingStrategy = matchingStrategy;
        this.handler = handler;
    }

    public NodeBasedContentHandler(Node referenceNode, NodeMatchingStrategy matchingStrategy) {
        this.referenceNode = referenceNode;
        this.matchingStrategy = matchingStrategy;
    }

    protected boolean recording;

    protected XMLStreamWriter2 writer2;

    protected StringWriter w = new StringWriter();

    @Override
    public void startDocument(XMLStreamReader2 xmlStreamReader) {

    }

    @Override
    public void startElement(XMLStreamReader2 xmlStreamReader) throws XMLStreamException {
        push(xmlStreamReader);

        if (matchingStrategy.match(referenceNode, currentNode)) {
            startRecording();
        }
        copyIfRecordingEnabled(xmlStreamReader);
    }

    private void push(XMLStreamReader2 xmlStreamReader) throws XMLStreamException {
        Node node = createNode(xmlStreamReader);
        if(currentNode == null){
            currentNode = node;
        }else{
            currentNode = currentNode.append(node);
        }
    }

    private Node createNode(XMLStreamReader2 xmlStreamReader) throws XMLStreamException {
        Map<String, String> attributes = getAttributes(xmlStreamReader);
        return new Node(null, attributes, xmlStreamReader.getName().getLocalPart());
    }

    private Map<String, String> getAttributes(XMLStreamReader2 xmlStreamReader) throws XMLStreamException {
        int attributesCount = xmlStreamReader.getAttributeCount();
        Map<String, String> attributes = new HashMap<String, String>();
        for(int i = 0;i<attributesCount;i++) {
            String name = xmlStreamReader.getAttributeName(i).getLocalPart();
            String value = xmlStreamReader.getAttributeValue(i);
            attributes.put(name, value);
        }
        return attributes;
    }

    @Override
    public void character(XMLStreamReader2 character) throws XMLStreamException {
        copyIfRecordingEnabled(character);
    }

    @Override
    public void endElement(XMLStreamReader2 endElement) throws XMLStreamException {
        copyIfRecordingEnabled(endElement);
        if (matchingStrategy.match(referenceNode, currentNode)) {
            stopRecording();
            writer2.closeCompletely();
            try {
                ParsingResult t = new ParsingResult();
                t.setContent(w.toString());
                t.setNode(currentNode);
                handler.execute(t);
                w.close();
                w = new StringWriter();
            } catch (Exception e) {

            }
        }
        pop();
    }

    private void pop() {
       currentNode = currentNode.getParent();
    }

    @Override
    public void attribute(XMLStreamReader2 streamReader) throws XMLStreamException {
        copyIfRecordingEnabled(streamReader);
    }

    protected void copyIfRecordingEnabled(XMLStreamReader2 xmlStreamReader) throws XMLStreamException {
        if (isRecording()) {
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
    public void endDocument(XMLStreamReader2 xmlStreamReader) {

    }


    public Action<ParsingResult> getHandler() {
        return handler;
    }

    public void setHandler(Action<ParsingResult> handler) {
        this.handler = handler;
    }
}
