package com.github.ecolangelo.core;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * rapresentation of an xml Node
 */
public class Node {

    final static Pattern FRAGMENT_VALIDATOR = Pattern.compile("");

    final static Pattern ATTRIBUTE_PATTERN = Pattern.compile("\\[(.+)\\]");

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
        if(path.startsWith("/"))path = path.replaceFirst("/","");
        String[] splittedPath= path.split("/");
        if(splittedPath.length == 0) throw new InvalidPathException("no fragments in path, looks is empty, path:"+path);
        String currentFragment = splittedPath[0];
        boolean isValid = validateFragment(currentFragment);
        if(!isValid) {
            throw new InvalidPathException("path: "+path+" invalid, found invalid fragment ---> "+currentFragment);
        }
        Node currentNode = createNodeFromXpathFragment(currentFragment);

        for(int i = 1; i<splittedPath.length;i++){
            currentFragment = splittedPath[i];
            isValid = validateFragment(currentFragment);
            if(!isValid) {
                throw new InvalidPathException("path: "+path+" invalid, found invalid fragment ---> "+currentFragment);
            }
            currentNode = currentNode.append(createNodeFromXpathFragment(currentFragment));
        }
        return currentNode;
    }

    protected static Node createNodeFromXpathFragment(String fragment){
        StringBuilder builderForTagName = new StringBuilder();

        for(int i = 0;i<fragment.length();i++) {
            char a = fragment.charAt(i);
            if(a !='['){
                builderForTagName.append(a);
            }
            else{
                break;
            }

        }
        Node node = new Node(builderForTagName.toString());

        Matcher m = ATTRIBUTE_PATTERN.matcher(fragment);
        if(m.find()) {
            String attributesStr = m.group(1);
            String[] attributeFragments = attributesStr.split(",");
            for (String attributeFragment : attributeFragments) {
                String[] splittedFragments = attributeFragment.split("=");
                node.getAttributes().put(splittedFragments[0], splittedFragments[1]);
            }
        }

        return node;
    }

    protected static boolean validateFragment(String fragment) {
        return true;
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
