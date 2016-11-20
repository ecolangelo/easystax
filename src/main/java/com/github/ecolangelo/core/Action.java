package com.github.ecolangelo.core;

/**
 * Created by eros on 27/09/14.
 */
public interface Action<T> {

    void execute(T t) throws ParseException;
}
