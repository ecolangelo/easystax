package com.github.ecolangelo.core;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class XmlNavigationPathTest {



    @Test
    public void resolveStackAfterPushing_3_tags_success() throws Exception {
        XmlNavigationPath stack = new XmlNavigationPath();
        stack.pushTag("teiCorpus");
        stack.pushTag("teiHeader");
        stack.pushTag("sourceDesc");

        assertThat(stack.resolvePath(),is("/teiCorpus/teiHeader/sourceDesc/"));
    }


    @Test
    public void resolveStackAfterPushing_3_tags_and_remove_1_success() throws Exception {
        XmlNavigationPath stack = new XmlNavigationPath();
        stack.pushTag("teiCorpus");
        stack.pushTag("teiHeader");
        stack.pushTag("sourceDesc");
        stack.popTag();

        assertThat(stack.resolvePath(), is("/teiCorpus/teiHeader/"));
    }

    @Test
    public void resolveStackAfterPushing_5_tags_and_remove_4_success() throws Exception {
        XmlNavigationPath stack = new XmlNavigationPath();
        stack.pushTag("teiCorpus");
        stack.pushTag("teiHeader");
        stack.pushTag("sourceDesc");
        stack.pushTag("tag1");
        stack.pushTag("tag3");
        assertThat(stack.resolvePath(),is("/teiCorpus/teiHeader/sourceDesc/tag1/tag3/"));
        stack.popTag();
        stack.popTag();
        stack.popTag();
        stack.popTag();

        assertThat(stack.resolvePath(),is("/teiCorpus/"));
    }

    @Test
    public void resolveStackAfterPushing_1_tags_and_remove_5_success() throws Exception {
        XmlNavigationPath stack = new XmlNavigationPath();
        stack.pushTag("teiCorpus");

        stack.popTag();
        stack.popTag();
        stack.popTag();
        stack.popTag();

        assertThat(stack.resolvePath(),is("/"));
    }


    @Test
    public void pushOneTag_success() throws Exception{
        XmlNavigationPath stack = new XmlNavigationPath();
        stack.pushTag("teiCorpus");
        assertThat(stack.getCurrent(), is("teiCorpus"));
        assertNull(stack.getParent());
    }

    @Test
    public void pushTwoTags_success() throws Exception{
        XmlNavigationPath stack = new XmlNavigationPath();
        stack.pushTag("teiCorpus");
        stack.pushTag("teiHeader");
        assertThat(stack.getCurrent(), is("teiHeader"));
        assertThat(stack.getParent(), is("teiCorpus"));
    }

    @Test
    public void push4Tags_and_remove2_integration_success() throws Exception{
        XmlNavigationPath stack = new XmlNavigationPath();
        stack.pushTag("root");
        assertThat(stack.getCurrent(), is("root"));
        assertNull(stack.getParent());
        assertThat(stack.resolvePath(), is("/root/"));

        stack.pushTag("teiCorpus");

        assertThat(stack.getCurrent(), is("teiCorpus"));
        assertThat(stack.getParent(), is("root"));
        assertThat(stack.resolvePath(), is("/root/teiCorpus/"));

        stack.pushTag("TEI");

        assertThat(stack.getParent(), is("teiCorpus"));
        assertThat(stack.getCurrent(), is("TEI"));
        assertThat(stack.resolvePath(), is("/root/teiCorpus/TEI/"));

        stack.pushTag("teiHeader");
        assertThat(stack.getCurrent(), is("teiHeader"));
        assertThat(stack.getParent(), is("TEI"));
        assertThat(stack.resolvePath(), is("/root/teiCorpus/TEI/teiHeader/"));

        stack.popTag();
        assertThat(stack.getCurrent(), is("TEI"));
        assertThat(stack.getParent(), is("teiCorpus"));
        assertThat(stack.resolvePath(), is("/root/teiCorpus/TEI/"));
        stack.popTag();
        assertThat(stack.getCurrent(), is("teiCorpus"));
        assertThat(stack.getParent(), is("root"));
        assertThat(stack.resolvePath(), is("/root/teiCorpus/"));

        stack.popTag();

        assertThat(stack.getCurrent(), is("root"));
        assertNull(stack.getParent());
        assertThat(stack.resolvePath(), is("/root/"));
        stack.popTag();

        assertNull(stack.getCurrent());
        assertNull(stack.getParent());
        assertThat(stack.resolvePath(), is("/"));
    }






}