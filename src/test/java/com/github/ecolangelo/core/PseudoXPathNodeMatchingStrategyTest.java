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
}