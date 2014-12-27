package com.github.ecolangelo.core;

import java.util.Map;

/**
 * Created by eros on 09/12/14.
 */
public abstract  class OnXmlSubPart implements Action<String> {

    public String text;

    public String text() {
        return text;
    }

    @Override
    public void execute(String s) throws Exception {
        payload(s);
    }

    public abstract void payload(String payload);

}
