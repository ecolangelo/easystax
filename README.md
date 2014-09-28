easystax
========

library to deal with StAX API for xml parsing the library use Woodstock as default provider for XMLInputFactory and XMLOutputFactory


### example of usage


String xml = "&lt;root&gt;" +
                        "&lt;person&gt;" +
                            "&lt;name&gt;Mario&lt;/name&gt;" +
                            "&lt;surname&gt;Zarantonello&lt;/surname&gt;" +
                            "&lt;address&gt;" +
                                "&lt;street&gt;Kalvermarkt&lt;/street&gt;" +
                                "&lt;number&gt;25&lt;/number&gt;" +
                                "&lt;postCode&gt;2511&lt;/postCode&gt;" +
                                "&lt;city&gt;Den Haag&lt;/city&gt;" +
                            "&lt;/address&gt;"+
                        "&lt;/person&gt;" +
                        "&lt;info&gt;" +
                            "&lt;company&gt;" +
                                "&lt;name type=\"standard\"&gt;E &amp; Y&lt;/name&gt;" +
                                "&lt;address&gt;" +
                                    "&lt;street&gt;Spui&lt;/street&gt;" +
                                    "&lt;number&gt;26&lt;/number&gt;" +
                                    "&lt;postCode&gt;2611&lt;/postCode&gt;" +
                                    "&lt;city&gt;Den Haag&lt;/city&gt;" +
                                "&lt;/address&gt;" +
                            "&lt;/company&gt;" +
                        "&lt;/info&gt;" +
                    "&lt;/root&gt;";


        XmlParser parser = new StaxParser();

        parser.registerHandlers(
                path("/root/person/address/street").withId("addressStreet"),
                path("/root/person/address").withId("fullXmlAddress"),
                path("/root/info/company/name").withId("nameOfTheCompany"),
                path("/root/info/company/address/city").withId("cityOfTheCompany"),
                path("/root/info/").withId("info")
        );



        Map&lt;String,String&gt; result = parser.parse(xml, Charset.defaultCharset());

        assertThat(result.get("addressStreet"), is("Kalvermarkt"));
        
        assertThat(result.get("fullXmlAddress"), is("&lt;street&gt;Kalvermarkt&lt;/street&gt;" +
                "&lt;number&gt;25&lt;/number&gt;" +
                "&lt;postCode&gt;2511&lt;/postCode&gt;" +
                "&lt;city&gt;Den Haag&lt;/city&gt;"));
        
        assertThat(result.get("nameOfTheCompany"), is("E &amp; Y"));
        
        assertThat(result.get("info"), is(
                "&lt;company&gt;" +
                "&lt;name type=\"standard\"&gt;E &amp;amp; Y&lt;/name&gt;" +
                "&lt;address&gt;" +
                "&lt;street&gt;Spui&lt;/street&gt;" +
                "&lt;number&gt;26&lt;/number&gt;" +
                "&lt;postCode&gt;2611&lt;/postCode&gt;" +
                "&lt;city&gt;Den Haag&lt;/city&gt;" +
                "&lt;/address&gt;" +
                "&lt;/company&gt;"
        ));
        
        
        