package com.github.ecolangelo.core.handlers;

import org.junit.Test;

import java.io.StringWriter;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class EnclosingTextExtractorHandlerTest {

    @Test
    public void removeStartAndEndTagWhenOutputting() throws Exception {
        SubXmlExtractorHandler handler = new SubXmlExtractorHandler("","/person/address/");
        StringWriter w = new StringWriter();
        w.append("<address><street>some street</street></address>");
        handler.w = w;
        assertThat(handler.getOut(), is("<address><street>some street</street></address>"));
    }


    @Test
    public void removeStartAndEndTagWhenOutputtingWhenAttributesContainsAttributes() throws Exception {
        SubXmlExtractorHandler handler = new SubXmlExtractorHandler("","/person/address/street");
        StringWriter w = new StringWriter();
        w.append("<street type=\"test\">some street</street>");
        handler.w = w;
        assertThat(handler.getOut(), is("<street type=\"test\">some street</street>"));
    }
}