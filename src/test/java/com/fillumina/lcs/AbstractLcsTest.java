package com.fillumina.lcs;

import org.junit.Test;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public abstract class AbstractLcsTest extends AbstractLcsTestExecutor {

    @Test(timeout = 500L)
    public void shouldWorkHUMAN() {
        lcs("HUMAN", "CHIMPANZEE")
                .assertResult("HMAN");
    }

    @Test(timeout = 500L)
    public void shouldWorkABCABBA() {
        lcs("ABCABBA", "CBABAC")
                .assertResult("CABA", "BABA", "CBBA");
    }

    @Test(timeout = 500L)
    public void shouldWorkPYTHON() {
        lcs("PYTHON", "PONY")
                .assertResult("PON");
    }

    @Test(timeout = 500L)
    public void shouldWorkSPRINGTIME() {
        lcs("SPRINGTIME", "PIONEER")
                .assertResult("PINE");
    }

    @Test(timeout = 500L)
    public void shouldWorkHORSEBACK() {
        lcs("HORSEBACK", "SNOWFLAKE")
                .assertResult("OAE", "OAK", "SAK");
    }

    @Test(timeout = 500L)
    public void shouldWorkMAELSTROM() {
        lcs("MAELSTROM", "BECALM")
                .assertResult("ELM", "ALM");
    }

    @Test(timeout = 500L)
    public void shouldWorkHEROICALLY() {
        lcs("HEROICALLY", "SCHOLARLY")
                .assertResult("HOLLY", "HOALY");
    }

    @Test(timeout = 500L)
    public void shouldReturnAnEmptyString() {
        lcs("ABCDEF", "GHIJKLMN")
                .assertResult("");
    }

    @Test//(timeout = 500L)
    public void shouldReturnTheOnlyMatchAtBeginning() {
        lcs("ABCDEF", "A")
                .assertResult("A");
    }

    @Test(timeout = 500L)
    public void shouldReturnTheOnlyMatchAtEnd() {
        lcs("ABCDEF", "F")
                .assertResult("F");
    }

    @Test(timeout = 500L)
    public void shouldReturnTheOnlyMatchAtMiddle() {
        lcs("ABCDEF", "C")
                .assertResult("C");
    }

    @Test(timeout = 500L)
    public void shouldReturnEmptyResultForEmptyList() {
        lcs("ABCDEF", "")
                .assertResult("");
    }

    @Test(timeout = 500L)
    public void shouldReturnTheOnlyMatchAtBeginningReversed() {
        lcs("A", "ABCDEF")
                .assertResult("A");
    }

    @Test(timeout = 500L)
    public void shouldReturnTheOnlyMatchAtEndReversed() {
        lcs("F", "ABCDEF")
                .assertResult("F");
    }

    @Test(timeout = 500L)
    public void shouldReturnTheOnlyMatchAtMiddleReversed() {
        lcs("C", "ABCDEF")
                .assertResult("C");
    }

    @Test(timeout = 500L)
    public void shouldReturnEmptyResultForEmptyListReversed() {
        lcs("", "ABCDEF")
                .assertResult("");
    }

    @Test(timeout = 500L)
    public void shouldReturnEmptyListForBothEmptyList() {
        lcs("", "")
                .assertResult("");
    }

    @Test(timeout = 500L)
    public void shouldGetTheLeftDiagonal() {
        lcs("123AAAAAAA", "123BBBBBBB")
                .assertResult("123");
    }

    @Test(timeout = 500L)
    public void shouldGetTheRightDiagonal() {
        lcs("AAAAAAA123", "BBBBBBB123")
                .assertResult("123");
    }

    @Test(timeout = 500L)
    public void shouldGetTheBothEndsDiagonals() {
        lcs("123AAAAAAA123", "123BBBBBBB123")
                .assertResult("123123");
    }
}
