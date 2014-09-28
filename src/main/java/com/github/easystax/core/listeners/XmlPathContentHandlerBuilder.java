package com.github.easystax.core.listeners;

import com.github.easystax.core.builders.FluentBuilder;

/**
 * Created by eros on 28/09/14.
 */
public class XmlPathContentHandlerBuilder extends ContentHandlerBuilder{

    public XmlPathContentHandlerBuilder(String id, StringBuilder stringBuilder) {
        this.builder = stringBuilder;
        this.id = id;
    }

    public XmlPathContentHandlerBuilder( StringBuilder stringBuilder) {
        this.builder = stringBuilder;
    }
}
