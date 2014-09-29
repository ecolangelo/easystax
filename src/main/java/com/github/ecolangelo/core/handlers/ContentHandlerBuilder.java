package com.github.ecolangelo.core.handlers;

import com.github.ecolangelo.core.builders.FluentBuilder;

/**
 * Created by eros on 28/09/14.
 */
public class ContentHandlerBuilder implements FluentBuilder<ContentHandler> {

    protected String id;

    protected StringBuilder builder;

    @Override
    public ContentHandler get() {
        return new EnclosingTextExtractorHandler(id, builder.toString());
    }

    public ContentHandler withId(String id){
        this.id = id;
        return get();
    }

    public static DotNotationContentHandlerBuilder root(String rootTag){
        return new DotNotationContentHandlerBuilder(new StringBuilder("/"+rootTag+"/"));
    }

    public static XmlPathContentHandlerBuilder path(String xmlPath){
        if(!xmlPath.endsWith("/"))xmlPath = xmlPath+"/";
        return new XmlPathContentHandlerBuilder(new StringBuilder(xmlPath));
    }

}
