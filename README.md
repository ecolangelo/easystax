easystax
========

library to deal with StAX API for xml parsing. The library uses Woodstock implementation for XMLStreamReader and XMLOutputWriter

### import with maven
<pre>
<code>
&lt;dependency&gt;
  &lt;groupId&gt;com.github.ecolangelo&lt;/groupId&gt;
  &lt;artifactId&gt;easystax&lt;/artifactId&gt;
  &lt;version&gt;0.0.2&lt;/version&gt;
&lt;/dependency&gt;

</code>
</pre>
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



        Map&lt;String,String&gt; result = parser.parse(xml, Charset.defaultCharset());

        result.get(ADDRESS_STREET) will output:
        Kalvermarkt

        result.get(ADDRESS_IN_XML) will output:
        &lt;address&gt;&lt;street&gt;Kalvermarkt&lt;/street&gt;&lt;number&gt;25&lt;/number&gt;&lt;postCode&gt;2511&lt;/postCode&gt;&lt;city&gt;Den Haag&lt;/city&gt;&lt;/address&gt;

        result.get(COMPANY_NAME) will output:
        E & Y

        result.get(CITY_COMPANY_IN_XML) will output:
        &lt;city&gt;Den Haag&lt;/city&gt;

</code>
</pre>
        
        
#### Licence:
   
   
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
        
        
        
