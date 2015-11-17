package com.fillumina.lcs.recursive;

import com.fillumina.lcs.testutil.AbstractLcsTest;
import com.fillumina.lcs.testutil.RandomSequenceGenerator;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import com.fillumina.lcs.Lcs;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class RecursiveLcsTest extends AbstractLcsTest {

    @Override
    protected Lcs<?> getLcsAlgorithm() {
        return new RecursiveLcs<Character>() {

            @Override
            public List<Character> lcs(List<Character> xs, int n,
                    List<Character> ys, int m) {
                count(xs, ys);
                return super.lcs(xs, n, ys, m);
            }
        };
    }

    @Test
    public void shouldGetTheRightResult() {
        lcs("HUMAN", "CHIMPANZEE")
                .assertResult("HMAN")
                .assertNumberOfCalls(2221);
    }

    @Test(timeout = 1_000L)
    public void shouldNotLoop() {
    }

    /**
     * Cannot use the standard test because this algorithm is VERY
     * memory hungry and time consuming (it's using recursion heavily).
     */
    @Test(timeout = 2_000L)
    @Override
    public void shouldPassLengthTest() {
        RandomSequenceGenerator generator =
                new RandomSequenceGenerator(10,5);
        @SuppressWarnings("unchecked")
        List<Integer> lcsList = ((Lcs)getLcsAlgorithm())
                .lcs(generator.getA(), generator.getB());
        assertEquals(generator.getLcs(), lcsList);
    }

    @Test(timeout = 10_000L)
    @Override
    public void shouldPerformRandomLengthTests() {
        randomLcs(7, 4, 100);
    }
}
