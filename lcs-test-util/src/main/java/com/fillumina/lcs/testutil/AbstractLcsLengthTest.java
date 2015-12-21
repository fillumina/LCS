package com.fillumina.lcs.testutil;

import java.util.List;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.fillumina.lcs.helper.LcsList;
import com.fillumina.lcs.helper.LcsLength;

/**
 * A suite of test that tests the returned LCS length.
 *
 * @author Francesco Illuminati 
 */
public abstract class AbstractLcsLengthTest extends AbstractLcsTest {

    public abstract <T extends LcsList & LcsLength> T getLcsSequenceGenerator();

    @Override
    public LcsList getLcsAlgorithm() {
        return getLcsSequenceGenerator();
    }

    @Test(timeout = 2000L)
    public void shouldLcsHUMAN() {
        countLcs("HUMAN", "CHIMPANZEE", 4);
    }

    @Test(timeout = 2000L)
    public void shouldLcsABCABBA() {
        countLcs("ABCABBA", "CBABAC", 4);
    }

    @Test(timeout = 2000L)
    public void shouldLcsPYTHON() {
        countLcs("PYTHON", "PONY", 3);
    }

    @Test(timeout = 2000L)
    public void shouldLcsSPRINGTIME() {
        countLcs("SPRINGTIME", "PIONEER", 4);
    }

    @Test(timeout = 2000L)
    public void shouldLcsHORSEBACK() {
        countLcs("HORSEBACK", "SNOWFLAKE", 3);
    }

    @Test(timeout = 2000L)
    public void shouldLcsMAELSTROM() {
        countLcs("MAELSTROM", "BECALM", 3);
    }

    @Test(timeout = 2000L)
    public void shouldLcsHEROICALLY() {
        countLcs("HEROICALLY", "SCHOLARLY", 5);
    }

    @Test(timeout = 2000L)
    public void shouldLcs0ForAnEmptyString() {
        countLcs("ABCDEF", "GHIJKLMN", 0);
    }

    @Test(timeout = 2000L)
    public void shouldLcs1ForTheOnlyMatchAtBeginning() {
        countLcs("ABCDEF", "A", 1);
    }

    @Test(timeout = 2000L)
    public void shouldLcs1ForTheOnlyMatchAtEnd() {
        countLcs("ABCDEF", "F", 1);
    }

    @Test(timeout = 2000L)
    public void shouldLcs1ForTheOnlyMatchAtMiddle() {
        countLcs("ABCDEF", "C", 1);
    }

    @Test(timeout = 2000L)
    public void shouldLcsEmptyResultForEmptyList() {
        countLcs("ABCDEF", "", 0);
    }

    @Test(timeout = 2000L)
    public void shouldLcsTheOnlyMatchAtBeginningReversed() {
        countLcs("A", "ABCDEF", 1);
    }

    @Test(timeout = 2000L)
    public void shouldLcsTheOnlyMatchAtEndReversed() {
        countLcs("F", "ABCDEF", 1);
    }

    @Test(timeout = 2000L)
    public void shouldLcsTheOnlyMatchAtMiddleReversed() {
        countLcs("C", "ABCDEF", 1);
    }

    @Test(timeout = 2000L)
    public void shouldLcsEmptyResultForEmptyListReversed() {
        countLcs("", "ABCDEF", 0);
    }

    @Test(timeout = 2000L)
    public void shouldLcsEmptyListForBothEmptyList() {
        countLcs("", "", 0);
    }

    @Test(timeout = 2000L)
    public void shouldLcsTheLeftDiagonal() {
        countLcs("123AAAAAAA", "123BBBBBBB", 3);
    }

    @Test(timeout = 2000L)
    public void shouldLcsTheRightDiagonal() {
        countLcs("AAAAAAA123", "BBBBBBB123", 3);
    }

    @Test(timeout = 2000L)
    public void shouldLcsTheBothEndsDiagonals() {
        countLcs("123AAAAAAA123", "123BBBBBBB123", 6);
    }

    @Test(timeout = 2000L)
    public void shouldPassLengthTestSize() {
        final int tot = 600;
        final int lcs = 500;
        RandomSequenceGenerator seqGen = new RandomSequenceGenerator(tot, lcs);
        final LcsLength algorithm = getLcsSequenceGenerator();
        assertEquals(lcs, algorithm.lcsLength(seqGen.getA(), seqGen.getB()));
    }

    private void countLcs(String a, String b, int expectedLcs) {
        final List<Character> listA = CharacterLcsHelper.toList(a);
        final List<Character> listB = CharacterLcsHelper.toList(b);
        final LcsLength algorithm = getLcsSequenceGenerator();
        assertEquals(expectedLcs, algorithm.lcsLength(listA, listB));
    }

    @Test//(timeout = 2_000L)
    public void shouldPassVeryLongTest() {
        randomLcs(6000, 5000, 1);
    }
}
