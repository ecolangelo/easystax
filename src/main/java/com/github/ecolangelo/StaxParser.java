package com.github.ecolangelo;

import com.github.ecolangelo.core.DummyClosure;
import com.github.ecolangelo.core.ParseException;
import com.github.ecolangelo.core.WoodstockFactory;
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

/**
 * Created by eros on 06/09/14.
 */
public class StaxParser implements XmlParser{

    private XMLInputFactory xmlInputFactory = WoodstockFactory.getInputFactory();

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
                    notifyStaxEventToAll(new DummyClosure<IContentHandler>() {
                        @Override
                        public void cl(IContentHandler IContentHandler) throws Exception{
                            IContentHandler.startElement(streamReader, stack);
                        }
                    });
                    break;
                }
                case XMLStreamConstants.CHARACTERS:{
                    notifyStaxEventToAll(new DummyClosure<IContentHandler>() {
                        @Override
                        public void cl(IContentHandler IContentHandler) throws Exception{
                            IContentHandler.character(streamReader, stack);
                        }
                    });
                    break;
                }
                case XMLStreamConstants.ATTRIBUTE:{
                    notifyStaxEventToAll(new DummyClosure<IContentHandler>() {
                        @Override
                        public void cl(IContentHandler IContentHandler) throws Exception{
                            IContentHandler.attribute(streamReader, stack);
                        }
                    });
                    break;
                }
                case XMLStreamConstants.END_ELEMENT:{
                    notifyStaxEventToAll(new DummyClosure<IContentHandler>() {
                        @Override
                        public void cl(IContentHandler IContentHandler) throws Exception{
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


    public void registerHandler(IContentHandler IContentHandler) {
        if(!handlers.contains(IContentHandler)) handlers.add(IContentHandler);
    }


    public void register(IContentHandler... IContentHandlers) {
        for(IContentHandler ch: IContentHandlers) {
            if(!handlers.contains(ch)) {
                handlers.add(ch);
            }
        }
    }

    private void notifyStaxEventToAll(DummyClosure<IContentHandler> closure){
        for(IContentHandler listener: handlers){
            try {
                closure.cl(listener);
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
        public IParse path(XmlFormat<String> id, String path) {
            parser.registerHandler(handler(id.key).path(path).asXml());
            return this;
        }

        @Override
        public IParse path(TextFormat<String> id, String path) {
            parser.registerHandler(handler(id.key).path(path).asText());
            return this;
        }

        @Override
        public IParse forEach(XmlFormat<String> id, String path, DummyClosure<String> resultHandler) {
            parser.registerHandler(handler(id.key).path(path).stream(resultHandler));
            return this;
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

        IParse path(XmlFormat<String> id,String path);
    }

    public interface IWith  {
        IPath with(XMLInputFactory inputFactory);
    }

    public interface IPath {
        IParse forEach(XmlFormat<String> id, String path, DummyClosure<String> resultHandler);

        IParse path(XmlFormat<String> id, String path);

        IParse path(TextFormat<String> id, String path);
    }

    public static class XmlFormat<T>{

        T key;

        public XmlFormat(T t) {
            this.key = t;
        }


    }

    public static class TextFormat<T>{

        T key;

        public TextFormat(T t) {
            this.key = t;
        }

    }


    public interface IParse extends IPath{
        Map<String,String> parse() throws XMLStreamException;
    }

    public static <T> XmlFormat<T> xml(T t){
        return new XmlFormat<T>(t);
    }

    public static <T> TextFormat<T> text(T t){
        return new TextFormat<T>(t);
    }

    public static XMLInputFactory woodstockInputFactory() {
        return WoodstockFactory.getInputFactory();
    }

    public static XMLInputFactory woodstockInputFactory(Map<String,Object> inputFactorySettings){
        XMLInputFactory inputFactory = WoodstockFactory.getInputFactory();
        for(Map.Entry<String,Object> entry : inputFactorySettings.entrySet()) {
            inputFactory.setProperty(entry.getKey(),entry.getValue());
        }
        return inputFactory;
    }

}
