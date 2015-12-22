package com.fillumina.lcs.testutil;

import java.util.List;
import org.junit.Test;

/**
 *
 * @author Francesco Illuminati
 */
public class RandomSequenceGeneratorTest {

    @Test
    public void checkLcsLength() {
        assertLenght(1, 1);
        assertLenght(10, 0);
        assertLenght(2, 1);
        assertLenght(5, 3);
        assertLenght(5, 5);
        assertLenght(50, 30);
        assertLenght(500, 300);
    }

    static void assertLenght(int total, int lcs) {
        new SequenceTester(total, lcs).checkLenght();
    }

    static class SequenceTester {
        private final int total, lcs;
        private final RandomSequenceGenerator generator;
        private final List<Integer> a, b;

        public SequenceTester(int total, int lcs) {
            this.total = total;
            this.lcs = lcs;

            this.generator = new RandomSequenceGenerator(total, lcs);
            this.a = generator.getA();
            this.b = generator.getB();
        }

        private void checkLenght() {
            int indexA = -1, indexB = -1;
            for (int value : generator.getLcs()) {
                indexA = getNext(a, indexA + 1, value);
                if (indexA == -1) {
                    throw new AssertionError("value " + value +
                            " not found in a");
                }

                indexB = getNext(b, indexB + 1, value);
                if (indexA == -1) {
                    throw new AssertionError("value " + value +
                            " not found in b");
                }
            }
        }

        private int getNext(List<Integer> list, int start, int value) {
            final int size = list.size();
            for (int i = start; i<size; i++) {
                if (value == a.get(i)) {
                    return i;
                }
            }
            return -1;
        }
    }
}
