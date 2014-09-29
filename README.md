easystax
========

library to deal with StAX API for xml parsing. The library use Woodstock as provider for XMLInputFactory and XMLOutputFactory


### example of usage

<pre>
<code>
String xml = "&lt;root&gt;" 
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
                                "&lt;name type=\"standard\"&gt;E &amp;amp; Y&lt;/name&gt;" +
                                "&lt;address&gt;" +
                                    "&lt;street&gt;Spui&lt;/street&gt;" +
                                    "&lt;number&gt;26&lt;/number&gt;" +
                                    "&lt;postCode&gt;2611&lt;/postCode&gt;" +
                                    "&lt;city&gt;Den Haag&lt;/city&gt;" +
                                "&lt;/address&gt;" +
                            "&lt;/company&gt;" +
                        "&lt;/info&gt;" +
                    "&lt;/root&gt;";


        StaxParser parser = new StaxParser();

        parser.registerHandlers(
                path("/root/person/address/street").withId("addressStreet"),
                path("/root/person/address").withId("fullXmlAddress"),
                path("/root/info/company/name").withId("nameOfTheCompany"),
                path("/root/info/company/address/city").withId("cityOfTheCompany"),
                path("/root/info/").withId("info")
        );



        Map&lt;String,String&gt; result = parser.parse(xml, Charset.defaultCharset());

        assertThat(result.get("addressStreet"), is("Kalvermarkt"));
        
        
        assertThat(result.get("fullXmlAddress"), is(
                "&lt;street&gt;Kalvermarkt&lt;/street&gt;" +
                "&lt;number&gt;25&lt;/number&gt;" +
                "&lt;postCode&gt;2511&lt;/postCode&gt;" +
                "&lt;city&gt;Den Haag&lt;/city&gt;"
                ));
        
        assertThat(result.get("nameOfTheCompany"), is("E &amp;amp; Y"));
        
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

</code>
</pre>
        
        
## Licence:
   
   
   Copyright [2014] [Colangelo Eros]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.    
        
        
        
