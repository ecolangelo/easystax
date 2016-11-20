package com.github.ecolangelo.core;


import com.github.ecolangelo.core.pojo.XmlPath;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.github.ecolangelo.StaxParser.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class StaxXmlParserIntegrationTest {

    @Test
    public void testStreaming() throws Exception {
        InputStream is = this.getClass().getResourceAsStream("/books.xml");

        final List<String> titles = new ArrayList<>();

        from(is).forEach("/bookstore/book/title").stream(new OnMatch() {
            @Override
            public void payload(ParsingResult payload) {

                titles.add(payload.getContent());
            }
        }).parse();

        assertThat(titles.size(), is(5));
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
            public void payload(ParsingResult payload) {
                categories.add(payload.getNode().getAttributes().get("category"));

            }
        }).parse();

        assertThat(categories.size(), is(5));
        assertThat(categories.get(0), is("COOKING"));
        assertThat(categories.get(1), is("CHILDREN"));
        assertThat(categories.get(2), is("WEB"));
        assertThat(categories.get(3), is("SOMETHING"));
    }

    @Test
    public void testOfNewApi() throws Exception {
        InputStream is = this.getClass().getResourceAsStream("/books.xml");

        List<ParsingResult> years = new ArrayList<ParsingResult>();
        List<ParsingResult> prices = new ArrayList<ParsingResult>();
        List<ParsingResult> author = new ArrayList<ParsingResult>();


        from(is).
                forEach("/bookstore/book/year").addTo(years).
                forEach("/bookstore/book/author").addTo(author).
                forEach("/bookstore/book/price").addTo(prices).parse();

        assertThat(years.size(), is(5));
        assertThat(prices.size(), is(5));
        assertThat(author.size(), is(9));
    }

    @Test
    public void testOfNewApiWithAttributes() throws Exception {
        InputStream is = this.getClass().getResourceAsStream("/books.xml");

        List<ParsingResult> author = new ArrayList<ParsingResult>();

        from(is).
                forEach("/bookstore/book[category=WEB]/author").addTo(author).
                parse();


        assertThat(author.size(), is(5));
        assertThat(author.get(0).getContent(), is("<author>James McGovern</author>"));
    }



    @Test
    public void testOfNewApiWithAttributes1() throws Exception {
        InputStream is = this.getClass().getResourceAsStream("/books.xml");

        List<ParsingResult> author = new ArrayList<ParsingResult>();

        from(is).
                forEach("/bookstore/book[id=1]/author").addTo(author).
                parse();


        assertThat(author.size(), is(1));
        assertThat(author.get(0).getContent(), is("<author>Erik T. Ray</author>"));
    }

    @Test
    public void testMultipleUseOfApi() throws Exception {
        InputStream is = this.getClass().getResourceAsStream("/books1.xml");

        String xmlBody = IOUtils.toString(is);

        List<ParsingResult>  authorList = new ArrayList<ParsingResult>();
        List<ParsingResult> priceListOfChildrenBook = new ArrayList<ParsingResult>();
        from(xmlBody)
                .forEach("/bookstore/book/author").addTo(authorList)
                .forEach("/bookstore/book[category=CHILDREN]/price").addTo(priceListOfChildrenBook).parse();

        assertThat(authorList.size(), is(2));
        assertThat(priceListOfChildrenBook.size(), is(1));

        assertThat(authorList.get(0).getContent(), is("<author>Giada De <br/> Laurentiis</author>"));
        assertThat(authorList.get(0).getText(), is("Giada De  Laurentiis"));

        assertThat(priceListOfChildrenBook.get(0).getText(), is("29.99"));


    }


    @Test
    public void testPojoAPI() throws Exception {
        InputStream is = this.getClass().getResourceAsStream("/books1.xml");
        from(is).bindWith(TestPojo.class).stream(new Action<TestPojo>() {
            @Override
            public void execute(TestPojo o) throws ParseException {
                System.out.println("bao!: "+o.getAuthor());
            }
        }).parse();

    }

    class TestPojo {

        

        @XmlPath("/bookstore/book/author")
        private String author;

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }
    }

}