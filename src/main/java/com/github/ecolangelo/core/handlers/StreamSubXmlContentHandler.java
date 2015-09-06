package com.github.ecolangelo.core.handlers;

import com.github.ecolangelo.core.Action;
import com.github.ecolangelo.core.OnXmlSubPart;
import com.github.ecolangelo.core.XmlNavigationPath;
import org.codehaus.stax2.XMLStreamReader2;

import javax.xml.stream.XMLStreamException;
import java.io.StringWriter;

/**
 * Created by eros on 04/10/14.
 */
public class StreamSubXmlContentHandler extends SubXmlExtractorHandler {

    OnXmlSubPart contentHandler;

    public StreamSubXmlContentHandler(String id, String path, OnXmlSubPart contentHandler) {
        super(id,path);
        this.contentHandler = contentHandler;
    }

    @Override
    public void endElement(XMLStreamReader2 endElement) throws XMLStreamException {
        copyIfRecordingEnabled(endElement);
        if(path.equals(navigationStack.resolvePath())){
            stopRecording();
            writer2.closeCompletely();
            try {
                contentHandler.execute(w.toString());
                w.close();
                w = new StringWriter();
            } catch (Exception e) {

            }
        }
        navigationStack.popTag();

    }


}
