package com.github.ecolangelo.core;

import org.junit.Test;

import java.util.HashMap;
import java.util.regex.Matcher;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class NodeTest {

    @Test
    public void testGetXPath() throws Exception {
        Node node = new Node(null, new HashMap<String, String>(){{
            put("xml:id","augh");
            put("type","publication");
        }},"root");
        assertThat(node.getXPath(), is("/root[xml:id=augh, type=publication]/"));
    }

    @Test
    public void testRegexForAttributes() throws Exception {
        Matcher attributes = Node.ATTRIBUTE_PATTERN.matcher("[id=1]");
        assertTrue(attributes.find());
        assertThat(attributes.group(1), is("id=1"));
    }

    @Test
    public void testRegexForAttributes1() throws Exception {
        Matcher attributes = Node.ATTRIBUTE_PATTERN.matcher("[id=1,type=publication]");
        assertTrue(attributes.find());
        assertThat(attributes.group(1), is("id=1,type=publication"));
    }

    @Test
    public void testGetNodeFromXPath_noAttributes()  throws Exception{
        String path = "/root/child";
        Node node = Node.createNodeFromXpath(path);
        assertThat(node.getName(),is("child"));
        assertThat(node.getAttributes().size(), is(0));
        assertThat(node.getParent().getName(), is("root"));
        assertThat(node.getParent().getAttributes().size(), is(0));
    }

    @Test
    public void testGetNodeFromXPath_singleAttribute() throws Exception {
        String path = "/root[id=1]/child";
        Node node = Node.createNodeFromXpath(path);
        assertThat(node.getName(),is("child"));
        assertThat(node.getAttributes().size(), is(0));
        assertThat(node.getParent().getName(), is("root"));
        assertThat(node.getParent().getAttributes().size(), is(1));
    }

    @Test
    public void testGetNodeFromXPath_multipleAttributes() throws Exception {
        String path = "/root[id=1,type=SomeType]/child";
        Node node = Node.createNodeFromXpath(path);
        assertThat(node.getName(),is("child"));
        assertThat(node.getAttributes().size(), is(0));
        assertThat(node.getParent().getName(), is("root"));
        assertThat(node.getParent().getAttributes().size(), is(2));
    }


}