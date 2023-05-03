package com.habe.autocomplete.trieCompleter;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class NodeTest {


    @Test
    public void getRecursiveSortedChildren_should_return_right_2values() {

        // Given
        TrieCompleter trie = new TrieCompleter();

        List<String> dictionnary = new ArrayList<>();

        dictionnary.add("pandora");
        dictionnary.add("paypal");
        dictionnary.add("pg&e");
        dictionnary.add("patate");
        dictionnary.add("pinterest");

        trie.constructTrie(dictionnary);

        final Node root = trie.getRoot();

        // When

        final List<String> recursiveSortedChildren = root.getRecursiveSortedChildren(2, new ArrayList<>());


        // Then

        Assertions.assertThat(recursiveSortedChildren).hasSize(2);
        Assertions.assertThat(recursiveSortedChildren).containsExactly("pandora", "patate");


    }

    @Test
    public void getRecursiveSortedChildren_should_return_right_3values() {

        // Given
        TrieCompleter trie = new TrieCompleter();

        List<String> dictionnary = new ArrayList<>();

        dictionnary.add("pandora");
        dictionnary.add("pg&e");
        dictionnary.add("patate");
        dictionnary.add("pinterest");

        trie.constructTrie(dictionnary);

        final Node root = trie.getRoot();

        // When

        final List<String> recursiveSortedChildren = root.getRecursiveSortedChildren(3, new ArrayList<>());

        // Then

        Assertions.assertThat(recursiveSortedChildren).hasSize(3);
        Assertions.assertThat(recursiveSortedChildren).containsExactly("pandora", "patate","pg&e");

    }


    @Test
    public void getRecursiveSortedChildren_should_return_right_4values() {

        // Given
        TrieCompleter trie = new TrieCompleter();

        List<String> dictionnary = new ArrayList<>();

        dictionnary.add("pandora");
        dictionnary.add("pa");
        dictionnary.add("pg&e");
        dictionnary.add("patate");
        dictionnary.add("pinterest");

        trie.constructTrie(dictionnary);

        final Node root = trie.getRoot();

        // When

        final List<String> recursiveSortedChildren = root.getRecursiveSortedChildren(4, new ArrayList<>());

        // Then

        Assertions.assertThat(recursiveSortedChildren).hasSize(4);
        Assertions.assertThat(recursiveSortedChildren).containsExactly("pa","pandora", "patate","pg&e");

    }
}
