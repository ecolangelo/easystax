easystax
========

library to deal with StAX API for xml parsing. The library uses Woodstox implementation for XMLStreamReader and XMLOutputWriter

### import with maven
<pre>
<code>
&lt;dependency&gt;
  &lt;groupId&gt;com.github.ecolangelo&lt;/groupId&gt;
  &lt;artifactId&gt;easystax&lt;/artifactId&gt;
  &lt;version&gt;0.0.3&lt;/version&gt;
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

from(is).forEach("/bookstore/book/title" , new OnXmlSubPart() {
            @Override
            public void payload(String payload){
                titles.add(payload);
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
        
        
        
