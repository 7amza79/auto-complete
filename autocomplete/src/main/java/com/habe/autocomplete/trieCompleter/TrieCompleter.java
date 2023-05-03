package com.habe.autocomplete.trieCompleter;

import com.habe.autocomplete.Autocompleter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class TrieCompleter implements Autocompleter {


    private final Node root;


    public TrieCompleter() {
        this.root = new Node();
    }

    public Node getRoot() {
        return root;
    }

    private void insert(String word) {

        Node usedNode = root;
        for (char character : word.toCharArray()) {
            usedNode = usedNode.getChildren().computeIfAbsent(character, Node::new);
        }
        usedNode.setWord(true);
        usedNode.setValue(word);
    }


    public void constructTrie(List<String> words) {

        final List<String> lowercase = words.stream().map(word -> word.toLowerCase(Locale.ROOT)).sorted().collect(Collectors.toList());

        lowercase.forEach(this::insert);
    }


    public List<String> execute(String prefix, int limit) {

        if (prefix.isEmpty() || !this.root.getChildren().containsKey(prefix.charAt(0))) {
            return new ArrayList<>();
        }

        Node usedNode = root;
        for (char character : prefix.toCharArray()) {
            if (usedNode.getChildren().containsKey(character)) {
                usedNode = usedNode.getChildren().get(character);
            } else {
                return new ArrayList<>();
            }
        }
        return usedNode.getRecursiveSortedChildren(limit, new ArrayList<>());
    }


}
