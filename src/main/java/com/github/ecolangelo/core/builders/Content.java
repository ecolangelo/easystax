package com.github.ecolangelo.core.builders;

import com.github.ecolangelo.core.Action;
import com.github.ecolangelo.core.OnMatch;
import com.github.ecolangelo.core.OnXmlSubPart;
import com.github.ecolangelo.core.handlers.IContentHandler;


public interface Content {

    IContentHandler stream(OnXmlSubPart resultHandler);

    IContentHandler stream(OnMatch resultHandler);

}
