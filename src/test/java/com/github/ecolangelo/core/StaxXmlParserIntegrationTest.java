package com.github.ecolangelo.core;


import com.github.ecolangelo.StaxParser;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.github.ecolangelo.StaxParser.*;
import static com.github.ecolangelo.core.handlers.SubXmlExtractorHandler.handler;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class StaxXmlParserIntegrationTest {


    @Test
    public void parseBookStorage() throws Exception {
        InputStream is = this.getClass().getResourceAsStream("/books.xml");

        Map<String, String> parseResult = from(is).with(woodstockInputFactory()).
                path("/bookstore/book/title", xml("booksXml")).
                path("/bookstore/book/title",text("books")).parse();

        assertThat(parseResult.get("booksXml"), is("<title lang=\"en\">Everyday Italian</title><title lang=\"en\">Harry Potter</title><title lang=\"en\">XQuery Kick Start</title><title lang=\"en\">Learning XML</title>"));
        assertThat(parseResult.get("books"), is("Everyday Italian\n" +
                "Harry Potter\n" +
                "XQuery Kick Start\n" +
                "Learning XML"));
    }

    @Test
    public void parseASubXmlAsText() throws Exception {
        String xml = IOUtils.toString(this.getClass().getResourceAsStream("/books.xml"));
        Map<String, String> map = from(xml).with(woodstockInputFactory()).path("/bookstore/book/",text("booksXml")).parse();
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


        String ADDRESS_STREET = "addressStreet";
        String ADDRESS_IN_XML = "fullXmlAddress";
        String COMPANY_NAME = "nameOfTheCompany";
        String CITY_COMPANY_IN_XML = "cityOfTheCompany";
        String INFO = "info";

        Map<String,String> result = from(xml).with(woodstockInputFactory()).
                path("/root/person/address/street", text(ADDRESS_STREET)).  //
                path("/root/person/address", xml(ADDRESS_IN_XML)).
                path("/root/info/company/name", text(COMPANY_NAME)).
                path("/root/info/company/address/city", xml(CITY_COMPANY_IN_XML)).
                path("/root/info/", xml(INFO)).parse();


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

        Map<String, String> parse = from(xml).with(woodstockInputFactory()).path("person",xml("/root/person")).parse();
        String out = parse.get("person");
        assertThat(out, is("<person><name>Mario</name><surname>Zarantonello</surname></person><person><name>Rosa</name><surname>Spina</surname></person>"));
    }


    @Test
    public void parseXml2() throws Exception {

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
                        path(("/root/person"), xml(person)).
                        path(("/root/person/name"), text(name)).
                        parse();

        assertThat(parse.get(person), is("<person><name>Mario</name><surname>Zarantonello</surname></person><person><name>Rosario</name><surname>Spina</surname></person>"));
        assertThat(parse.get(name), is("Mario\nRosario"));
    }

    @Test
    public void testStreaming() throws Exception {
        InputStream is = this.getClass().getResourceAsStream("/books.xml");

        final List<String> titles = new ArrayList<String>();

        from(is).with(woodstockInputFactory()).forEach("/bookstore/book/title", xml("titles") , new DummyClosure<String>() {
            @Override
            public void cl(String s) throws Exception {

                titles.add(s);
            }
        }).parse();

        assertThat(titles.size(), is(4));
        assertThat(titles.get(0), is("<title lang=\"en\">Everyday Italian</title>"));
        assertThat(titles.get(1), is("<title lang=\"en\">Harry Potter</title>"));
        assertThat(titles.get(2), is("<title lang=\"en\">XQuery Kick Start</title>"));
        assertThat(titles.get(3), is("<title lang=\"en\">Learning XML</title>"));
    }



}