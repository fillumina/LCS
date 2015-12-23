package com.fillumina.lcs.algorithm.scoretable;

import com.fillumina.lcs.testutil.Converter;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Francesco Illuminati
 */
public class WagnerFischerLevenshteinDistanceTest {

    @Test
    public void testCloseDistance() {
        WagnerFischerLevenshteinDistance algo =
                new WagnerFischerLevenshteinDistance();

        List<Character> a = Converter.toList("tuesday");
        List<Character> b = Converter.toList("thursday");

        assertEquals(2, algo.distance(a, b));
    }

    @Test
    public void testFarDistance() {
        WagnerFischerLevenshteinDistance algo =
                new WagnerFischerLevenshteinDistance();

        List<Character> a = Converter.toList("monday");
        List<Character> b = Converter.toList("saturday");

        assertEquals(5, algo.distance(a, b));
    }
}
