package com.fillumina.lcs.scoretable;

import com.fillumina.lcs.testutil.AbstractLcsTest;
import static junit.framework.Assert.assertEquals;
import org.junit.Test;
import com.fillumina.lcs.helper.LcsList;

/**
 *
 * @author Francesco Illuminati 
 */
public class WagnerFischerLcsTest extends AbstractLcsTest {

    @Override
    public LcsList getLcsAlgorithm() {
        return new WagnerFischerLcs();
    }

    @Test(timeout = 1_000L)
    public void shouldWorkKitten() {
        lcs("SITTING", "KITTEN")
                .assertResult("ITTN");
    }

    @Test(timeout = 1_000L)
    public void shouldWorkSaturday() {
        lcs("SUNDAY", "SATURDAY")
                .assertResult("SUDAY");
    }

    @Test(timeout = 1_000L)
    public void shouldWorkAvery() {
        lcs("AVERY", "GARVEY")
                .assertResult("AVEY");
    }

    @Test
    public void shouldReturnTheMinimum() {
        assertEquals(1, WagnerFischerLcs.minIndex(1, 2, 3));
        assertEquals(2, WagnerFischerLcs.minIndex(2, 1, 3));
        assertEquals(3, WagnerFischerLcs.minIndex(2, 3, 1));
        assertEquals(3, WagnerFischerLcs.minIndex(3, 2, 1));
        assertEquals(1, WagnerFischerLcs.minIndex(1, 3, 2));
        assertEquals(2, WagnerFischerLcs.minIndex(3, 1, 2));
    }
}
