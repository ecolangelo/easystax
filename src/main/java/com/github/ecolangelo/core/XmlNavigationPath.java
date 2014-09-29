package com.github.ecolangelo.core;

import java.util.EmptyStackException;
import java.util.Stack;

/**
 * Created by eros on 21/09/14.
 */
public class XmlNavigationPath {

    final private Stack<String> xmlPath = new Stack<String>();

    private String parent;
    private String current;

    public void pushTag(String tagName){
        parent = current;
        current = tagName;
        xmlPath.push(current);
    }


    public void popTag(){
        try {
            xmlPath.pop();
            current = xmlPath.peek();
        }catch (EmptyStackException ese){
            current = null;
            parent = null;
            return;
        }

        try {
            xmlPath.pop();
            parent = xmlPath.peek();
        }catch (EmptyStackException ese){
            parent = null;
        }
        xmlPath.push(current);
    }

    public String getParent() {
        return parent;
    }

    public String getCurrent() {
        return current;
    }

    public String resolvePath(){
        StringBuilder builder = new StringBuilder("/");
        for(String s: xmlPath){
            builder.append(s).append("/");
        }
        return builder.toString();
    }

    @Override
    public String toString() {
        return resolvePath();
    }
}
