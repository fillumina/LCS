package com.fillumina.lcs.myers;

import com.fillumina.lcs.testutil.AbstractLcsTest;
import com.fillumina.lcs.testutil.RandomSequenceGenerator;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Ignore;
import org.junit.Test;
import com.fillumina.lcs.ListLcs;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class OptimizedMyersLcsTest extends AbstractLcsTest {

    @Override
    public ListLcs getLcsAlgorithm() {
        return new OptimizedMyersLcs();
    }

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

        @SuppressWarnings("unchecked")
        List<Integer> lcsList = (List<Integer>) algo
                .lcs(generator.getA(), generator.getB());

        return algo.getMemCounter();
    }
}
