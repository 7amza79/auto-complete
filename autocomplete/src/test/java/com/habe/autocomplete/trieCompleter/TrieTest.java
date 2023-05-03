package com.habe.autocomplete.trieCompleter;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TrieTest {

    private final List<String> DICTIONNARY = Arrays.asList(
            "project runway"
            , "pinterest"
            , "river"
            , "kayak"
            , "progenex"
            , "progeria"
            , "pg&e"
            , "project free tv"
            , "bank"
            , "proactive"
            , "progesterone"
            , "press democrat"
            , "priceline"
            , "pandora"
            , "reprobe",
            "paypal"
    );



    @Test
    public void insert_should_insert_words_individually_not_in_order() {
        // Given

        TrieCompleter trie = new TrieCompleter();

        // When
        List<String> dictionnary = new ArrayList<>();
        dictionnary.add("Hello");
        dictionnary.add("Bonjour");
        dictionnary.add("Salut");
        dictionnary.add("Patate");
        dictionnary.add("Colombia");
        dictionnary.add("Actor");

        trie.constructTrie(dictionnary);


        // Then

        final Node root = trie.getRoot();
        Assertions.assertThat(root).isNotNull();
        Assertions.assertThat(root.getChildren()).isNotEmpty();
        Assertions.assertThat(root.getChildren()).hasSize(6);


    }


    @Test
    public void constructTrie_should_create_trie_with_odrer() {

        // Given

        TrieCompleter trie = new TrieCompleter();


        // When

        trie.constructTrie(DICTIONNARY);


        // Then

        final Node root = trie.getRoot();
        Assertions.assertThat(root).isNotNull();
        Assertions.assertThat(root.getChildren()).isNotEmpty();
        Assertions.assertThat(root.getChildren()).hasSize(4);

    }


    @Test
    public void searchPrefix_should_return_right_values_when_limit4_and_prefix_p() {

        // Given

        TrieCompleter trie = new TrieCompleter();


        // When

        trie.constructTrie(DICTIONNARY);


        // Then

        final List<String> results = trie.execute("p", 4);


        Assertions.assertThat(results).hasSize(4);
        Assertions.assertThat(results).containsExactly("pandora","paypal","pg&e","pinterest");

    }

    @Test
    public void searchPrefix_should_return_right_values_when_limit4_and_prefix_pr() {

        // Given

        TrieCompleter trie = new TrieCompleter();


        // When

        trie.constructTrie(DICTIONNARY);


        // Then

        final List<String> results = trie.execute("pr", 4);


        Assertions.assertThat(results).hasSize(4);
        Assertions.assertThat(results).containsExactly("press democrat","priceline","proactive","progenex");

    }

    @Test
    public void searchPrefix_should_return_right_values_when_limit4_and_prefix_pro() {

        // Given

        System.gc();
        Runtime runtime = Runtime.getRuntime();
        final long usedMemoryBefore = runtime.totalMemory() - runtime.freeMemory();

        TrieCompleter trie = new TrieCompleter();
        trie.constructTrie(DICTIONNARY);


        // When
        long start = System.nanoTime();

        final List<String> results = trie.execute("pro", 4);

        long end = System.nanoTime();
        long usedMemoryAfter = runtime.totalMemory() - runtime.freeMemory();

        // Then
        System.out.println("Elapsed Time for trie autocompleter for \"pro\" prefix in nano seconds: ["+ (end-start)+
                "].");
        System.out.println("Memory used: [" + (usedMemoryAfter-usedMemoryBefore) +"] bytes.");

        Assertions.assertThat(results).hasSize(4);
        Assertions.assertThat(results).containsExactly("proactive","progenex","progeria","progesterone");

    }

    @Test
    public void searchPrefix_should_return_right_values_when_limit4_and_prefix_prog() {

        // Given

        System.gc();
        Runtime runtime = Runtime.getRuntime();
        final long usedMemoryBefore = runtime.totalMemory() - runtime.freeMemory();

        TrieCompleter trie = new TrieCompleter();

        trie.constructTrie(DICTIONNARY);


        // When
        long start = System.nanoTime();
        final List<String> results = trie.execute("prog", 4);

        long end = System.nanoTime();
        long usedMemoryAfter = runtime.totalMemory() - runtime.freeMemory();

        // Then
        System.out.println("Elapsed Time for trie autocompleter for \"prog\" prefix in nano seconds: ["+ (end-start)+
                "].");
        System.out.println("Memory used: [" + (usedMemoryAfter-usedMemoryBefore) +"] bytes.");

        Assertions.assertThat(results).hasSize(3);
        Assertions.assertThat(results).containsExactly("progenex","progeria","progesterone");

    }


    @Test
    public void searchPrefix_should_returnRightResults_when_largeFileProvided() throws IOException {
        // Given

        System.gc();
        Runtime runtime = Runtime.getRuntime();
        final long usedMemoryBefore = runtime.totalMemory() - runtime.freeMemory();

        TrieCompleter trie = new TrieCompleter();


        List<String> largeDict = getDICTIONNARY();
        trie.constructTrie(largeDict);


        // When
        long start = System.nanoTime();
        final List<String> results = trie.execute("prog", 4);

        long end = System.nanoTime();
        long usedMemoryAfter = runtime.totalMemory() - runtime.freeMemory();

        // Then
        System.out.println("Elapsed Time for trie autocompleter with large dataset for \"prog\" prefix in nano " +
                "seconds: ["+ (end-start)+
                "].");
        System.out.println("Memory used with large dataset: [" + (usedMemoryAfter-usedMemoryBefore) +"] bytes.");

        Assertions.assertThat(results).hasSize(4);
        Assertions.assertThat(results).containsExactly("prog", "prog.", "progambling", "progamete");
    }



    private List<String> getDICTIONNARY() throws IOException {


        List<String> result;
        try (Stream<String> lines = Files.lines(Paths.get("src/test/resources/words.txt"))) {
            result = lines.collect(Collectors.toList());
        }
        return result;

    }
}
