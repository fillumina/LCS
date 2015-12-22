package com.fillumina.lcs.testutil;

import com.fillumina.lcs.helper.LcsLength;
import com.fillumina.lcs.helper.LcsList;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * A test suite that checks the returned LCS length.
 *
 * @author Francesco Illuminati
 */
public abstract class AbstractLcsLengthTest extends AbstractLcsTest {

    public abstract LcsLength getLcsLengthAlgorithm();

    @Override
    public LcsList getLcsAlgorithm() {
        return getLcsLengthAlgorithm();
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
        final LcsLength algorithm = getLcsLengthAlgorithm();
        assertEquals(lcs, algorithm.lcsLength(seqGen.getArrayA(),
                seqGen.getArrayB()));
    }

    private void countLcs(String a, String b, int expectedLcs) {
        final Character[] arrayA = ConversionHelper.toArray(a);
        final Character[] arrayB = ConversionHelper.toArray(b);
        final LcsLength algorithm = getLcsLengthAlgorithm();
        assertEquals(expectedLcs, algorithm.lcsLength(arrayA, arrayB));
    }

    @Test//(timeout = 2_000L)
    public void shouldPassVeryLongTest() {
        randomLcs(6000, 5000, 1);
    }
}
