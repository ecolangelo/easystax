package com.github.ecolangelo.core;


import com.github.ecolangelo.StaxParser;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.Map;

import static com.github.ecolangelo.core.handlers.SubXmlExtractorHandler.handler;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;

public class StaxXmlParserIntegrationTest {

    @Test
    public void exampleUsageWithDotNotation() throws Exception {
        String inputXml = IOUtils.toString(this.getClass().getResourceAsStream("/inputTest.xml"));

        StaxParser parser = new StaxParser();

        String PUBLICATION_REQUEST = "publications";
        String NUMBER_REQUEST = "number";
        String COUNTRY_REQUEST = "country";

        parser.registerHandlers(
                handler(PUBLICATION_REQUEST).path("/family/publications/").subXml(),
                handler(NUMBER_REQUEST).path("/family/publications/ExchCpcPublication/number").text(),
                handler(COUNTRY_REQUEST).path("/family/publications/ExchCpcPublication/country").text()
        );



        Map<String,String> result = parser.parse(inputXml, Charset.defaultCharset());

        assertThat(result.get(PUBLICATION_REQUEST), containsString("<sequence>1</sequence>"));
//        assertThat(result.get(NUMBER_REQUEST), CoreMatchers.containsString("MO20130060"));
    }
     /*
    @Test
    public void parseBookStorage() throws Exception {
        String xml = IOUtils.toString(this.getClass().getResourceAsStream("/books.xml"));
        StaxParser parser = new StaxParser();
        parser.registerHandlers(path("/bookstore/book/title").withId("books"));
        Map<String, String> map = parser.parse(xml, Charset.defaultCharset());
        assertThat(map.get("books"), CoreMatchers.containsString("<title lang=\"en\">Everyday Italian</title>"));
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

        parser.registerHandlers(
                path("/root/person/address/street").withId("addressStreet"),
                path("/root/person/address").withId("fullXmlAddress"),
                path("/root/info/company/name").withId("nameOfTheCompany"),
                path("/root/info/company/address/city").withId("cityOfTheCompany"),
                path("/root/info/").withId("info")
        );



        Map<String,String> result = parser.parse(xml, Charset.defaultCharset());

        assertThat(result.get("addressStreet"), is("<street>Kalvermarkt</street>"));

        assertThat(result.get("fullXmlAddress"), is("<address><street>Kalvermarkt</street>" +
                "<number>25</number>" +
                "<postCode>2511</postCode>" +
                "<city>Den Haag</city></address>"));

        assertThat(result.get("nameOfTheCompany"), is("<name type=\"standard\">E &amp; Y</name>"));

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


        parser.registerHandlers(path("/root/person").withId("person"));

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
    }  */
}