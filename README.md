easystax
========

utility library that wraps StAX API for xml parsing. The library uses Woodstox implementation for XMLInputFactory and XMLOutputFactory

Note: no validation is currently implemented for path, example of valid path:
/root/child
/root[attribute1=value1,attribute2=valu2...]/child

### import with maven
<pre>
<code>
&lt;dependency&gt;
  &lt;groupId&gt;com.github.ecolangelo&lt;/groupId&gt;
  &lt;artifactId&gt;easystax&lt;/artifactId&gt;
  &lt;version&gt;0.0.6&lt;/version&gt;
&lt;/dependency&gt;

</code>
</pre>

### example: parsing per node name and attributes

<pre>
<code>
String xmlBody = "&lt;bookstore&gt;
    &lt;book category="COOKING"&gt;
         &lt;title lang="en"&gt;Everyday Italian&lt;/title&gt;
         &lt;author&gt;Giada De &lt;br/&gt; Laurentiis&lt;/author&gt;
         &lt;year&gt;2005&lt;/year&gt;
         &lt;price&gt;30.00&lt;/price&gt;
    &lt;/book&gt;

    &lt;book category="CHILDREN"&gt;
          &lt;title lang="en"&gt;Harry Potter&lt;/title&gt;
          &lt;author&gt;J K. Rowling&lt;/author&gt;
          &lt;year&gt;2005&lt;/year&gt;
          &lt;price&gt;24.00&lt;/price&gt;
    &lt;/book&gt;
&lt;/bookstore&gt;"


List<ParsingResult>  authorList = new ArrayList<>();
List<ParsingResult> priceListOfChildrenBook = new ArrayList<>();

from(xmlBody)
    .forEach("/bookstore/book/author").addTo(authorList)
    .forEach("/bookstore/book[category=CHILDREN]/price").addTo(priceListOfChildrenBook)
    .parse();

assertThat(authorList.size(), is(2));
assertThat(priceListOfChildrenBook.size(), is(1));

assertThat(authorList.get(0).getContent(), is("<author>Giada De <br/> Laurentiis</author>"));
assertThat(authorList.get(0).getText(), is("Giada De  Laurentiis"));

assertThat(priceListOfChildrenBook.get(0).getText(), is("29.99"));

</code>

</pre>

### example: streaming xml

input (generally a huge xml):

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
            &lt;name&gt;Marc&lt;/name&gt;
            &lt;surname&gt;Wright&lt;/surname&gt;
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

from(is).forEach("/bookstore/book/address" ).stream(new OnMatch() {
            @Override
            public void payload(ParsingResult payload){
                System.out.println(payload.getContent());
            }
        }).parse();

</pre>
</code>

this will print out all the sub xml that match the provided path /bookstore/book/address:

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


ParsingResult:

- node: contains attributes, name of the node and parent node
- content: enclosing xml with tags
- text: enclosing text stripped out of the tags

</code>

        
#### Licence:
   
   
   Copyright [2015] [Colangelo Eros]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.    
        
        
        
