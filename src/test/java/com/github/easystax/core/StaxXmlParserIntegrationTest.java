package com.github.easystax.core;


import com.github.easystax.StaxParser;
import com.github.easystax.XmlParser;
import com.github.easystax.core.listeners.ContentHandlerBuilder;
import com.github.easystax.core.listeners.DotNotationContentHandlerBuilder;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import com.github.easystax.core.listeners.ContentHandler;

import java.nio.charset.Charset;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;

public class StaxXmlParserIntegrationTest {

    @Test
    public void parseRightXml() throws Exception {
        String inputXml = IOUtils.toString(this.getClass().getResourceAsStream("/inputTest.xml"));

        XmlParser parser = new StaxParser();

        String PUBLICATION_REQUEST = "publications";
        String NUMBER_REQUEST = "number";
        String COUNTRY_REQUEST = "country";
        ContentHandler contentHandler = ContentHandlerBuilder.root("family").dot("publications").withId(PUBLICATION_REQUEST);
        ContentHandler numberHandler = ContentHandlerBuilder.root("family").dot("publications").dot("ExchCpcPublication").dot(NUMBER_REQUEST).withId("number");
        ContentHandler countryHandler = ContentHandlerBuilder.root("family").dot("publications").dot("ExchCpcPublication").dot(COUNTRY_REQUEST).withId("country");

        parser.addListener(contentHandler);
        parser.addListener(numberHandler);
        parser.addListener(numberHandler);
        parser.addListener(countryHandler);

        Map<String,String> result = parser.parse(inputXml, Charset.defaultCharset());

        assertThat(result.get(PUBLICATION_REQUEST), containsString("<sequence>1</sequence>"));
        assertThat(result.get(NUMBER_REQUEST).trim(), is("MO20130060"));
        assertThat(result.get(COUNTRY_REQUEST), is("IT"));
    }



    @Test
    public void parseXml() throws Exception {
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

        XmlParser parser = new StaxParser();



        parser.parse(inputXml, Charset.defaultCharset());
    }
}