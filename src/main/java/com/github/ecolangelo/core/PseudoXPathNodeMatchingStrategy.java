package com.github.ecolangelo.core;

import java.util.Map;

public class PseudoXPathNodeMatchingStrategy implements NodeMatchingStrategy{

    /*
    *
    * testing matching of one node against the other.
    * a node is rapresented by his value(tagName+attributes) and his parents
    * e.g. /root[id='']/child1[id=''] etc...
    * nodeToBeTested could omit the attributes, following xPath behaviour, it will
    * anyway match:
    * /root/child1/child2 will match against a node made of
    * /root[id=1]/child1[id=1]/child2[id=3]
    * while for instance the other way around won't match
    *
    */

    @Override
    public boolean match(Node nodeToBeTested, Node sample) {
        if (!gracefulNodeValidation(nodeToBeTested, sample)) return false;

        Node parentToBeTested = nodeToBeTested.getParent();
        Node parentSample = sample.getParent();
        boolean testResult = true;
        while(parentToBeTested != null && parentSample != null && testResult) {
            testResult = gracefulNodeValidation(parentToBeTested, parentSample);
            parentToBeTested = parentToBeTested.getParent();
            parentSample = parentSample.getParent();
        }
        return testResult;
    }

    protected boolean gracefulNodeValidation(Node nodeToBeTested, Node sample) {
        if(!nodeToBeTested.getName().equals(sample.getName())) return false;

        Map<String,String> attributes = nodeToBeTested.getAttributes();
        if(attributes != null && attributes.size()>0 && !attributes.equals(sample.getAttributes())){
            return false;
        }
        return true;
    }
}
