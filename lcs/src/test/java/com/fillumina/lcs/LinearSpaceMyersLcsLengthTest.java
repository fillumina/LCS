package com.fillumina.lcs;

import com.fillumina.lcs.testutil.RandomSequenceGenerator;
import java.util.List;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class LinearSpaceMyersLcsLengthTest {

    @Test
    public void shouldReturnTheRightLcsValue() {
        long seed = System.nanoTime();
        checkLcsLength(600, 600, seed);
        checkLcsLength(600, 500, seed);
        checkLcsLength(600, 50, seed);
        checkLcsLength(600, 5, seed);
        checkLcsLength(600, 1, seed);
        checkLcsLength(600, 0, seed);
        checkLcsLength(0, 0, seed);
    }

    private void checkLcsLength(int total, int lcs, long seed) {
        RandomSequenceGenerator generator =
                new RandomSequenceGenerator(total, lcs, seed);
        List<Integer> lcsList = generator.getLcs();
        List<Integer> a = generator.getA();
        List<Integer> b = generator.getB();
        final LinearSpaceMyersLcsLength myLcsLength =
                new LinearSpaceMyersLcsLength(a, b);
        assertEquals(lcs, myLcsLength.getLcs());
    }
}
