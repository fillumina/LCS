package com.fillumina.lcs.algorithm.scoretable;

import com.fillumina.lcs.algorithm.scoretable.WagnerFischerLevenshteinDistance;
import com.fillumina.lcs.testutil.ConversionHelper;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Francesco Illuminati 
 */
public class WagnerFischerLevenshteinDistanceTest {

    @Test
    public void testCloseDistance() {
        WagnerFischerLevenshteinDistance algo =
                new WagnerFischerLevenshteinDistance();

        List<Character> a = ConversionHelper.toList("tuesday");
        List<Character> b = ConversionHelper.toList("thursday");

        assertEquals(2, algo.distance(a, b));
    }

    @Test
    public void testFarDistance() {
        WagnerFischerLevenshteinDistance algo =
                new WagnerFischerLevenshteinDistance();

        List<Character> a = ConversionHelper.toList("monday");
        List<Character> b = ConversionHelper.toList("saturday");

        assertEquals(5, algo.distance(a, b));
    }
}
