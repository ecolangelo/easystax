package com.github.ecolangelo.core.handlers;

import org.junit.Test;

import java.io.StringWriter;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class EnclosingTextExtractorHandlerTest {

    @Test
    public void removeStartAndEndTagWhenOutputting() throws Exception {
        EnclosingTextExtractorHandler handler = new EnclosingTextExtractorHandler("","/person/address/");
        StringWriter w = new StringWriter();
        w.append("<address><street>some street</street></address>");
        handler.w = w;
        assertThat(handler.getOut(), is("<street>some street</street>"));
    }


    @Test
    public void removeStartAndEndTagWhenOutputtingWhenAttributesContainsAttributes() throws Exception {
        EnclosingTextExtractorHandler handler = new EnclosingTextExtractorHandler("","/person/address/street");
        StringWriter w = new StringWriter();
        w.append("<street type=\"test\">some street</street>");
        handler.w = w;
        assertThat(handler.getOut(), is("some street"));
    }
}