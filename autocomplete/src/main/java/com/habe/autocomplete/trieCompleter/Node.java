package com.habe.autocomplete.trieCompleter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

class Node {

    private boolean isWord;
    private final Map<Character, Node> children;
    private String value;


    public Node() {
        this.children = new LinkedHashMap<>();
    }

    public Node(char character) {
        this.children = new LinkedHashMap<>();
        value = String.valueOf(character);
    }


    public void setWord(boolean word) {
        isWord = word;
    }

    public Map<Character, Node> getChildren() {
        return children;
    }


    public void setValue(String value) {
        this.value = value;
    }

    List<String> getRecursiveSortedChildren(int limit, List<String> resultValues) {

        if (resultValues.size() == limit) {
            return resultValues;
        }


        if (this.isWord && resultValues.size() < limit) {
            resultValues.add(this.value);
        }
        if (!this.children.isEmpty() && resultValues.size() < limit) {
            for (char node : this.children.keySet()) {
                resultValues = this.children.get(node).getRecursiveSortedChildren(limit, resultValues);
            }
        }

        return resultValues;
    }
}
