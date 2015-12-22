package com.fillumina.lcs.algorithm.myers;

import com.fillumina.lcs.helper.LcsList;
import com.fillumina.lcs.testutil.AbstractLcsTest;
import com.fillumina.lcs.testutil.RandomSequenceGenerator;
import static org.junit.Assert.assertEquals;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Francesco Illuminati
 */
public class OptimizedMyersLcsTest extends AbstractLcsTest {

    @Override
    public LcsList getLcsAlgorithm() {
        return new OptimizedMyersLcs();
    }

    /**
     * Test the memory used by the Myers algorithm. The number
     * returned refers to the integer allocated, each one using 4 bytes.
     */
    @Ignore @Test
    public void shouldCheckMemory() {
        assertEquals(63, consumedMemory(6, 4));
        assertEquals(1251, consumedMemory(60, 40));
        // 340 Kb
        assertEquals(84411, consumedMemory(600, 400));
        // 2.8 Mb
        assertEquals(717_615, consumedMemory(600, 4));
        // 32 Mb
        assertEquals(8_044_011, consumedMemory(6_000, 4_000));
        // 280 Mb
        assertEquals(71_114_931, consumedMemory(6_000, 40));
    }

    private int consumedMemory(int len, int lcs) {
        RandomSequenceGenerator generator =
                new RandomSequenceGenerator(len, lcs);

        final OptimizedMyersLcsMemConsumption algo
                = new OptimizedMyersLcsMemConsumption();

        algo.lcs(generator.getArrayA(), generator.getArrayB());

        return algo.getMemCounter();
    }
}
