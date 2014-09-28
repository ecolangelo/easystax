package com.github.easystax;

import com.github.easystax.core.DummyClosure;
import com.github.easystax.core.ParseException;
import com.github.easystax.core.WoodstockInputFactory;
import com.github.easystax.core.XmlNavigationPath;
import com.github.easystax.core.listeners.ContentHandler;
import org.codehaus.stax2.XMLStreamReader2;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import java.io.ByteArrayInputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eros on 06/09/14.
 */
public class StaxParser implements XmlParser{

    final private XMLInputFactory xmlInputFactory = WoodstockInputFactory.getInputFactory();

    final private List<ContentHandler> listeners = new ArrayList<ContentHandler>();

    public void parse(String xml,Charset charset) throws XMLStreamException {
        final XMLStreamReader2 streamReader = (XMLStreamReader2) xmlInputFactory.createXMLStreamReader(new ByteArrayInputStream(xml.getBytes(charset)));
        final XmlNavigationPath stack = new XmlNavigationPath();
        while(streamReader.hasNext()) {
            streamReader.next();
            int eventType = streamReader.getEventType();
            switch(eventType){
                case XMLStreamConstants.START_ELEMENT:{

                    String localPart = streamReader.getName().getLocalPart();
                    stack.pushTag(localPart);
                    notifyStaxEventToAll(new DummyClosure<ContentHandler>() {
                        @Override
                        public void cl(ContentHandler contentHandler) throws Exception{
                            contentHandler.startElement(streamReader, stack);
                        }
                    });
                    break;
                }
                case XMLStreamConstants.CHARACTERS:{
                    notifyStaxEventToAll(new DummyClosure<ContentHandler>() {
                        @Override
                        public void cl(ContentHandler contentHandler) throws Exception{
                            contentHandler.character(streamReader, stack);
                        }
                    });
                    break;
                }
                case XMLStreamConstants.ATTRIBUTE:{
                    notifyStaxEventToAll(new DummyClosure<ContentHandler>() {
                        @Override
                        public void cl(ContentHandler contentHandler) throws Exception{
                            contentHandler.attribute(streamReader, stack);
                        }
                    });
                    break;
                }
                case XMLStreamConstants.END_ELEMENT:{
                    notifyStaxEventToAll(new DummyClosure<ContentHandler>() {
                        @Override
                        public void cl(ContentHandler contentHandler) throws Exception{
                            contentHandler.endElement(streamReader, stack);
                        }
                    });
                    stack.popTag();
                    break;

                }
            }
        }
    }

    @Override
    public void addListener(ContentHandler contentHandler) {
        if(!listeners.contains(contentHandler))listeners.add(contentHandler);
    }


    private void notifyStaxEventToAll(DummyClosure<ContentHandler> closure){
        for(ContentHandler listener:listeners){
            try {
                closure.cl(listener);
            } catch (Exception e) {
                throw new ParseException(e);
            }
        }
    }

}
