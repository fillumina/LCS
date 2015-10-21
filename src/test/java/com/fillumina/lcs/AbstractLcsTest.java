package com.fillumina.lcs;

import org.junit.Test;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public abstract class AbstractLcsTest extends AbstractLcsTestExecutor {

    @Test
    public void shouldWorkHUMAN() {
        lcs("HUMAN", "CHIMPANZEE")
                .assertResult("HMAN");
    }

    @Test
    public void shouldWorkABCABBA() {
        lcs("ABCABBA", "CBABAC")
                .assertResult("CABA", "BABA", "CBBA");
    }

    @Test
    public void shouldWorkPYTHON() {
        lcs("PYTHON", "PONY")
                .assertResult("PON");
    }

    @Test
    public void shouldWorkSPRINGTIME() {
        lcs("SPRINGTIME", "PIONEER")
                .assertResult("PINE");
    }

    @Test
    public void shouldWorkHORSEBACK() {
        lcs("HORSEBACK", "SNOWFLAKE")
                .assertResult("OAE", "OAK", "SAK");
    }

    @Test
    public void shouldWorkMAELSTROM() {
        lcs("MAELSTROM", "BECALM")
                .assertResult("ELM", "ALM");
    }

    @Test
    public void shouldWorkHEROICALLY() {
        lcs("HEROICALLY", "SCHOLARLY")
                .assertResult("HOLLY", "HOALY");
    }

    @Test
    public void shouldReturnAnEmptyString() {
        lcs("ABCDEF", "GHIJKLMN")
                .assertResult("");
    }

    @Test
    public void shouldReturnTheOnlyMatchAtBeginning() {
        lcs("ABCDEF", "A")
                .assertResult("A");
    }

    @Test
    public void shouldReturnTheOnlyMatchAtEnd() {
        lcs("ABCDEF", "F")
                .assertResult("F");
    }

    @Test
    public void shouldReturnTheOnlyMatchAtMiddle() {
        lcs("ABCDEF", "C")
                .assertResult("C");
    }

    @Test
    public void shouldReturnEmptyResultForEmptyList() {
        lcs("ABCDEF", "")
                .assertResult("");
    }

    @Test
    public void shouldReturnTheOnlyMatchAtBeginningReversed() {
        lcs("A", "ABCDEF")
                .assertResult("A");
    }

    @Test
    public void shouldReturnTheOnlyMatchAtEndReversed() {
        lcs("F", "ABCDEF")
                .assertResult("F");
    }

    @Test
    public void shouldReturnTheOnlyMatchAtMiddleReversed() {
        lcs("C", "ABCDEF")
                .assertResult("C");
    }

    @Test
    public void shouldReturnEmptyResultForEmptyListReversed() {
        lcs("", "ABCDEF")
                .assertResult("");
    }

    @Test
    public void shouldReturnEmptyListForBothEmptyList() {
        lcs("", "")
                .assertResult("");
    }
}
