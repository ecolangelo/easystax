package com.github.ecolangelo;

import com.github.ecolangelo.core.ParsingResult;
import org.junit.Test;

import java.io.InputStream;

import static org.junit.Assert.*;

public class StaxIteratorIntegrationTest {

    @Test
    public void test() throws Exception {
        InputStream is = this.getClass().getResourceAsStream("/books.xml");
        StaxIterator iterator = new StaxIterator(is,"/bookstore/book/title");

        while(iterator.hasNext()) {
            ParsingResult ps = iterator.next();

            System.out.println("ps.getContent() = " + ps.getContent());

        }

    }

}