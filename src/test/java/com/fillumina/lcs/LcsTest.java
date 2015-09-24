package com.fillumina.lcs;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
@RunWith(Parameterized.class)
public class LcsTest {

    private final LcsCountingTestExecutor testExecutor;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
            {new LcsCountingTestExecutor() {

                @Override
                protected Lcs<Character> getLcs() {
                    return new RecursiveLcs<Character>() {

                        @Override
                        public List<Character> lcs(List<Character> xs,
                                List<Character> ys) {
                            count(xs, ys);
                            return super.lcs(xs, ys);
                        }
                    };
                }
            }},
            {new LcsCountingTestExecutor() {

                @Override
                protected Lcs<Character> getLcs() {
                    return new OptimizedRecursiveLcs<Character>() {

                        @Override
                        public List<Character> lcs(List<Character> xs,
                                List<Character> ys) {
                            count(xs, ys);
                            return super.lcs(xs, ys);
                        }
                    };
                }
            }},
            {new LcsCountingTestExecutor() {

                @Override
                protected Lcs<Character> getLcs() {
                    return new MemoizedRecursiveLcs<Character>() {

                        @Override
                        public List<Character> lcs(List<Character> xs,
                                List<Character> ys) {
                            count(xs, ys);
                            return super.lcs(xs, ys);
                        }
                    };
                }
            }}
        });
    }

    public LcsTest(LcsCountingTestExecutor testExecutor) {
        this.testExecutor = testExecutor;
    }

    @Test
    public void shouldGetTheRightResult() {
        testExecutor.lcs("HUMAN", "CHIMPANZEE").assertResult("HMAN");
    }

}
