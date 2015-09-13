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

        from(is).forEach("/bookstore/book/title").stream(new OnMatch() {
            @Override
            public void payload(ParsingResult payload) {
                   titles.add(payload.getContent());
            }
        }).parse();

        assertThat(titles.size(), is(4));
        assertThat(titles.get(0), is("<title lang=\"en\">Everyday Italian</title>"));
        assertThat(titles.get(1), is("<title lang=\"en\">Harry Potter</title>"));
        assertThat(titles.get(2), is("<title lang=\"en\">XQuery Kick Start</title>"));
        assertThat(titles.get(3), is("<title lang=\"en\">Learning XML</title>"));
    }

    @Test
    public void testPayloadApiGettingAttributes() throws Exception {
        InputStream is = this.getClass().getResourceAsStream("/books.xml");

        final List<String> categories = new ArrayList<String>();

        from(is).forEach("/bookstore/book").stream(new OnMatch() {
            @Override
            public void payload(ParsingResult payload){
                categories.add(payload.getNode().getAttributes().get("category"));

            }
        }).parse();

        assertThat(categories.size(), is(4));
        assertThat(categories.get(0), is("COOKING"));
        assertThat(categories.get(1), is("CHILDREN"));
        assertThat(categories.get(2), is("WEB"));
        assertThat(categories.get(3), is("WEB"));
    }

    @Test
    public void testOfNewApi() throws Exception {
        InputStream is = this.getClass().getResourceAsStream("/books.xml");

        List<ParsingResult> years = new ArrayList<ParsingResult>();
        List<ParsingResult> prices = new ArrayList<ParsingResult>();
        List<ParsingResult> author = new ArrayList<ParsingResult>();


        from(is).
                forEach("/bookstore/book/year").addResultTo(years).
                forEach("/bookstore/book/author").addResultTo(author).
                forEach("/bookstore/book/price").addResultTo(prices).parse();

        assertThat(years.size(), is(4));
        assertThat(prices.size(), is(4));
        assertThat(author.size(), is(8));
    }

    @Test
    public void testOfNewApiWithAttributes() throws Exception {
        InputStream is = this.getClass().getResourceAsStream("/books.xml");

        List<ParsingResult> author = new ArrayList<ParsingResult>();

        from(is).
                forEach("/bookstore/book[category=WEB]/author").addResultTo(author).
                parse();


        assertThat(author.size(), is(5));
        assertThat(author.get(0).getContent(), is("<author>James McGovern</author>"));
    }

    @Test
    public void testOfNewApiWithAttributes1() throws Exception {
        InputStream is = this.getClass().getResourceAsStream("/books.xml");

        List<ParsingResult> author = new ArrayList<ParsingResult>();

        from(is).
                forEach("/bookstore/book[id=1]/author").addResultTo(author).
                parse();


        assertThat(author.size(), is(5));
        assertThat(author.get(0).getContent(), is("<author>James McGovern</author>"));
    }
}