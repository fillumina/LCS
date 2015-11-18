package com.fillumina.lcs.testutil;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import com.fillumina.lcs.Lcs;
import java.util.Objects;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public abstract class AbstractLcsTest extends AbstractLcsTestExecutor {

    public void randomLcs(int len, int lcs, int iterations) {
        @SuppressWarnings("unchecked")
        final Lcs<Integer> algorithm = (Lcs<Integer>)getLcsAlgorithm();
        for (int i=0; i<iterations; i++) {
            RandomSequenceGenerator generator =
                    new RandomSequenceGenerator(len,lcs);

            List<Integer> lcsList = algorithm
                    .lcs(generator.getA(), generator.getB());

            final List<Integer> expectedLcs = generator.getLcs();
            final int size = expectedLcs.size();
            StringBuilder buf = new StringBuilder();
            for (int j=0; j<size; j++) {
                final Integer expected = expectedLcs.get(j);
                final Integer result = lcsList.get(j);
                if (!Objects.equals(expected, result)) {
                    buf.append("lists differs at index ").append(j).
                            append(" expected=").append(expected).
                            append(", result=").append(result).append("\n");
                }
            }
            if (buf.length() != 0) {
                throw new AssertionError("iteration " + i + " seed: " +
                    generator.getSeed() +"L\n" +
                        buf.toString());
            }
        }
    }

    @Test
    public void shouldAllowNullValueInTheList() {
        List<Character> a = new ArrayList<>();
        List<Character> b = new ArrayList<>();
        a.add(null);
        b.add(null);
        @SuppressWarnings("unchecked")
        List<Character> solution =
                ((Lcs<Character>)getLcsAlgorithm()).lcs(a, b);
        assertEquals(a, solution);
    }

    @Test(timeout = 10_000L)
    public void shouldPerformRandomLengthTests() {
        randomLcs(10, 3, 1);
    }

    @Test
    public void shouldSelectTheOptimalResult() {
        lcs("CXCDEFX", "CDEFX").assertResult("CDEFX");
    }

    @Test
    public void shouldSelectTheOptimalResultAvoidingPreoptimizations() {
        lcs("_CXCDEFX_", "=CDEFX=").assertResult("CDEFX");
    }

    @Test(timeout = 1_000L)
    public void shouldGetTheOnlyElement() {
        lcs("A", "A").assertResult("A");
    }

    @Test(timeout = 1_000L)
    public void shouldWorkHUMAN() {
        lcs("HUMAN", "CHIMPANZEE")
                .assertResult("HMAN");
    }

    @Test(timeout = 1_000L)
    public void shouldWorkABCABBA() {
        lcs("ABCABBA", "CBABAC")
                .assertResult("CABA", "BABA", "CBBA");
    }

    @Test(timeout = 1_000L)
    public void shouldWorkPYTHON() {
        lcs("PYTHON", "PONY")
                .assertResult("PON");
    }

    @Test(timeout = 1_000L)
    public void shouldWorkSPRINGTIME() {
        lcs("SPRINGTIME", "PIONEER")
                .assertResult("PINE");
    }

    @Test(timeout = 1_000L)
    public void shouldWorkHORSEBACK() {
        lcs("HORSEBACK", "SNOWFLAKE")
                .assertResult("OAE", "OAK", "SAK");
    }

    @Test(timeout = 1_000L)
    public void shouldWorkMAELSTROM() {
        lcs("MAELSTROM", "BECALM")
                .assertResult("ELM", "ALM");
    }

    @Test(timeout = 1_000L)
    public void shouldWorkHEROICALLY() {
        lcs("HEROICALLY", "SCHOLARLY")
                .assertResult("HOLLY", "HOALY");
    }

    @Test(timeout = 1_000L)
    public void shouldReturnAnEmptyString() {
        lcs("ABCDEF", "GHIJKLMN")
                .assertResult("");
    }

    @Test//(timeout = 1_000L)
    public void shouldReturnTheOnlyMatchAtBeginning() {
        lcs("ABCDEF", "A")
                .assertResult("A");
    }

    @Test(timeout = 1_000L)
    public void shouldReturnTheOnlyMatchAtEnd() {
        lcs("ABCDEF", "F")
                .assertResult("F");
    }

    @Test(timeout = 1_000L)
    public void shouldReturnTheOnlyMatchAtMiddle() {
        lcs("ABCDEF", "C")
                .assertResult("C");
    }

    @Test(timeout = 1_000L)
    public void shouldReturnEmptyResultForEmptyList() {
        lcs("ABCDEF", "")
                .assertResult("");
    }

    @Test//(timeout = 1_000L)
    public void shouldReturnTheOnlyMatchAtBeginningReversed() {
        lcs("A", "ABCDEF")
                .assertResult("A");
    }

    @Test//(timeout = 1_000L)
    public void shouldReturnTheOnlyMatchAtEndReversed() {
        lcs("F", "ABCDEF")
                .assertResult("F");
    }

    @Test//(timeout = 1_000L)
    public void shouldReturnTheOnlyMatchAtEndReversedOdd() {
        lcs("E", "ABCDE")
                .assertResult("E");
    }

    @Test(timeout = 1_000L)
    public void shouldReturnTheOnlyMatchAtMiddleReversed() {
        lcs("C", "ABCDEF")
                .assertResult("C");
    }

    @Test(timeout = 1_000L)
    public void shouldReturnEmptyResultForEmptyListReversed() {
        lcs("", "ABCDEF")
                .assertResult("");
    }

    @Test(timeout = 1_000L)
    public void shouldReturnEmptyListForBothEmptyList() {
        lcs("", "")
                .assertResult("");
    }

    @Test(timeout = 1_000L)
    public void shouldGetTheLeftDiagonal() {
        lcs("123AAAAAAA", "123BBBBBBB")
                .assertResult("123");
    }

    @Test(timeout = 1_000L)
    public void shouldGetTheRightDiagonal() {
        lcs("AAAAAAA123", "BBBBBBB123")
                .assertResult("123");
    }

    @Test(timeout = 1_000L)
    public void shouldGetTheBothEndsDiagonals() {
        lcs("123AAA123", "123BBB123")
                .assertResult("123123");
    }

    @Test(timeout = 1_000L)
    public void shouldGetTheBothEndsPlusMiddleDiagonals() {
        lcs("123A4A123", "123B4B123")
                .assertResult("1234123");
    }

    @Test(timeout = 1_000L)
    public void shouldGetFullDiagonals() {
        lcs("1234567890", "1234567890")
                .assertResult("1234567890");
    }

    @Test(timeout = 1_000L)
    public void shouldGetOuterExtremities() {
        lcs("A123", "456A")
                .assertResult("A");
    }

    @Test(timeout = 1_000L)
    public void shouldGetInnerExtremities() {
        lcs("123A", "A456")
                .assertResult("A");
    }

    @Test(timeout = 1_000L)
    public void shouldGetOuterExtremitiesInverted() {
        lcs("A000", "000A")
                .assertResult("000");
    }

    @Test(timeout = 1_000L)
    public void shouldGetInnerExtremitiesInverted() {
        lcs("000A", "A000")
                .assertResult("000");
    }

    /**
     * @see <a href="https://neil.fraser.name/writing/diff/">
     * Myers algorithm problems
     * </a>
     */
    @Test(timeout = 1_000L)
    public void shouldPassAccuracyTest() {
        lcs("XAXCXABC", "ABCY")
                .assertResult("ABC");
    }

    @Test(timeout = 1_000L)
    public void shouldPassLengthTest() {
        randomLcs(60, 10, 1);
    }
}
