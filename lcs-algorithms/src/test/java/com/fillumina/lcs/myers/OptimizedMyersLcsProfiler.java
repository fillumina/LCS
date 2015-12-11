package com.fillumina.lcs.myers;

import com.fillumina.lcs.testutil.RandomSequenceGenerator;
import java.util.List;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class OptimizedMyersLcsProfiler {

    public static void main(String[] args) {
        RandomSequenceGenerator generator =
                new RandomSequenceGenerator(60, 50);

        for (int i=0; i<1000; i++) {
            @SuppressWarnings("unchecked")
            List<? extends Integer> lcsList = new OptimizedMyersLcs()
                    .lcs(generator.getA(), generator.getB());
            if (lcsList == null) {
                throw new AssertionError();
            }
        }
    }
}
