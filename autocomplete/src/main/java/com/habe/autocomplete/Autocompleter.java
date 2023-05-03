package com.habe.autocomplete;

import java.util.List;

public interface Autocompleter {

    List<String> execute(String prefix, int number);
}
