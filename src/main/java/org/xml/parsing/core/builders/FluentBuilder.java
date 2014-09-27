package org.xml.parsing.core.builders;

import org.xml.parsing.core.StaxPath;
import org.xml.parsing.core.listeners.ContentHandler;

/**
 * Created by eros on 14/09/14.
 */
public interface FluentBuilder<T> {

    public T get();

}
