package com.github.ecolangelo.core;

import com.ctc.wstx.stax.WstxInputFactory;
import com.ctc.wstx.stax.WstxOutputFactory;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;

/**
 * Created by eros on 28/09/14.
 */
public class WoodstockInputFactory {

    private WoodstockInputFactory() {

    }

    private static class WstxInputFactoryHolder {
        public static final WstxInputFactory INSTANCE = new WstxInputFactory();
    }

    @SuppressWarnings("SameReturnValue")
    public static XMLInputFactory getInputFactory() {
        return WstxInputFactoryHolder.INSTANCE;
    }

    private static class WstxOutputFactoryHolder {
        public static final WstxOutputFactory INSTANCE = new WstxOutputFactory();
    }

    @SuppressWarnings("SameReturnValue")
    public static XMLOutputFactory getOutputFactory() {
        return WstxOutputFactoryHolder.INSTANCE;
    }
}
