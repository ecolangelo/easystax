package com.github.ecolangelo.core;

/**
 * Created by eros on 27/12/14.
 */
public abstract class OnMatch implements Action<Payload> {

    @Override
    public void execute(Payload payload) throws Exception {
        payload(payload);
    }

    public abstract void payload(Payload payload);
}
