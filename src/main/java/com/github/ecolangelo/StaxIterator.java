package com.github.ecolangelo;

import com.github.ecolangelo.core.WoodstoxFactory;
import com.github.ecolangelo.core.XmlNavigationPath;
import com.github.ecolangelo.core.handlers.SubXmlExtractorHandler;
import org.codehaus.stax2.XMLStreamReader2;

import javax.xml.stream.XMLStreamException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Iterator;

/**
 * Created by eros on 06/10/14.
 */
public class StaxIterator extends SubXmlExtractorHandler implements Iterator<String> {

    private InputStream is;
    private String current;
    private XMLStreamReader2 reader2;

    boolean flag;

    public StaxIterator(String id, String path,InputStream inputStream) throws XMLStreamException {
        super(id, path);
        flag = true;
        is = inputStream;
        reader2 = (XMLStreamReader2) WoodstoxFactory.getInputFactory().createXMLStreamReader(inputStream);
        iterate();
    }

    private void iterate() throws XMLStreamException {
        XmlNavigationPath xmlNavigationPath = new XmlNavigationPath();
        while (reader2.hasNext()){
            reader2.next();
            switch (reader2.getEventType()){
                case XMLStreamReader2.START_ELEMENT:{
                    xmlNavigationPath.pushTag(reader2.getName().getLocalPart());
                    startElement(reader2, xmlNavigationPath);
                    break;
                }
                case XMLStreamReader2.ATTRIBUTE:{
                    attribute(reader2,xmlNavigationPath);
                    break;
                }
                case XMLStreamReader2.CHARACTERS:{
                    character(reader2,xmlNavigationPath);
                    break;
                }
                case XMLStreamReader2.END_ELEMENT:{
                    endElement(reader2,xmlNavigationPath);
                    xmlNavigationPath.popTag();
                    break;
                }

            }
        }

    }




    @Override
    public void endElement(XMLStreamReader2 endElement, XmlNavigationPath navigationStack) throws XMLStreamException {
        copyIfRecordingEnabled(endElement);
        if(path.equals(navigationStack.resolvePath())){
            stopRecording();
            writer2.closeCompletely();
            try {
                current = w.toString();
                w = new StringWriter();
            } catch (Exception e) {

            }
        }

    }

    @Override
    public String getOut() {
        return null;
    }

    @Override
    public String getId() {
        return "default";
    }

    @Override
    public boolean hasNext() {
        return flag;
    }

    @Override
    public String next() {
        return current;
    }

    @Override
    public void remove() {

    }
}
