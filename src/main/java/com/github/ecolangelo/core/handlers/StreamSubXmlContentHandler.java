package com.github.ecolangelo.core.handlers;

import com.github.ecolangelo.core.DummyClosure;
import com.github.ecolangelo.core.XmlNavigationPath;
import org.codehaus.stax2.XMLStreamReader2;

import javax.xml.stream.XMLStreamException;
import java.io.StringWriter;

/**
 * Created by eros on 04/10/14.
 */
public class StreamSubXmlContentHandler extends SubXmlExtractorHandler {

    DummyClosure<String> contentHandler;

    public StreamSubXmlContentHandler(String id, String path, DummyClosure<String> contentHandler) {
        super(id,path);
        this.contentHandler = contentHandler;
    }

    @Override
    public void endElement(XMLStreamReader2 endElement, XmlNavigationPath navigationStack) throws XMLStreamException {
        copyIfRecordingEnabled(endElement);
        if(path.equals(navigationStack.resolvePath())){
            stopRecording();
            writer2.closeCompletely();
            try {
                contentHandler.cl(w.toString());
                w.close();
                w = new StringWriter();
            } catch (Exception e) {

            }
        }

    }


}
