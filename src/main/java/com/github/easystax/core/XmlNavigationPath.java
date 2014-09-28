package com.github.easystax.core;

import java.util.EmptyStackException;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

/**
 * Created by eros on 21/09/14.
 */
public class XmlNavigationPath {

    private Stack<String> xmlPath = new Stack<String>();
    private Set<String> openTags = new HashSet<String>();

    private String parent;
    private String current;


    public void pushTag(String tagName){
        parent = current;
        current = tagName;
        openTags.add(current);
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

    public Stack<String> getXmlPath() {
        return xmlPath;
    }

    public Set<String> getOpenTags() {
        return openTags;
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
