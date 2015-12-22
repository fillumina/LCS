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


## Modules

The project is divided in 4 (maven) modules:

1. `lcs-helper` defines some common interfaces
2. `lcs-test-util` contains a test suit to make it sure that every
algorithm is working right.
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
* [Smith-Waterman]
(lcs-algorithms/src/main/java/com/fillumina/lcs/algorithm/scoretable/SmithWatermanLcs.java)
* [Wagner-Fisher]
(lcs-algorithms/src/main/java/com/fillumina/lcs/algorithm/scoretable/WagnerFischerLcs.java)
* [Linear space Hirschberg]
(lcs-algorithms/src/main/java/com/fillumina/lcs/algorithm/hirschberg/HirschbergLinearSpaceAlgorithmLcs.java)
* [Myers]
(lcs-algorithms/src/main/java/com/fillumina/lcs/algorithm/myers/MyersLcs.java)
* [Linear Space Myers]
(lcs-algorithms/src/main/java/com/fillumina/lcs/algorithm/myers/linearspace/LinearSpaceMyersLcsSolver.java)

There is also a very fast implementation of the Levenshtein distance algorithm.
The module containing the optimized algorithms will be soon released on
maven central.


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
