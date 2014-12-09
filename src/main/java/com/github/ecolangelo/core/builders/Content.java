package com.github.ecolangelo.core.builders;

import com.github.ecolangelo.core.Action;
import com.github.ecolangelo.core.handlers.IContentHandler;


public interface Content {

    IContentHandler asText();

    IContentHandler asXml();

    IContentHandler stream(Action<String> resultHandler);

}
