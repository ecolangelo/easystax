package com.github.ecolangelo.core.handlers;

import com.github.ecolangelo.core.OnMatch;
import com.github.ecolangelo.core.Payload;
import com.github.ecolangelo.core.XmlNavigationPath;
import org.codehaus.stax2.XMLStreamReader2;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by eros on 27/12/14.
 */
public class StreamPayloadContentHandler extends SubXmlExtractorHandler {

    OnMatch contentHandler;
    Map<String, String> attributes = new HashMap<String, String>();


    public StreamPayloadContentHandler(String id, String path, OnMatch contentHandler) {
        super(id,path);
        this.contentHandler = contentHandler;
    }

    @Override
    public void startElement(XMLStreamReader2 xmlStreamReader, XmlNavigationPath navigationStack) throws XMLStreamException {
        if(path.equals(navigationStack.resolvePath())) {
            startRecording();
            for(int i = 0; i< xmlStreamReader.getAttributeCount();i++){
                QName attributeName1 = xmlStreamReader.getAttributeName(i);
                String value = xmlStreamReader.getAttributeValue(i);
                if(attributeName1 != null && value != null) {
                    attributes.put(attributeName1.getLocalPart(), value);
                }
            }
        }
        copyIfRecordingEnabled(xmlStreamReader);
    }

    @Override
    public void endElement(XMLStreamReader2 endElement, XmlNavigationPath navigationStack) throws XMLStreamException {
        copyIfRecordingEnabled(endElement);
        if(path.equals(navigationStack.resolvePath())){
            stopRecording();
            writer2.closeCompletely();
            try {
                Payload p = new Payload();
                p.setAttributes(attributes);
                p.setText(w.toString().replaceAll("<[^<>]+>", ""));
                p.setXml(w.toString());
                p.setXmlStreamReader(endElement);
                contentHandler.payload(p);
                w.close();
                w = new StringWriter();
            } catch (Exception e) {

            }
        }

    }
}
