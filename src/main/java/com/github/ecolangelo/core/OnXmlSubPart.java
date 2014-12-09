package com.github.ecolangelo.core;

/**
 * Created by eros on 09/12/14.
 */
public abstract  class OnXmlSubPart implements Action<String> {

    @Override
    public void execute(String s) throws Exception {
        payload(s);
    }

    public abstract void payload(String payload);

}
