#AutoCompleter

This project is for experimentation / test purposes.
The code implements an auto complete program that provides sorted suggestions for a prefix in `java 8`.


## Disclaimer

Since this is an experimentation code, no interface has been coded.
The interface that sets the desired behaviour is `Autocompleter.java`.
The main method of this interface will take a prefix (_String: prefix_) and a number (_int: number_) and should return at most `number` of suggestions.

## Implementation

In order to implement the autocomplete program we followed the TDD (_Test Driven Development_) process which gave us as first simple implementation the `SimpleAutocompleter.java` class.

In this version, we used a simple sorted arraylist as data structure to store the known words. We will discuss later the sorting process.

Adding performance constraints (mainly querying speed) led us to the second implementation in which we used the `trie` data structure which we will discuss later.

In order to measure the solution performances, we considered two aspects:

    - Execution time : Querying data.
    - Memory consumption: used memory at runtime from loading to querying data.

Execution time comparison :

**Small Dataset**

        Elapsed Time for simple autocompleter for "pro" prefix in milli seconds: [105056065]. ~0,105056065s
        Memory used: [2621480] bytes. ~2.5 Mb


        Elapsed Time for trie autocompleter for "pro" prefix in milli seconds: [497142]. ~0,000497142s
        Memory used: [2621504] bytes. ~2.5 Mb


Trie autocompleter is 211 times faster than simple autompleter for this small dataset using the same amount of memory for storage.

----

**Large Dataset**

        Elapsed Time for simple autocompleter with large dataset for "prog" prefix in milli seconds: [165374365]. ~0,165374365s
        Memory used with large dataset: [55026184] bytes. ~52 Mb
        
        
        Elapsed Time for trie autocompleter with large dataset for "prog" prefix in milli seconds: [235480]. ~0,00023548s
        Memory used with large dataset: [335024816] bytes. ~319 Mb

For a larger dataset, the trie based autocompleter is 702 times faster than the simple autocompleter using 6 times the amount of memory.

## Technical details

### Sorting data: Speed & Order

- #### Sorting at writing vs sorting at reading :

Assumptions:

    - Lexicographic order should be garanteed when responding to requests.
    - The system is more likely to be requested on the reading than on the writing : Write once - Read many.
    - The system will need to respond as fast as possible to the reading requests.
    - The system doesn't have a writing speed/time constraint.


Tradeoffs/decisions:

    - Lexicographic sorting can be done at the data writing step and not at the reading. That will garantee a faster response at reading.
    - Data structures that garantee the preservation of the sorting must be used to store data.


- #### Sorting manually vs using self sorting data structures


Assumptions:

    - Sorting can be done through ordered data structures e.g _treemap_. The treemap structure garantees the ordering of the keys at the reading and offers an O(log N) lookup complexity.
    - At the other hand sorting can be done manually and storage garanteeing this order can be done using LinkedHashmap structure that garantees that keys are ordered by intsertion order. The lookup and insertion complexity of linkedHashmap is O(1) making it faster than treeMap structure.
    - Ordinary Hashmap structures cannot be used since the order is never garanteed with these structures.


Tradeoffs/decisions:

    - We will prefer using linkedHashmap structure over tree map structure to store the data and we will make the sorting before inserting data.



- #### Data structuring : _Trie_


Assumptions:

    - A prefix is used to identify the requested words.
    - Each word can be considered as a prefix (assuming that there is not a length limit for prefix).
    - Data is lexically linked: each element is a either an association of the previous elements or a contuinity of them.  

Tradeoffs/decisions:

    - A data structure garanteeing links between prefixes and words should be used. Since every word can be used as a prefix (for now), links should exist between all connected words.
    - We will use the trie data structure or **prefix tree** to modelize and store the data.
    - Since the prefix can be a single character or a word. For e.g, all of "t", "te", "tea" should be considered as prefixes for the word "tear". Note that "tea" is not only a prefix but also a word so our data structure should be able to handle it.




### Possible optimizations

    - Limiting the trie depth: 
    In this version of code, we considered an unlimited depth of the trie which can lead to unnecessary storage usage and additional search time. 
    A possible optimization could be to limit the trie depth. 
    It should be a tradeoff considering the maximum prefix length possible. 
    In this implementation we didn't use this mechanism for simplicity purposes since we used the `Character` data type as key of the nodes. 
    In order to limit the depth of the trie, we should use the `String` type as key. 

    - Not storing the unuseful prefixes:
    Assuming that one character prefixes aren't very useful in a real usage we can gain storage space not storing them. That also requires using `String` data structure as key.

    - Not storing stop words as prefixes:
    Assuming that stop words may not be relevant in searchs, we can also gain space storage not storing them.


### Scaling requirements

If keywords were much larger :

In this implementation, the keywords are stored using TRIE structure which resides in main memory and this cannot be possible to be done if the keywords size explodes.
The application could be very RAM intensive which could not be very optimal. If the trie size exceeds the allocated memory size, the JVM will have to handle swap in/out issues which will affect performance.
In this type of issues, and to handle this type and this size of data, I would recommend using a document oriented database with text search engine (such as elasticsearch).

Why elasticsearch ?

- Since elasticsearch is a scaling distributed database.
- Since elasticsearch offers advanced and optimized search features(using Lucene) that can handle this type of issues such as:
    - NGrams: The idea is to create all the possible prefixes for every word and indexing them.
      Which would make the query very fast. This method is exhaustive, fast but can be very memory exhaustive since it creates and stores a large number of tokens.
    - Completion suggester : A tool provided by elastic which is based on an in-memory data structure called `FST` Finite State Transducer (very similar to the trie structure). ([For more info about this data structure](https://blog.burntsushi.net/transducers/)).
      This suggester assures high speed provides multiple features such as custom ordering which can be important in some cases. More details about this suggester can be found in this [blog article](https://www.elastic.co/fr/blog/you-complete-me).
    - search-as-you-type field type: a dedicated data type to searching for both prefix and infix searches. [ES documentation](https://www.elastic.co/guide/en/elasticsearch/reference/7.2/search-as-you-type.html#search-as-you-type).
    - Prefix query : A simple way to get data using prefix without altering the indexes. We would not recommend this method since it is not effective (Risk of missing results) and is very slow.

A very interesting and detailed comparison between these methods can be found in this [blog post](https://medium.com/@mourjo_sen/a-detailed-comparison-between-autocompletion-strategies-in-elasticsearch-66cb9e9c62c4).



### Sub-sequence search requirements


One way to achieve this need is to use the `suffix trie` data structure
or `suffix tree` that is a compression of this structure or also the `suffix array` that is an optimization of the suffix tree.

The basic principle of suffix tries is to store for each word, every possible suffixes beginning from each letter.

A simple suffix trie using the word `reprobe$` would be like the trie below.
Note that the `$` character is used to indicate the end of a word.

![](/home/habe/auto-complete/autocomplete/reprobe_trie.png)

Matching a sub-sequence would be possible by searching its existence on the trie and returning
the words related to that suffix. 

[An explaining blog article for suffix trie and its usage.](https://www.geeksforgeeks.org/pattern-searching-using-trie-suffixes/?ref=lbp)




