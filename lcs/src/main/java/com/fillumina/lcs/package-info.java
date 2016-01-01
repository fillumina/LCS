/**
 * This packages presents some optimized algorithms that solve the
 * Longest Common Sub-sequence problem for sequences with the following
 * characteristics:
 * <ul>
 * <li><b>Length</b> small sequences have less than about 50 elements;</li>
 * <li><b>Similarity</b> the sequences are similar if they share most of their
 * elements (more than 50%).</li>
 * </ul>
 *
 * <p>
 * Performance tests have shown that the Myers' algorithms are more suited
 * for similar sequences (the linear space being used for the longer sequences),
 * while the score-table algorithms are independent from the similarity and so
 * perform well when the sequences are almost different.
 * </p>
 *
 * <table border='1'>
 * <tr>
 * <th></th>
 * <th>few elements</th>
 * <th>many elements</th>
 * </tr>
 * <tr>
 * <th>similar</th>
 * <td>Myers</td>
 * <td>Linear Space Myers</td>
 * </tr>
 * <tr>
 * <th>differnt</th>
 * <td>Wagner-Fisher</td>
 * <td>Hirshberg</td>
 * </tr>
 * </table>
 *
 * @author Francesco Illuminati
 */
package com.fillumina.lcs;
