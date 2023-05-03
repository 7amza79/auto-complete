package com.habe.autocomplete;


import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class SimpleAutocompleter implements Autocompleter {


    public static final int SEARCH_LIMIT_NUMBER = 4;
    private final List<String> dictionnary;

    public SimpleAutocompleter(List<String> dictionnary) {
        this.dictionnary = dictionnary;
    }

    @Override
    public List<String> execute(String prefix, int limit) {

        final List<String> lowercaseSortedWords = dictionnary
                .stream()
                .map(String::toLowerCase)
                .sorted()
                .collect(Collectors.toList());

        return lowercaseSortedWords.stream().filter(word -> word.startsWith(prefix.toLowerCase())).limit(SEARCH_LIMIT_NUMBER).collect(Collectors.toList());

    }
}
