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

### example: parsing huge xml

input: 

<pre>
<code>
&lt;registry&gt;

        &lt;person&gt;
            &lt;name&gt;Mario&lt;/name&gt;
            &lt;surname&gt;Zarantonello&lt;/surname&gt;
            &lt;address&gt;
                    &lt;street&gt;Spui&lt;/street&gt;
                    &lt;number&gt;75&lt;/number&gt;
                    &lt;city&gt;Amsterdam&lt;/city&gt;
                    &lt;code&gt;92123&lt;/code&gt;
            &lt;/address&gt;
        &lt;/person&gt;
    
        &lt;person&gt;
            &lt;name&gt;Harry&lt;/name&gt;
            &lt;surname&gt;Potter&lt;/surname&gt;
            &lt;address&gt;
                &lt;street&gt;Lincoln&lt;/street&gt;
                &lt;number&gt;25&lt;/number&gt;
                &lt;city&gt;London&lt;/city&gt;
                &lt;code&gt;12345&lt;/code&gt;
            &lt;/address&gt;
        &lt;/person&gt; 
                .
                .
                .
                .
                .
                .
                .
                .
&lt;/registry&gt;
</code>
</pre>

<pre>
<code>
InputStream is = (...) //getting input stream from source

from(is).with(woodstockInputFactory()).forEach("/registry/person/address", xml("titles") , new DummyClosure<String>() {
            @Override
            public void cl(String s) throws Exception {
                System.out.println(s);
            }
        }).parse();

</pre>
</code>

this will print out the sub xml containing the adress of each person:

<pre>
<code>
&lt;address&gt;
    &lt;street&gt;Spui&lt;/street&gt;
    &lt;number&gt;75&lt;/number&gt;
    &lt;city&gt;Amsterdam&lt;/city&gt;
    &lt;code&gt;92123&lt;/code&gt;
&lt;/address&gt;

&lt;address&gt;
    &lt;street&gt;Lincoln&lt;/street&gt;
    &lt;number&gt;25&lt;/number&gt;
    &lt;city&gt;London&lt;/city&gt;
    &lt;code&gt;12345&lt;/code&gt;
&lt;/address&gt;
      .
      .
      .
</pre>
</code>

### example: extracting multiple information from xml

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



Map&lt;String,String&gt; result = from(xml).with(woodstockInputFactory()).
                                                  path("/root/person/address/street", text(ADDRESS_STREET)).  
                                                  path("/root/person/address", xml(ADDRESS_IN_XML)).
                                                  path("/root/info/company/name", text(COMPANY_NAME)).
                                                  path("/root/info/company/address/city", xml(CITY_COMPANY_IN_XML)).
                                                  path("/root/info/", xml(INFO)).parse();

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
        
        
        
