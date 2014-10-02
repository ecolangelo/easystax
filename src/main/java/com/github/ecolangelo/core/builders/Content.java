package com.github.ecolangelo.core.builders;

import com.github.ecolangelo.core.handlers.IContentHandler;

/**
 * Created by eros on 30/09/14.
 */
public interface Content {

    IContentHandler asText();

    IContentHandler asXml();

    IContentHandler attribute(String name);
}
