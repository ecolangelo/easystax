package com.github.ecolangelo.core.builders;

import com.github.ecolangelo.core.DummyClosure;
import com.github.ecolangelo.core.handlers.IContentHandler;


public interface Content {

    IContentHandler asText();

    IContentHandler asXml();

    IContentHandler stream(DummyClosure<String> resultHandler);

}
