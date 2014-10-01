package com.github.ecolangelo.core;


import com.github.ecolangelo.StaxParser;
import org.apache.commons.io.IOUtils;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.Map;

import static com.github.ecolangelo.core.handlers.SubXmlExtractorHandler.handler;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class StaxXmlParserIntegrationTest {

    @Test
    public void exampleUsageWithDotNotation() throws Exception {
        String inputXml = IOUtils.toString(this.getClass().getResourceAsStream("/inputTest.xml"));

        StaxParser parser = new StaxParser();

        String PUBLICATION_REQUEST = "publications";
        String NUMBER_REQUEST = "number";
        String COUNTRY_REQUEST = "country";

        parser.register(
                handler(PUBLICATION_REQUEST).path("/family/publications/").subXml(),
                handler(NUMBER_REQUEST).path("/family/publications/ExchCpcPublication/number").text(),
                handler(COUNTRY_REQUEST).path("/family/publications/ExchCpcPublication/country").text()
        );



        Map<String,String> result = parser.parse(inputXml, Charset.defaultCharset());

        assertThat(result.get(PUBLICATION_REQUEST), containsString("<sequence>1</sequence>"));
        assertThat(result.get(NUMBER_REQUEST), containsString("MO20130060"));
        assertThat(result.get(COUNTRY_REQUEST), containsString("IT"));
    }

    @Test
    public void parseBookStorage() throws Exception {
        String xml = IOUtils.toString(this.getClass().getResourceAsStream("/books.xml"));
        StaxParser parser = new StaxParser();
        parser.register(
                handler("booksXml").path("/bookstore/book/title").subXml(),
                handler("books").path("/bookstore/book/title").text()
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
                handler("booksXml").path("/bookstore/book/").text()
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
       handler("asd");
        parser.register(
                handler("addressStreet").path("/root/person/address/street").text(),
                handler("fullXmlAddress").path("/root/person/address").subXml(),
                handler("nameOfTheCompany").path("/root/info/company/name").text(),
                handler("cityOfTheCompany").path("/root/info/company/address/city").subXml(),
                handler("info").path("/root/info/").subXml()
        );



        Map<String,String> result = parser.parse(xml, Charset.defaultCharset());

        assertThat(result.get("addressStreet"), is("Kalvermarkt"));

        assertThat(result.get("fullXmlAddress"), is("<address><street>Kalvermarkt</street>" +
                "<number>25</number>" +
                "<postCode>2511</postCode>" +
                "<city>Den Haag</city></address>"));

        assertThat(result.get("nameOfTheCompany"), is("E & Y"));

        assertThat(result.get("info"), is(
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


        parser.register(handler("person").path("/root/person").subXml());

        Map<String, String> parse = parser.parse(xml, Charset.defaultCharset());
        String out = parse.get("person");
        assertThat(out, is("<person><name>Mario</name><surname>Zarantonello</surname></person><person><name>Rosa</name><surname>Spina</surname></person>"));
    }

    @Test
    public void parseXmlWithoutHandler() throws Exception {
        String inputXml = "<teiCorpus>" +
                "<teiCorpus>" +
                "<teiHeader>" +
                "<sourceDesc>blabla</sourceDesc>" +
                "</teiHeader>" +
                "<TEI>" +
                "<teiHeader>" +
                "<fileDesc>blabla</fileDesc>" +
                "</teiHeader>" +
                "<text>" +
                "<group>" +
                "<text change=\"asdfasdfa\">" +
                "<div type=\"description\">blabla</div>" +
                "</text>" +
                "</group>" +
                "</text>" +
                "</TEI>" +
                "</teiCorpus>" +
                "</teiCorpus>";

        StaxParser parser = new StaxParser();

        Map<String,String> result = parser.parse(inputXml, Charset.defaultCharset());
        assertNotNull(result);
        assertThat(result.size(), is(0));
    }
}