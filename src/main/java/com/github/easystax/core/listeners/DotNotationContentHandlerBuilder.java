package com.github.easystax.core.listeners;

/**
 * Created by eros on 14/09/14.
 */
public class DotNotationContentHandlerBuilder extends ContentHandlerBuilder {

    protected DotNotationContentHandlerBuilder(StringBuilder builder) {
        this.builder = builder;
    }

    protected DotNotationContentHandlerBuilder(String id, StringBuilder builder) {
        this.builder = builder;
    }

    public DotNotationContentHandlerBuilder dot(String tag){
        builder.append(tag).append("/");
        return new DotNotationContentHandlerBuilder(id, builder);
    }
}
