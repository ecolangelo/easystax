package com.github.ecolangelo.core;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class NodeTest {

    @Test
    public void testGetXPath() throws Exception {
        Node node = new Node(null, new HashMap<String, String>(){{
            put("xml:id","augh");
            put("type","publication");
        }},"root");
        System.out.println("node = " + node);
    }
}