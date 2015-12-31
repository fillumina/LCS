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

1. realization of a fast LCS implementation (see `lcs` module)
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
2. `lcs-test-util` contains a test suit to make it sure that every
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

The main usage of the Longest Common Sub-sequence algorithms is to find out
what are the differences between two sequences that are known to be similar
(i.e. they share most of the elements in the sequence). This is the case
of the diff program that shows changes applied to a text or of programs that
searches for mutations on a DNA sequence. For this kind of usage the
Myers algorithm is the clear winner. The following table shows the results
of a performance test applied to the algorithms comparing two sequences of
600 elements with an LCS (number of equal elements) of 400. Consider
that the memory utilization of the not linear Myers algorithm grows
quadratically with the distance between the matches while the
linear space variant, although a bit slower, has a much smaller memory footprint
with the added advantage of being constant (it's independent from the
distance between the matches).
[AlgorithmPerformanceTest]
(lcs-algorithms/src/test/java/com/fillumina/lcs/algorithm/performance/AlgorithmsPerformanceTest.java)

    `TOTAL=600` and `LCS=400`:

    SmithWaterman                 	   0 :	      1.97 ms		     74.90 %
    WagnerFischer                 	   1 :	      2.14 ms		     81.50 %
    OptimizedHirschbergLinearSpace	   2 :	      2.63 ms		    100.00 %
    OptimizedMyersLcs             	   3 :	      0.85 ms		     32.56 %
    OptimizedLinearSpaceLcs       	   4 :	      1.00 ms		     38.28 %

When the length of the sequences grows the memory access become more costly and
the performances of the HirschbergLinearSpace starts to improve in respect to
the scoretable algorithms.

    `TOTAL=6000` and `LCS=4000`:

    SmithWaterman                 	   0 :	    251.23 ms		     61.32 %
    WagnerFischer                 	   1 :	    409.72 ms		    100.00 %
    OptimizedHirschbergLinearSpace	   2 :	    250.73 ms		     61.20 %
    OptimizedMyersLcs             	   3 :	     89.51 ms		     21.85 %
    OptimizedLinearSpaceLcs       	   4 :	     92.15 ms		     22.49 %

When the sequences are different and share very few elements the results
changes dramatically: the scoretable algorithms (Smith-Waterman and
Wagner-Fischer) are clearly faster as shown by the following table (but their
memory usage is quadratic with the length of the sequences).
[AlgorithmPerformanceTest]
(lcs-algorithms/src/test/java/com/fillumina/lcs/algorithm/performance/AlgorithmsPerformanceTest.java)

    `TOTAL=600` and `LCS=4`:

    SmithWaterman                 	   0 :	      2.43 ms		     36.44 %
    WagnerFischer                 	   1 :	      2.22 ms		     33.35 %
    OptimizedHirschbergLinearSpace	   2 :	      2.76 ms		     41.30 %
    OptimizedMyersLcs             	   3 :	      6.02 ms		     90.11 %
    OptimizedLinearSpaceLcs       	   4 :	      6.68 ms		    100.00 %

The performances remain stable when the tests are performed with the length
of the sequences increase by an order of magnitude. The HirshbergLinearSpace
gains even more speed and considering the quadratic algorithms consumes a lot
of memory it becomes more interesting as the length of the sequences increases.

    `TOTAL=6000` and `LCS=40`:

    SmithWaterman                 	   0 :	    218.84 ms		     31.72 %
    WagnerFischer                 	   1 :	    293.51 ms		     42.54 %
    OptimizedHirschbergLinearSpace	   2 :	    238.50 ms		     34.57 %
    OptimizedMyersLcs             	   3 :	    601.26 ms		     87.15 %
    OptimizedLinearSpaceLcs       	   4 :	    689.91 ms		    100.00 %

To summarize:

    * __For similar sequences:__ the Linear Space Myers algorithm is very fast
      (only slightly slower than the direct Myers
      algorithm) and memory efficient. For small sequences the simple
      Myers algorithm might be preferred but be warned that its memory usage
      grows quite fast with the distance between matches.

    * __For mostly different sequences:__ the Linear Space Hirschberg algorithm
      is a bit slower for few elements but becomes more and more efficient as
      the length of the sequences increases plus it uses much less memory than
      the quadratic space algorithms.

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
