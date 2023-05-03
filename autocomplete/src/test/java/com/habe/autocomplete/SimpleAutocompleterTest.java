package com.habe.autocomplete;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class SimpleAutocompleterTest {

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
    public void execute_should_return_right_propositions_for_p() {
        // Given
        System.gc();
        Runtime runtime = Runtime.getRuntime();
        final long usedMemoryBefore = runtime.totalMemory() - runtime.freeMemory();

        SimpleAutocompleter autoCompleter = new SimpleAutocompleter(DICTIONNARY);
        // When
        long start = System.nanoTime();

        final List<String> result = autoCompleter.execute("p", 4);
        long end = System.nanoTime();
        long usedMemoryAfter = runtime.totalMemory() - runtime.freeMemory();
        // Then


        System.out.println("Elapsed Time for simple autocompleter for \"p\" prefix in nano seconds: [" + (end - start)+"].");
        System.out.println("Memory used: [" + (usedMemoryAfter - usedMemoryBefore)+"] bytes.");
        assertEquals(4, result.size());
        Assertions.assertThat(result).containsExactly("pandora", "paypal", "pg&e", "pinterest");

    }

    @Test
    public void execute_should_return_completePropositions_for_pr() {
        // Given

        System.gc();
        Runtime runtime = Runtime.getRuntime();
        final long usedMemoryBefore = runtime.totalMemory() - runtime.freeMemory();
        SimpleAutocompleter autoCompleter = new SimpleAutocompleter(DICTIONNARY);
        // When
        long start = System.nanoTime();

        final List<String> result = autoCompleter.execute("pr", 4);
        long end = System.nanoTime();
        long usedMemoryAfter = runtime.totalMemory() - runtime.freeMemory();

        // Then

        System.out.println("Elapsed Time for simple autocompleter for \"pr\" prefix in nano seconds: [" + (end - start)+"].");
        System.out.println("Memory used: [" + (usedMemoryAfter - usedMemoryBefore)+"].");

        assertEquals(4, result.size());
        Assertions.assertThat(result).containsExactly("press democrat",
                "priceline",
                "proactive",
                "progenex");
    }


    @Test
    public void execute_should_return_completePropositions_for_pro() {
        // Given

        System.gc();
        Runtime runtime = Runtime.getRuntime();
        final long usedMemoryBefore = runtime.totalMemory() - runtime.freeMemory();

        SimpleAutocompleter autoCompleter = new SimpleAutocompleter(DICTIONNARY);
        // When
        long start = System.nanoTime();

        final List<String> result = autoCompleter.execute("pro", 4);
        long end = System.nanoTime();
        long usedMemoryAfter = runtime.totalMemory() - runtime.freeMemory();

        // Then
        System.out.println("Elapsed Time for simple autocompleter for \"pro\" prefix in nano seconds: [" + (end - start) + "].");
        System.out.println("Memory used: [" + (usedMemoryAfter - usedMemoryBefore) + "] bytes.");

        assertEquals(4, result.size());
        Assertions.assertThat(result).containsExactly("proactive",
                "progenex",
                "progeria",
                "progesterone");
    }

    @Test
    public void execute_should_return_completePropositions_for_prog() {
        // Given
        System.gc();
        Runtime runtime = Runtime.getRuntime();
        final long usedMemoryBefore = runtime.totalMemory() - runtime.freeMemory();

        SimpleAutocompleter autoCompleter = new SimpleAutocompleter(DICTIONNARY);
        // When
        long start = System.nanoTime();

        final List<String> result = autoCompleter.execute("prog", 4);
        long end = System.nanoTime();
        long usedMemoryAfter = runtime.totalMemory() - runtime.freeMemory();

        // Then
        System.out.println("Elapsed Time for simple autocompleter for \"prog\" prefix in nano seconds: [" + (end - start) + "].");
        System.out.println("Memory used: [" + (usedMemoryAfter - usedMemoryBefore) + "] bytes.");

        assertEquals(3, result.size());
        Assertions.assertThat(result).containsExactly("progenex",
                "progeria",
                "progesterone");
    }


    @Test
    public void searchPrefix_should_returnRightResults_when_largeFileProvided() throws IOException {
        // Given

        System.gc();
        Runtime runtime = Runtime.getRuntime();
        final long usedMemoryBefore = runtime.totalMemory() - runtime.freeMemory();

        List<String> largeDict = getDICTIONNARY();
        SimpleAutocompleter autoCompleter = new SimpleAutocompleter(largeDict);


        // When
        long start = System.nanoTime();
        final List<String> results = autoCompleter.execute("prog", 4);

        long end = System.nanoTime();
        long usedMemoryAfter = runtime.totalMemory() - runtime.freeMemory();

        // Then
        System.out.println("Elapsed Time for simple autocompleter with large dataset for \"prog\" prefix in nano " +
                "seconds: [" + (end - start) +
                "].");
        System.out.println("Memory used with large dataset: [" + (usedMemoryAfter - usedMemoryBefore) + "] bytes.");

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
