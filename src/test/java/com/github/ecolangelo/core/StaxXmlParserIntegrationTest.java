package com.github.ecolangelo.core;


import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.github.ecolangelo.StaxParser.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class StaxXmlParserIntegrationTest {

    @Test
    public void testStreaming() throws Exception {
        InputStream is = this.getClass().getResourceAsStream("/books.xml");

        final List<String> titles = new ArrayList<String>();

//        from(is).forEach("/bookstore/book/title" , new OnXmlSubPart() {
//            @Override
//            public void payload(String payload){
//                titles.add(payload);
//            }
//        }).parse();

        assertThat(titles.size(), is(4));
        assertThat(titles.get(0), is("<title lang=\"en\">Everyday Italian</title>"));
        assertThat(titles.get(1), is("<title lang=\"en\">Harry Potter</title>"));
        assertThat(titles.get(2), is("<title lang=\"en\">XQuery Kick Start</title>"));
        assertThat(titles.get(3), is("<title lang=\"en\">Learning XML</title>"));
    }


    @Test
    public void payloadAPI() throws Exception {
        InputStream is = this.getClass().getResourceAsStream("/books.xml");

        final List<String> titles = new ArrayList<String>();
        final List<String> titleLanguages = new ArrayList<String>();

//        from(is).forEach("/bookstore/book/title" , new OnMatch() {
//            @Override
//            public void payload(Payload payload){
//                titles.add(payload.getXml());
//                titleLanguages.add(payload.getAttributes().get("lang"));
//
//            }
//        }).parse();

        assertThat(titles.size(), is(4));
        assertThat(titles.get(0), is("<title lang=\"en\">Everyday Italian</title>"));
        assertThat(titles.get(1), is("<title lang=\"en\">Harry Potter</title>"));
        assertThat(titles.get(2), is("<title lang=\"en\">XQuery Kick Start</title>"));
        assertThat(titles.get(3), is("<title lang=\"en\">Learning XML</title>"));
        assertThat(titleLanguages.size(), is(4));
        assertThat(titleLanguages.get(0), is("en"));
    }

    @Test
    public void testPayloadApiGettingAttributes() throws Exception {
        InputStream is = this.getClass().getResourceAsStream("/books.xml");

        final List<String> categories = new ArrayList<String>();


//        from(is).forEach("/bookstore/book" , new OnMatch() {
//            @Override
//            public void payload(Payload payload){
//                categories.add(payload.getAttributes().get("category"));
//
//            }
//        }).parse();

        assertThat(categories.size(), is(4));
        assertThat(categories.get(0), is("COOKING"));
        assertThat(categories.get(1), is("CHILDREN"));
        assertThat(categories.get(2), is("WEB"));
        assertThat(categories.get(3), is("WEB"));
    }



}