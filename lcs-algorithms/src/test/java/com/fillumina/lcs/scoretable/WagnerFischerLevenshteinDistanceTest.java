package com.fillumina.lcs.scoretable;

import com.fillumina.lcs.testutil.CharacterLcsHelper;
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

        List<Character> a = CharacterLcsHelper.toList("tuesday");
        List<Character> b = CharacterLcsHelper.toList("thursday");

        assertEquals(2, algo.distance(a, b));
    }

    @Test
    public void testFarDistance() {
        WagnerFischerLevenshteinDistance algo =
                new WagnerFischerLevenshteinDistance();

        List<Character> a = CharacterLcsHelper.toList("monday");
        List<Character> b = CharacterLcsHelper.toList("saturday");

        assertEquals(5, algo.distance(a, b));
    }
}
