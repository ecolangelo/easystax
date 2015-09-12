package com.github.ecolangelo.core;

import java.util.HashMap;
import java.util.Map;

/**
 * rapresentation of an xml Node
 */
public class Node {

    public Node(String name) {
        this.name = name;
        this.attributes = new HashMap<String, String>();
    }

    public Node(Node parent, Map<String, String> attributes, String name) {
        this.parent = parent;
        this.attributes = attributes;
        this.name = name;
    }

    public Node(Map<String, String> attributes, String name) {
        this.attributes = attributes;
        this.name = name;
    }

    private Node parent;

    private Map<String,String> attributes;

    private String name;

    public Node getParent() {
        return parent;
    }

    public static Node createNodeFromXpath(String path) {
        return null;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static Node root(String name){
        return new Node(null,new HashMap<String,String>(), name);
    }

    public static Node root(String name, Map<String,String> attributes){
        return new Node(null, attributes, name);
    }

    public Node append(Node node) {
        node.setParent(this);
        return node;
    }

    public String getXPath(){
        StringBuilder builder = new StringBuilder("/");
        Node current = this;

        while(current != null){
            builder.insert(0,current.toString()).insert(0,"/");
            current = current.getParent();
        }

        return builder.toString();
    }

    public String toString() {
        return name+""+(attributes.size()>0?attributes.toString().replaceAll("\\{","[").replaceAll("\\}","]"):"");
    }


}
