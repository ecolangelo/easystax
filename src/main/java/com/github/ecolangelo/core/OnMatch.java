package com.github.ecolangelo.core;

import java.util.function.Consumer;

/**
 * Created by eros on 27/12/14.
 */
public abstract class OnMatch implements Consumer<ParsingResult> {

    @Override
    public void accept(ParsingResult payload) {
        payload(payload);
    }

    public abstract void payload(ParsingResult payload);
}
