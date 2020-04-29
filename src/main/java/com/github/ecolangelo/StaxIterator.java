package com.github.ecolangelo;

import com.github.ecolangelo.core.*;
import com.github.ecolangelo.core.handlers.NodeBasedContentHandler;
import org.codehaus.stax2.XMLStreamReader2;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

public class StaxIterator implements Iterator<ParsingResult> {


    private XMLInputFactory xmlInputFactory = WoodstoxFactory.getInputFactory();
    private XMLStreamReader2 streamReader;

    private NodeBasedContentHandler IContentHandler;
    private ParsingResult currentResult;

    private StaxIterator(String xml, Charset charset) {


    }

    Consumer<ParsingResult> handler = new Consumer<ParsingResult>() {
        @Override
        public void accept(ParsingResult parsingResult) {

        }
    };


    public StaxIterator(InputStream input, String path) {
        IContentHandler = new NodeBasedContentHandler(Node.createNodeFromXpath(path),new XPathNodeMatchingStrategy());
        try {
            streamReader = (XMLStreamReader2) xmlInputFactory.createXMLStreamReader(input);
        } catch (XMLStreamException e) {
            throw new ParseException(e);
        }
    }


    @Override
    public boolean hasNext() {
        try {
            while (streamReader.hasNext()) {
                streamReader.next();
                int eventType = streamReader.getEventType();
                switch (eventType) {
                    case XMLStreamConstants.START_ELEMENT: {

                        try {
                            IContentHandler.startElement(streamReader);
                        } catch (XMLStreamException e) {
                            throw new ParseException(e);
                        }

                        break;
                    }
                    case XMLStreamConstants.CHARACTERS: {

                        try {
                            IContentHandler.character(streamReader);
                        } catch (XMLStreamException e) {
                            throw new ParseException(e);
                        }

                        break;
                    }
                    case XMLStreamConstants.ATTRIBUTE: {

                        try {
                            IContentHandler.attribute(streamReader);
                        } catch (XMLStreamException e) {
                            throw new ParseException(e);
                        }

                        break;
                    }
                    case XMLStreamConstants.END_ELEMENT: {

                        try {
                            IContentHandler.endElement(streamReader);
                            if(IContentHandler.getCurrentParsingResult() != null) {
                                this.currentResult =  IContentHandler.getCurrentParsingResult();
                                return true;
                            }
                        } catch (XMLStreamException e) {
                            throw new ParseException(e);
                        }


                    }
                }
            }
        } catch (XMLStreamException xse) {
            throw new ParseException(xse);
        }

        return false;
    }

    @Override
    public ParsingResult next() {
        if(!hasNext())
            throw new NoSuchElementException("iterator exhausted");

        return currentResult;
    }

    @Override
    public void remove() {

    }

    @Override
    public void forEachRemaining(Consumer<? super ParsingResult> action) {

    }
}
