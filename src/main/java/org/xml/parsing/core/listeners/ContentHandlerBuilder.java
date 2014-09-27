package org.xml.parsing.core.listeners;

import org.xml.parsing.core.builders.FluentBuilder;

/**
 * Created by eros on 14/09/14.
 */
public class ContentHandlerBuilder implements FluentBuilder<ContentHandler> {

    private ContentHandler handler;

    private StringBuilder builder;

    ContentHandlerBuilder(ContentHandler handler) {
        this.handler = handler;
    }

    ContentHandlerBuilder(StringBuilder builder) {
        this.builder = builder;
    }


    public static ContentHandlerBuilder build(String rootTag){
        return new ContentHandlerBuilder(new StringBuilder("/"+rootTag+"/"));
    }

    public ContentHandlerBuilder dot(String tag){
        builder.append(tag).append("/");
        return new ContentHandlerBuilder(builder);
    }


    @Override
    public ContentHandler get() {
        return new EnclosingTextExtractorHandler(builder.toString());
    }
}
