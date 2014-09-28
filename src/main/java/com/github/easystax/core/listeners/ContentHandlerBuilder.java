package com.github.easystax.core.listeners;

import com.github.easystax.core.builders.FluentBuilder;

/**
 * Created by eros on 14/09/14.
 */
public class ContentHandlerBuilder implements FluentBuilder<ContentHandler> {

    private StringBuilder builder;

    private ContentHandlerBuilder(StringBuilder builder) {
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
