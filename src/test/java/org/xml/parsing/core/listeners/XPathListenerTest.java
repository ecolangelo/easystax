package org.xml.parsing.core.listeners;

import com.ctc.wstx.stax.WstxInputFactory;

import org.junit.Ignore;
import org.junit.Test;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import java.io.ByteArrayInputStream;

public class XPathListenerTest {

    private WstxInputFactory wstxInputFactory = new WstxInputFactory();

    @Ignore
    public void testStartDocument() throws Exception {
        String xml = "<root><teiHeader type=\"application\">some text<teiHeader><root>";
        XMLStreamReader reader =  wstxInputFactory.createXMLStreamReader(new ByteArrayInputStream(xml.getBytes("UTF-8")));
        EnclosingTextExtractorHandler listener = new EnclosingTextExtractorHandler("/root/");
        while(reader.hasNext()){
            switch(reader.getEventType()){
                case XMLStreamConstants.START_ELEMENT:
            }

        }

    }


    @Test
    public void testStartElement() throws Exception {

    }

    @Test
    public void testCharacter() throws Exception {

    }

    @Test
    public void testEndElement() throws Exception {

    }

    @Test
    public void testEndDocument() throws Exception {

    }
}