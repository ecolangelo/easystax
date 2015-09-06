package com.github.ecolangelo;

import com.github.ecolangelo.core.*;
import com.github.ecolangelo.core.handlers.IContentHandler;
import org.codehaus.stax2.XMLStreamReader2;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.*;

import static com.github.ecolangelo.core.handlers.SubXmlExtractorHandler.handler;


public class StaxParser implements XmlParser{

    private XMLInputFactory xmlInputFactory = WoodstoxFactory.getInputFactory();

    final private List<IContentHandler> handlers = new ArrayList<IContentHandler>();

    public StaxParser() {}

    public StaxParser(XMLInputFactory xmlInputFactory){
        this.xmlInputFactory = xmlInputFactory;
    }


    public Map<String, String> parse(String xml, Charset charset) throws XMLStreamException {
        return parse(new ByteArrayInputStream(xml.getBytes(charset)));
    }

    @Override
    public Map<String, String> parse(InputStream input) throws XMLStreamException {
        final XMLStreamReader2 streamReader = (XMLStreamReader2) xmlInputFactory.createXMLStreamReader(input);
        return parse(streamReader);
    }

    private Map<String, String> parse(final XMLStreamReader2 streamReader) throws XMLStreamException {
        while(streamReader.hasNext()) {
            streamReader.next();
            int eventType = streamReader.getEventType();
            switch(eventType){
                case XMLStreamConstants.START_ELEMENT:{
                    notifyStaxEventToAll(new Action<IContentHandler>() {
                        @Override
                        public void execute(IContentHandler IContentHandler) throws Exception{
                            IContentHandler.startElement(streamReader);
                        }
                    });
                    break;
                }
                case XMLStreamConstants.CHARACTERS:{
                    notifyStaxEventToAll(new Action<IContentHandler>() {
                        @Override
                        public void execute(IContentHandler IContentHandler) throws Exception{
                            IContentHandler.character(streamReader);
                        }
                    });
                    break;
                }
                case XMLStreamConstants.ATTRIBUTE:{
                    notifyStaxEventToAll(new Action<IContentHandler>() {
                        @Override
                        public void execute(IContentHandler IContentHandler) throws Exception{
                            IContentHandler.attribute(streamReader);
                        }
                    });
                    break;
                }
                case XMLStreamConstants.END_ELEMENT:{
                    notifyStaxEventToAll(new Action<IContentHandler>() {
                        @Override
                        public void execute(IContentHandler IContentHandler) throws Exception{
                            IContentHandler.endElement(streamReader);
                        }
                    });
                    break;
                }
            }
        }

        Map<String,String> results = new HashMap<String, String>();
        for(IContentHandler ch : handlers){
            results.put(ch.getId(),ch.getOut());
        }
        return results;
    }


    public void registerHandler(IContentHandler contentHandler) {
        if(!handlers.contains(contentHandler)) handlers.add(contentHandler);
    }


    public void register(IContentHandler... IContentHandlers) {
        for(IContentHandler ch: IContentHandlers) {
            if(!handlers.contains(ch)) {
                handlers.add(ch);
            }
        }
    }

    private void notifyStaxEventToAll(Action<IContentHandler> closure){
        for(IContentHandler listener: handlers){
            try {
                closure.execute(listener);
            } catch (Exception e) {
                throw new ParseException(e);
            }
        }
    }

    public static Builder from(InputStream inputStream){
        return new Builder(inputStream);
    }



    public static Builder from(String xml){
        return new Builder(xml);
    }


    public static class Builder implements  IPath, IParse, IProperties {

        private InputStream inputStream;

        private StaxParser parser;

        public Builder(InputStream inputStream) {
            this.inputStream = inputStream;

        }

        public Builder(String xml) {
            this.inputStream = new ByteArrayInputStream(xml.getBytes(Charset.forName("UTF-8")));
        }

        @Override
        public IParse forEach(String path, OnXmlSubPart resultHandler) {
            parser = new StaxParser(woodstoxInputFactory());
            parser.registerHandler(handler(UUID.randomUUID().toString()).path(path).stream(resultHandler));
            return this;
        }

        @Override
        public IParse forEach(String path, OnMatch resultHandler) {
            parser = new StaxParser(woodstoxInputFactory());
            parser.registerHandler(handler(UUID.randomUUID().toString()).path(path).stream(resultHandler));
            return this;
        }

        @Override
        public IPath properties(Map<String,Object> xmlParserProperties) {
            parser = new StaxParser(woodstoxInputFactory(xmlParserProperties));
            return this;
        }

        @Override
        public void parse() throws XMLStreamException {
            parser.parse(inputStream);
        }
    }


    public interface IProperties {
        IPath properties(Map<String,Object> xmlParserProperties);
    }

    public interface IPath {
        IParse forEach(String path , OnXmlSubPart resultHandler);

        IParse forEach(String path, OnMatch resultHandler);
    }


    public interface IParse extends IPath{
        void parse() throws XMLStreamException;
    }


    public static XMLInputFactory woodstoxInputFactory() {
        return WoodstoxFactory.getInputFactory();
    }

    public static XMLInputFactory woodstoxInputFactory(Map<String, Object> inputFactorySettings){
        XMLInputFactory inputFactory = WoodstoxFactory.getInputFactory();
        for(Map.Entry<String,Object> entry : inputFactorySettings.entrySet()) {
            inputFactory.setProperty(entry.getKey(),entry.getValue());
        }
        return inputFactory;
    }

}
