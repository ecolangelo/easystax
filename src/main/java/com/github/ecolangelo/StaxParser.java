package com.github.ecolangelo;

import com.github.ecolangelo.core.*;
import com.github.ecolangelo.core.handlers.IContentHandler;
import com.github.ecolangelo.core.handlers.NodeBasedContentHandler;
import com.github.ecolangelo.core.pojo.XmlPath;
import org.codehaus.stax2.XMLStreamReader2;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.*;


public class StaxParser implements XmlParser{

    private XMLInputFactory xmlInputFactory = WoodstoxFactory.getInputFactory();

    final private List<IContentHandler> handlers = new ArrayList<IContentHandler>();

    public StaxParser(XMLInputFactory xmlInputFactory){
        this.xmlInputFactory = xmlInputFactory;
    }


    public void parse(String xml, Charset charset) throws XMLStreamException {
        parse(new ByteArrayInputStream(xml.getBytes(charset)));
    }

    @Override
    public void parse(InputStream input) throws XMLStreamException {
        final XMLStreamReader2 streamReader = (XMLStreamReader2) xmlInputFactory.createXMLStreamReader(input);
        parse(streamReader);
    }

    private void parse(final XMLStreamReader2 streamReader) throws XMLStreamException {
        while(streamReader.hasNext()) {
            streamReader.next();
            int eventType = streamReader.getEventType();
            switch(eventType){
                case XMLStreamConstants.START_ELEMENT:{
                    notifyStaxEventToAll(IContentHandler -> {
                        try {
                            IContentHandler.startElement(streamReader);
                        } catch (XMLStreamException e) {
                            throw new ParseException(e);
                        }
                    });
                    break;
                }
                case XMLStreamConstants.CHARACTERS:{
                    notifyStaxEventToAll(IContentHandler -> {
                        try {
                            IContentHandler.character(streamReader);
                        } catch (XMLStreamException e) {
                            throw new ParseException(e);
                        }
                    });
                    break;
                }
                case XMLStreamConstants.ATTRIBUTE:{
                    notifyStaxEventToAll(IContentHandler -> {
                        try {
                            IContentHandler.attribute(streamReader);
                        } catch (XMLStreamException e) {
                            throw new ParseException(e);
                        }
                    });
                    break;
                }
                case XMLStreamConstants.END_ELEMENT:{
                    notifyStaxEventToAll(IContentHandler -> {
                        try {
                            IContentHandler.endElement(streamReader);
                        } catch (XMLStreamException e) {
                            throw new ParseException(e);
                        }
                    });
                    break;
                }
            }
        }
    }


    public void registerHandler(IContentHandler contentHandler) {
        if(!handlers.contains(contentHandler)) handlers.add(contentHandler);
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


    public static class Builder<T> implements  IPath, IParse, ForEach, IProperties ,IBind<T>{

        private InputStream inputStream;

        private StaxParser parser;

        private NodeBasedContentHandler currentContentHandler;

        public Builder(InputStream inputStream) {
            this.inputStream = inputStream;


        }

        public Builder(String xml) {
            this.inputStream = new ByteArrayInputStream(xml.getBytes(Charset.forName("UTF-8")));
        }

        @Override
        public ForEach forEach(String path) {
            currentContentHandler = new NodeBasedContentHandler(Node.createNodeFromXpath(path), new XPathNodeMatchingStrategy());
            return this;
        }

        @Override
        public IResult<T> bindWith(Class<T> classType) {
            return new PojoBuilderParser<T>(inputStream, classType);
        }

        @Override
        public IParse stream(OnMatch match) {
            if(parser == null)parser = new StaxParser(woodstoxInputFactory());
            currentContentHandler.setHandler(match);
            parser.registerHandler(currentContentHandler);
            return this;
        }

        @Override
        public ForEach addTo(final Collection<ParsingResult> result) {
            if(parser == null)parser = new StaxParser(woodstoxInputFactory());
            currentContentHandler.setHandler(new OnMatch() {
                @Override
                public void payload(ParsingResult payload) {
                    result.add(payload);
                }
            });
            parser.registerHandler(currentContentHandler);
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

    public static class PojoBuilderParser<T> implements IResult<T> ,IParse{

        private InputStream is;
        private Class<T> type;
        private StaxParser parser = new StaxParser(woodstoxInputFactory());
        public PojoBuilderParser (InputStream is, Class<T> pojoType){
            this.is = is;
            this.type = pojoType;

        }

        @Override
        public IParse addTo(Collection<T> results) {
            return null;
        }

        @Override
        public IParse stream(Action<T> action) {
            List<Annotation> xmlPathAnnotations = getAnnotationsOnFields(XmlPath.class, type);

            for(Annotation a : xmlPathAnnotations){
                XmlPath xmlPath = (XmlPath)a;
                NodeBasedContentHandler contentHandler = new NodeBasedContentHandler(Node.createNodeFromXpath(xmlPath.value()), new XPathNodeMatchingStrategy());
                contentHandler.setHandler(new OnMatch() {
                    @Override
                    public void payload(ParsingResult payload) {
                        T t = null;
                        try {
                            t = type.newInstance();
                            inject(t, payload.getContent());
                        } catch (InstantiationException | IllegalAccessException e) {
                            throw new ParseException(e);
                        }
                        action.execute(t);
                    }
                });
                parser.registerHandler(contentHandler);

            }
            return this;
        }

        @Override
        public void parse() throws XMLStreamException {
            parser.parse(is);
        }
    }

    public static void inject(Object instance, Object value) {
        Field[] fields = instance.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(XmlPath.class)) {
                XmlPath set = field.getAnnotation(XmlPath.class);
                field.setAccessible(true);
                try {
                    field.set(instance, value);
                } catch (IllegalAccessException e) {
                    throw new ParseException(e);
                }

            }
        }
    }

    public static List<Annotation> getAnnotationsOnFields(Class annotationType, Class type) {
        Field[] fields = type.getDeclaredFields();
        List<Annotation> annotations = new ArrayList<>();
        for (Field field : fields) {
            if (field.isAnnotationPresent(annotationType)) {
                Annotation set = field.getAnnotation(annotationType);
                annotations.add(set);

            }
        }
        return annotations;
    }


    public interface IProperties {
        IPath properties(Map<String,Object> xmlParserProperties);
    }

    public interface IPath {
        ForEach forEach(String path);
    }

    public interface ForEach {

        ForEach forEach(String path);

        IParse stream(OnMatch match);

        ForEach addTo(Collection<ParsingResult> result);

        void parse() throws XMLStreamException;

    }

    public interface IBind<T> {
        public IResult<T> bindWith(Class<T> classType);
    }

    public interface IResult<T> {
        IParse addTo(Collection<T> results);

        IParse stream(Action<T> action);
    }


    public interface IParse{
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
