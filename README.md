LCS Algorithms
==============

__A study of different Longest Common Sub-sequence algorithms with
optimizations.__

- __version:__ 1.0
- __released:__ 22 December 2015
- __author:__ Francesco Illuminati (fillumina@gmail.com)
- __license:__ [apache 2.0](http://www.apache.org/licenses/LICENSE-2.0)


## Goals

This project accomplishes two different goals:

1. realization of some fast LCS implementations (see `lcs` module)
2. study of some different practical LCS algorithms (see `lcs-algorithm` module)

An [LCS](https://en.wikipedia.org/wiki/Longest_common_subsequence_problem)
algorithm is a procedure to recognize the Longest Common Subsequence from
two given sequences. The result of the calculation can be any of these:

1. the length of the LCS
2. a set of matching indexes from the two sequences
3. the LCS sequence itself

Notice that the LCS is not unique, there could be many different sub-sequences
of the same (maximum) size.

The module containing the optimized algorithms will be soon released on
maven central.


## Modules

The project is divided in 4 (maven) modules:

1. `lcs-helper` defines some common interfaces
2. `lcs-test-util` contains a test suite to be sure that every
algorithm is correct.
3. `lcs-algorithm` contains the implementation of many different
LCS algorithms. There is also a performance test to compare them
under different conditions.
4. `lcs` contains performance-wise implementations that can be imported
and used separately from the other modules of the project.


## Algorithms

The algorithms included are (along with some variants and optimizations):

* [Recursive]
(lcs-algorithms/src/main/java/com/fillumina/lcs/algorithm/recursive/RecursiveLcs.java)
* [Memoized Recursive]
(lcs-algorithms/src/main/java/com/fillumina/lcs/algorithm/recursive/MemoizedRecursiveLcs.java)
* [Bottom-up]
(lcs-algorithms/src/main/java/com/fillumina/lcs/algorithm/scoretable/BottomUpLcs.java)
* [Wagner-Fisher]
(lcs-algorithms/src/main/java/com/fillumina/lcs/algorithm/scoretable/WagnerFischerLcs.java)
* [Smith-Waterman]
(lcs-algorithms/src/main/java/com/fillumina/lcs/algorithm/scoretable/SmithWatermanLcs.java)
* [Linear space Hirschberg]
(lcs-algorithms/src/main/java/com/fillumina/lcs/algorithm/hirschberg/HirschbergLinearSpaceAlgorithmLcs.java)
* [Myers]
(lcs-algorithms/src/main/java/com/fillumina/lcs/algorithm/myers/MyersLcs.java)
* [Linear Space Myers]
(lcs-algorithms/src/main/java/com/fillumina/lcs/algorithm/myers/linearspace/LinearSpaceMyersLcsSolver.java)

There is also a very fast implementation of the Levenshtein distance algorithm.


## Speed Considerations

Different algorithms perform differently depending on the input. Myers'
algorithms perform very well when the sequences are similar (shares the majority
of the elements) but their memory usage and execution time grows dramatically
when the sequences differ. The scoretable algorithms always execute the same
number of operations independently from the similarity of the input sequences
and they are particularly performant when the sequences are small.
The Hirschberg algorithm is a linear space improvement over the Wagner-Fischer
algorithm which has quadratic space usage. It's slower than the Wagner-Fischer
but a good choice if the sequences are different and long.

All the presented tests are executed with [AlgorithmPerformanceTest]
(lcs-algorithms/src/test/java/com/fillumina/lcs/algorithm/performance/AlgorithmsPerformanceTest.java).

### Medium size similar sequences (`TOTAL=600`, `LCS=400`)

    SmithWaterman                 	   0 :	      1.97 ms		     74.90 %
    WagnerFischer                 	   1 :	      2.14 ms		     81.50 %
    OptimizedHirschbergLinearSpace	   2 :	      2.63 ms		    100.00 %
    OptimizedMyersLcs             	   3 :	      0.85 ms		     32.56 %
    OptimizedLinearSpaceLcs       	   4 :	      1.00 ms		     38.28 %

When the sequences are similar and not very long the Myers algorithm is the
clear winner. Note that the linear space variant isn't that slower.
The performance of the scoretable algorithms doesn't depend on the similarities.

### Long similar sequences (`TOTAL=6000`, `LCS=4000`)

    SmithWaterman                 	   0 :	    251.23 ms		     61.32 %
    WagnerFischer                 	   1 :	    409.72 ms		    100.00 %
    OptimizedHirschbergLinearSpace	   2 :	    250.73 ms		     61.20 %
    OptimizedMyersLcs             	   3 :	     89.51 ms		     21.85 %
    OptimizedLinearSpaceLcs       	   4 :	     92.15 ms		     22.49 %

When the length of the sequences grows the memory access become more costly and
the performances of the Hirschberg algorithm starts to improve in respect to
the scoretable algorithms. Myers algorithms are still the best but the performances of
the linear space algorithm is starting to pair with those of the simple
algorithm. For longer sequences the simple Myers algorithm cannot be used
because of the enormous amount of memory required.

### Medium size different sequences (`TOTAL=600`, `LCS=4`)

    SmithWaterman                 	   0 :	      2.43 ms		     36.44 %
    WagnerFischer                 	   1 :	      2.22 ms		     33.35 %
    OptimizedHirschbergLinearSpace	   2 :	      2.76 ms		     41.30 %
    OptimizedMyersLcs             	   3 :	      6.02 ms		     90.11 %
    OptimizedLinearSpaceLcs       	   4 :	      6.68 ms		    100.00 %

When the sequences are different (share very few elements) the results
change dramatically: the scoretable algorithms (Smith-Waterman and
Wagner-Fischer) stay stable while the Myers algorithms become much slower.

### Big different sequences (`TOTAL=6000`, `LCS=40`)

    SmithWaterman                 	   0 :	    218.84 ms		     31.72 %
    WagnerFischer                 	   1 :	    293.51 ms		     42.54 %
    OptimizedHirschbergLinearSpace	   2 :	    238.50 ms		     34.57 %
    OptimizedMyersLcs             	   3 :	    601.26 ms		     87.15 %
    OptimizedLinearSpaceLcs       	   4 :	    689.91 ms		    100.00 %

When length of the sequences increase the Hirshberg algorithm (which uses linear
space) has an edge over the other scoretable algorithms (Smith-Waterman and
Wagner-Fischer). For longer different sequences is the clear winner.

### Very small different sequences (`TOTAL=30`, `LCS=2`)

    SmithWaterman                 	   0 :	      6.17 us		     40.80 %
    WagnerFischer                 	   1 :	      5.39 us		     35.68 %
    OptimizedHirschbergLinearSpace	   2 :	      7.95 us		     52.56 %
    OptimizedMyersLcs             	   3 :	     14.82 us		     98.00 %
    OptimizedLinearSpaceLcs       	   4 :	     15.13 us		    100.00 %

In case of small different sequences the Wagner-Fischer algorithm performs very
well because most of its (very simple) calculations are performed on the cache.

### Very small similar sequences (`TOTAL=30`, `LCS=20`)

    SmithWaterman                 	   0 :	      6.28 us		     73.83 %
    WagnerFischer                 	   1 :	      5.72 us		     67.23 %
    OptimizedHirschbergLinearSpace	   2 :	      8.51 us		    100.00 %
    OptimizedMyersLcs             	   3 :	      3.63 us		     42.69 %
    OptimizedLinearSpaceLcs       	   4 :	      4.52 us		     53.18 %

The Myers algorithm is always first when the sequences are similar.

### Small similar sequences (`TOTAL=60`, `LCS=40`)

    SmithWaterman                 	   0 :	     21.51 us		     72.52 %
    WagnerFischer                 	   1 :	     19.29 us		     65.05 %
    OptimizedHirschbergLinearSpace	   2 :	     29.66 us		    100.00 %
    OptimizedMyersLcs             	   3 :	     11.40 us		     38.44 %
    OptimizedLinearSpaceLcs       	   4 :	     13.78 us		     46.47 %

The linear space Myers algorithm perform a little better now than with
smaller sequences.

### Small different sequences (`TOTAL=60`, `LCS=4`)

    SmithWaterman                 	   0 :	     22.92 us		     33.70 %
    WagnerFischer                 	   1 :	     19.14 us		     28.14 %
    OptimizedHirschbergLinearSpace	   2 :	     28.23 us		     41.50 %
    OptimizedMyersLcs             	   3 :	     57.46 us		     84.45 %
    OptimizedLinearSpaceLcs       	   4 :	     68.03 us		    100.00 %



### Conclusions

* __Wagner-Fischer__ is very fast for different sequences but uses quadratic
memory so it quickly becomes impractical when the length of the sequences
grows;
* __Myers__ is fast when the sequences are similar but uses a lot of memory
if they differ (grows quadratically with the maximum distance between matches);
* __Hirschberg__ is not particularly fast but it is stable and uses very little
memory (grows linearly with the length of the smaller sequence). It performs
better than the Myers algorithms when the sequences are different.
* __Linear Space Myers__ is very fast if the sequences are similar but slow
down considerably if they differ.

|           | few elements (<50)    | many elements          |
| --------- | --------------------- | ---------------------- |
| similar   | Myers                 | Linear Space Myers     |
| different | Wagner-Fischer        | Hirschberg             |


## Bibliography

* [Wikipedia: Longest Common Subsequence problem]
(https://en.wikipedia.org/wiki/Longest_common_subsequence_problem)
* [Neil Fraser: Diff Strategies]
(https://neil.fraser.name/writing/diff/)
* [Wikipedia: Smith-Waterman Algorithm]
(https://en.wikipedia.org/wiki/Smith%E2%80%93Waterman_algorithm)
* [Wikipedia: Wagner Fischer Algorithm]
(https://en.wikipedia.org/wiki/Wagner%E2%80%93Fischer_algorithm)
* [Dynamic Programming]
(https://en.wikipedia.org/wiki/Dynamic_programming)
* [An O(ND) Difference Algorithm and Its Variations, Myers 1986 (PDF)]
(https://neil.fraser.name/software/diff_match_patch/myers.pdf)
* [Wikipedia: Levenshtein Distance]
(https://en.wikipedia.org/wiki/Levenshtein_distance)
