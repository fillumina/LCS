package com.fillumina.lcs.recursive;

import com.fillumina.lcs.testutil.AbstractLcsTest;
import java.util.List;
import org.junit.Test;
import com.fillumina.lcs.Lcs;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class RecursiveLcsTest extends AbstractLcsTest {

    @Override
    public Lcs getLcsAlgorithm() {
        return new RecursiveLcs() {

            @Override
            public <T> List<? extends T> lcs(
                    List<? extends T> a, int n,
                    List<? extends T> b, int m) {
                count(a, b);
                return super.lcs(a, n, b, m);
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
     * memory hungry and time consuming.
     */
    @Test(timeout = 2_000L)
    @Override
    public void shouldPassLengthTest() {
        randomLcs(10, 5, 1);
    }

    @Test(timeout = 10_000L)
    @Override
    public void shouldPerformRandomLengthTests() {
        randomLcs(7, 4, 100);
    }
}
