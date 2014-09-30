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
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by eros on 06/09/14.
 */
public class StaxParser implements XmlParser{

    final private XMLInputFactory xmlInputFactory = WoodstockFactory.getInputFactory();

    final private List<IContentHandler> handlers = new ArrayList<IContentHandler>();

    public Map<String,String> parse(String xml,Charset charset) throws XMLStreamException {
        final XMLStreamReader2 streamReader = (XMLStreamReader2) xmlInputFactory.createXMLStreamReader(new ByteArrayInputStream(xml.getBytes(charset)));
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


    public void registerHandlers(IContentHandler... IContentHandlers) {
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

}
