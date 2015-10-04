package com.github.ecolangelo.core;

public class ParsingResult {

    private Node node;

    private String content;

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public String getContent() {
        return content;
    }

    public String getText() {
        return content.replaceAll("<[^<>]+>","");
    }

    public void setContent(String content) {
        this.content = content;
    }
}
