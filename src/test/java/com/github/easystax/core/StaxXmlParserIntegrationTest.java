package com.github.easystax.core;


import com.github.easystax.StaxParser;
import com.github.easystax.XmlParser;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import com.github.easystax.core.listeners.ContentHandler;
import com.github.easystax.core.listeners.ContentHandlerBuilder;

import java.nio.charset.Charset;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;

public class StaxXmlParserIntegrationTest {

    @Test
    public void parseRightXml() throws Exception {
        String inputXml = IOUtils.toString(this.getClass().getResourceAsStream("/inputTest.xml"));

        XmlParser parser = new StaxParser();

        ContentHandler contentHandler = ContentHandlerBuilder.build("family").dot("publications").get();
        ContentHandler numberHandler = ContentHandlerBuilder.build("family").dot("publications").dot("ExchCpcPublication").dot("number").get();
        ContentHandler countryHandler = ContentHandlerBuilder.build("family").dot("publications").dot("ExchCpcPublication").dot("country").get();

        parser.addListener(contentHandler);
        parser.addListener(numberHandler);
        parser.addListener(numberHandler);
        parser.addListener(countryHandler);

        parser.parse(inputXml, Charset.defaultCharset());

        assertThat(contentHandler.getOut(), containsString("<sequence>1</sequence>"));
        assertThat(numberHandler.getOut().trim(), is("MO20130060"));
        assertThat(countryHandler.getOut(), is("IT"));
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