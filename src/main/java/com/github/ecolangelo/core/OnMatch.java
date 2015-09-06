package com.github.ecolangelo.core;

/**
 * Created by eros on 27/12/14.
 */
public abstract class OnMatch implements Action<ParsingResult> {

    @Override
    public void execute(ParsingResult payload) throws Exception {
        payload(payload);
    }

    public abstract void payload(ParsingResult payload);
}
