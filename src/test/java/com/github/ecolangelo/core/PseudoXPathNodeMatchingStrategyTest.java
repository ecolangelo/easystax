package com.github.ecolangelo.core;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class PseudoXPathNodeMatchingStrategyTest {

    @Test
    public void testMatch1() throws Exception {
        // first: /root/child
        // second: /root/child
        Node root = new Node("root");
        Node first = root.append(new Node("child"));

        Node secondroot = new Node("root");
        Node second = secondroot.append(new Node("child"));

        assertTrue(new PseudoXPathNodeMatchingStrategy().match(first, second));

    }

    @Test
    public void testGracefulNodeValidation_true_success () throws Exception {
        Node first = new Node("root");
        Node second = new Node("root");
        assertTrue(new PseudoXPathNodeMatchingStrategy().gracefulNodeValidation(first, second));
    }

    @Test
    public void testGracefulNodeValidation_true1_success () throws Exception {
        Node first = new Node("root");

        Node second = new Node("root");
        second.getAttributes().put("id","1");
        assertTrue(new PseudoXPathNodeMatchingStrategy().gracefulNodeValidation(first, second));
    }

    @Test
    public void testGracefulNodeValidation_false_success () throws Exception {
        Node first = new Node("root");
        first.getAttributes().put("id","1");
        Node second = new Node("root");
        second.getAttributes().put("id","1");
        second.getAttributes().put("type","application");
        assertFalse(new PseudoXPathNodeMatchingStrategy().gracefulNodeValidation(first, second));
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

        assertTrue("\n" + first.getXPath() + " ---> " + second.getXPath(), new PseudoXPathNodeMatchingStrategy().match(first, second));

    }
}