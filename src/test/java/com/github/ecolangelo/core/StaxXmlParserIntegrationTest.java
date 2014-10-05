package com.github.ecolangelo.core;


import com.github.ecolangelo.StaxParser;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Map;

import static com.github.ecolangelo.StaxParser.*;
import static com.github.ecolangelo.core.handlers.SubXmlExtractorHandler.handler;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class StaxXmlParserIntegrationTest {


    @Test
    public void parseBookStorage() throws Exception {
        String xml = IOUtils.toString(this.getClass().getResourceAsStream("/books.xml"));
        StaxParser parser = new StaxParser();
        parser.register(
                handler("booksXml").path("/bookstore/book/title").asXml(),
                handler("books").path("/bookstore/book/title").asText()
        );
        Map<String, String> map = parser.parse(xml, Charset.defaultCharset());
        assertThat(map.get("booksXml"), is("<title lang=\"en\">Everyday Italian</title><title lang=\"en\">Harry Potter</title><title lang=\"en\">XQuery Kick Start</title><title lang=\"en\">Learning XML</title>"));
        assertThat(map.get("books"), is("Everyday Italian\n" +
                "Harry Potter\n" +
                "XQuery Kick Start\n" +
                "Learning XML"));
    }

    @Test
    public void parseASubXmlAsText() throws Exception {
        String xml = IOUtils.toString(this.getClass().getResourceAsStream("/books.xml"));
        StaxParser parser = new StaxParser();
        parser.register(
                handler("booksXml").path("/bookstore/book/").asText()
        );
        Map<String, String> map = parser.parse(xml, Charset.defaultCharset());

        assertNotNull(map);
    }

    @Test
    public void exampleOfUsageWithAbsoluteXmlPath() throws Exception {
        String xml = "<root>" +
                "<person>" +
                "<name>Mario</name>" +
                "<surname>Zarantonello</surname>" +
                "<address>" +
                "<street>Kalvermarkt</street>" +
                "<number>25</number>" +
                "<postCode>2511</postCode>" +
                "<city>Den Haag</city>" +
                "</address>"+
                "</person>" +
                "<info>" +
                "<company>" +
                "<name type=\"standard\">E &amp; Y</name>" +
                "<address>" +
                "<street>Spui</street>" +
                "<number>26</number>" +
                "<postCode>2611</postCode>" +
                "<city>Den Haag</city>" +
                "</address>" +
                "</company>" +
                "</info>" +
                "</root>";


        StaxParser parser = new StaxParser();

        String ADDRESS_STREET = "addressStreet";
        String ADDRESS_IN_XML = "fullXmlAddress";
        String COMPANY_NAME = "nameOfTheCompany";
        String CITY_COMPANY_IN_XML = "cityOfTheCompany";
        String INFO = "info";

        parser.register(
                handler(ADDRESS_STREET).path("/root/person/address/street").asText(),
                handler(ADDRESS_IN_XML).path("/root/person/address").asXml(),
                handler(COMPANY_NAME).path("/root/info/company/name").asText(),
                handler(CITY_COMPANY_IN_XML).path("/root/info/company/address/city").asXml(),
                handler(INFO).path("/root/info/").asXml()
        );



        Map<String,String> result = parser.parse(xml, Charset.defaultCharset());

        assertThat(result.get(ADDRESS_STREET), is("Kalvermarkt"));

        assertThat(result.get(ADDRESS_IN_XML), is("<address><street>Kalvermarkt</street>" +
                "<number>25</number>" +
                "<postCode>2511</postCode>" +
                "<city>Den Haag</city></address>"));

        assertThat(result.get(COMPANY_NAME), is("E & Y"));

        assertThat(result.get(CITY_COMPANY_IN_XML), is("<city>Den Haag</city>"));

        assertThat(result.get(INFO), is(
                "<info><company>" +
                        "<name type=\"standard\">E &amp; Y</name>" +
                        "<address>" +
                        "<street>Spui</street>" +
                        "<number>26</number>" +
                        "<postCode>2611</postCode>" +
                        "<city>Den Haag</city>" +
                        "</address>" +
                        "</company></info>"
        ));
    }

    @Test
    public void parseXmlWithMultipleTagWithSameNameAtSameLevel() throws Exception {
        String xml = "<root>" +
                "<person>" +
                "<name>Mario</name>" +
                "<surname>Zarantonello</surname>" +
                "</person>" +
                "<person>" +
                "<name>Rosa</name>" +
                "<surname>Spina</surname>" +
                "</person>"+
                "</root>";



        StaxParser parser = new StaxParser();


        parser.register(handler("person").path("/root/person").asXml());

        Map<String, String> parse = parser.parse(xml, Charset.defaultCharset());
        String out = parse.get("person");
        assertThat(out, is("<person><name>Mario</name><surname>Zarantonello</surname></person><person><name>Rosa</name><surname>Spina</surname></person>"));
    }


    @Test
    public void completeFluent() throws Exception {

        String xml =
                "<root>" +
                    "<person>" +
                        "<name>Mario</name>" +
                        "<surname>Zarantonello</surname>" +
                    "</person>" +
                    "<person>" +
                        "<name>Rosario</name>" +
                        "<surname>Spina</surname>" +
                    "</person>"+
                "</root>";

        String person = "person";
        String name = "name";

        Map<String, String> parse =
                from(xml).
                with(woodstockInputFactory()).
                        path((person), xml("/root/person")).
                        path((name), text("/root/person/name")).
                parse();

        String out = parse.get(person);

        assertThat(out, is("<person><name>Mario</name><surname>Zarantonello</surname></person><person><name>Rosario</name><surname>Spina</surname></person>"));
    }



    @Test
    public void testStreaming() throws Exception {
        InputStream is = this.getClass().getResourceAsStream("/books.xml");
        Map<String,String> titles = from(is).with(woodstockInputFactory()).forEach("titles","/books/book/title", new DummyClosure<String>() {
            @Override
            public void cl(String s) throws Exception {
                System.out.println("title: "+s);
            }
        }).parse();



    }

}