package com.github.ecolangelo.core;

import org.junit.Test;

import static org.junit.Assert.*;

public class XPathNodeMatchingStrategyTest {

    @Test
    public void testMatch1() throws Exception {
        // first: /root/child
        // second: /root/child
        Node root = new Node("root");
        Node first = root.append(new Node("child"));

        Node secondroot = new Node("root");
        Node second = secondroot.append(new Node("child"));

        assertTrue(new XPathNodeMatchingStrategy().match(first, second));

    }

    @Test
    public void testGracefulNodeValidation_true_success () throws Exception {
        Node first = new Node("root");
        Node second = new Node("root");
        assertTrue(new XPathNodeMatchingStrategy().nodeMatch(first, second));
    }

    @Test
    public void testGracefulNodeValidation_true1_success () throws Exception {
        Node first = new Node("root");

        Node second = new Node("root");
        second.getAttributes().put("id","1");
        assertTrue(new XPathNodeMatchingStrategy().nodeMatch(first, second));
    }



    @Test
    public void testGracefulNodeValidation_differentSetOfAttributes_success () throws Exception {
        Node first = new Node("root");
        first.getAttributes().put("id","1");
        Node second = new Node("root");
        second.getAttributes().put("id","1");
        second.getAttributes().put("type","application");
        assertTrue(new XPathNodeMatchingStrategy().nodeMatch(first, second));
    }

    @Test
    public void testGracefulNodeValidation_differentSetOfAttributes_DoesNotMatch_success () throws Exception {
        Node first = new Node("root");
        first.getAttributes().put("id","1");
        first.getAttributes().put("type","application");
        Node second = new Node("root");
        second.getAttributes().put("id","1");
        assertFalse(new XPathNodeMatchingStrategy().nodeMatch(first, second));
    }

    @Test
    public void testMatch2() throws Exception {
        // first: /root/child
        // second: /root[id=1]/child
        Node root = new Node("root");
        Node first = root.append(new Node("child"));

        Node secondroot = new Node("root");
        secondroot.getAttributes().put("id","1");
        Node second = secondroot.append(new Node("child"));

        assertTrue("\n" + first.getXPath() + " ---> " + second.getXPath(), new XPathNodeMatchingStrategy().match(first, second));

    }

    @Test
    public void testMatch3() throws Exception {
        // first: /root[id=1]/child
        // second: /root[id=1,category=WEB]/child
        Node root = new Node("root");
        root.getAttributes().put("id","1");
        Node first = root.append(new Node("child"));

        Node secondroot = new Node("root");
        secondroot.getAttributes().put("id","1");
        secondroot.getAttributes().put("category","WEB");
        Node second = secondroot.append(new Node("child"));

        assertTrue("\n" + first.getXPath() + " ---> " + second.getXPath(), new XPathNodeMatchingStrategy().match(first, second));

    }

    @Test
    public void testAttributesMatch_return_true_success() throws Exception {
        Node node1 =new Node("root");
        node1.getAttributes().put("id","1");
        Node node2 = new Node("root");
        node2.getAttributes().put("id","1");

        assertTrue(new XPathNodeMatchingStrategy().attributesMatch(node1, node2));
    }

    @Test
    public void testAttributesDoesNotMatch_success() throws Exception {
        Node node1 = new Node("root");
        node1.getAttributes().put("id","1");
        Node node2 = new Node("root");

        assertFalse((new XPathNodeMatchingStrategy().attributesMatch(node1, node2)));
    }

    @Test
    public void testAttributesMatch2() throws Exception {
        Node node1 = new Node("root");
        node1.getAttributes().put("id","1");
        Node node2 = new Node("root");
        node2.getAttributes().put("id","1");
        node2.getAttributes().put("category","public");

        assertTrue(new XPathNodeMatchingStrategy().attributesMatch(node1, node2));
    }
}