package com.github.ecolangelo;

import com.github.ecolangelo.core.Action;
import com.github.ecolangelo.core.ParseException;
import com.github.ecolangelo.core.WoodstoxFactory;
import com.github.ecolangelo.core.XmlNavigationPath;
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
        final XmlNavigationPath stack = new XmlNavigationPath();
        while(streamReader.hasNext()) {
            streamReader.next();
            int eventType = streamReader.getEventType();
            switch(eventType){
                case XMLStreamConstants.START_ELEMENT:{

                    String localPart = streamReader.getName().getLocalPart();
                    stack.pushTag(localPart);
                    notifyStaxEventToAll(new Action<IContentHandler>() {
                        @Override
                        public void execute(IContentHandler IContentHandler) throws Exception{
                            IContentHandler.startElement(streamReader, stack);
                        }
                    });
                    break;
                }
                case XMLStreamConstants.CHARACTERS:{
                    notifyStaxEventToAll(new Action<IContentHandler>() {
                        @Override
                        public void execute(IContentHandler IContentHandler) throws Exception{
                            IContentHandler.character(streamReader, stack);
                        }
                    });
                    break;
                }
                case XMLStreamConstants.ATTRIBUTE:{
                    notifyStaxEventToAll(new Action<IContentHandler>() {
                        @Override
                        public void execute(IContentHandler IContentHandler) throws Exception{
                            IContentHandler.attribute(streamReader, stack);
                        }
                    });
                    break;
                }
                case XMLStreamConstants.END_ELEMENT:{
                    notifyStaxEventToAll(new Action<IContentHandler>() {
                        @Override
                        public void execute(IContentHandler IContentHandler) throws Exception{
                            IContentHandler.endElement(streamReader, stack);
                        }
                    });
                    stack.popTag();
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

    public static IWith from(InputStream inputStream){
        return new Builder(inputStream);
    }

    public static IWith from(String xml){
        return new Builder(xml);
    }


    public static class Builder implements IFrom, IPath, IParse, IWith{

        private InputStream inputStream;

        private StaxParser parser;

        public Builder(InputStream inputStream) {
            this.inputStream = inputStream;

        }

        public Builder(String xml) {
            this.inputStream = new ByteArrayInputStream(xml.getBytes(Charset.forName("UTF-8")));
        }

        @Override
        public IParse path(String path, XmlFormat format) {
            parser.registerHandler(handler(format.id).path(path).asXml());
            return this;
        }

        @Override
        public IParse path(String path, TextFormat format) {
            parser.registerHandler(handler(format.id).path(path).asText());
            return this;
        }

        @Override
        public IParse forEach(String path, XmlFormat xmlFormat, Action<String> resultHandler) {
            parser.registerHandler(handler(xmlFormat.id).path(path).stream(resultHandler));
            return this;
        }

        @Override
        public Iterator<String> forEach(String id, XmlFormat format) {
            return null;
        }

        @Override
        public IPath with(XMLInputFactory xmlInputFactory) {
            parser = new StaxParser(xmlInputFactory);
            return this;
        }

        @Override
        public Map<String, String> parse() throws XMLStreamException {
            return parser.parse(inputStream);
        }
    }

    public interface IFrom {

        IParse path(String id, TextFormat path);

        IParse path(String id, XmlFormat path);
    }

    public interface IWith  {
        IPath with(XMLInputFactory inputFactory);
    }

    public interface IPath {
        IParse forEach(String id, XmlFormat format, Action<String> resultHandler);

        Iterator<String> forEach(String id, XmlFormat format);


        IParse path(String id, XmlFormat path);

        IParse path(String id, TextFormat path);
    }

    public static class XmlFormat{

        String id;

        public XmlFormat(String id) {
            this.id = id;
        }


    }

    public static class TextFormat{

        String id;

        public TextFormat(String id) {
            this.id = id;
        }

    }


    public interface IParse extends IPath{
        Map<String,String> parse() throws XMLStreamException;
    }

    public static  XmlFormat xml(String path){
        return new XmlFormat(path);
    }

    public static TextFormat text(String path){
        return new TextFormat(path);
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
