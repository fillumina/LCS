package com.fillumina.lcs.algorithm.recursive;

import com.fillumina.lcs.algorithm.recursive.RecursiveLcs.Stack;
import com.fillumina.lcs.helper.LcsList;
import com.fillumina.lcs.testutil.AbstractLcsTest;
import java.util.Arrays;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author Francesco Illuminati
 */
public class RecursiveLcsTest extends AbstractLcsTest {

    @Override
    public LcsList getLcsAlgorithm() {
        return new RecursiveLcs() {

            @Override
            <T> Stack<T> recursiveLcs(T[] a, int n, T[] b, int m) {
                count(a, b);
                return super.recursiveLcs(a, n, b, m);
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

    @Test
    public void shouldModifyTheOriginalNotAffectTheOther()
            throws CloneNotSupportedException {
        Stack<Character> stack = new Stack<>('h')
                .push('e')
                .push('l')
                .push('l')
                .push('o');

        final Stack<Character> appended = stack.push('!');

        assertEquals(5, stack.size());
        assertEquals(6, appended.size());
    }

    @Test
    public void shouldModifyTwoStacks()
            throws CloneNotSupportedException {
        Stack<Character> a = new Stack<>('c')
                .push('a');

        final Stack<Character> cam = a.push('m');
        final Stack<Character> cat = a.push('t');

        assertEquals(Arrays.asList('c', 'a', 't'), cat.asList());
        assertEquals(Arrays.asList('c', 'a', 'm'), cam.asList());
    }

    @Test
    public void shouldReturnSizeOneForSingleton() {
        Stack<Character> stack = new Stack<>('h');

        assertEquals(1, stack.size());
    }

    @Test
    public void shouldReturnTheList() {
        Stack<Character> stack = new Stack<>('h')
                .push('e')
                .push('l')
                .push('l')
                .push('o');

        assertEquals(Arrays.asList('h', 'e', 'l', 'l', 'o'),
                stack.asList());
    }

    @Test
    public void shouldReturnTheSingleton() {
        Stack<Character> stack = new Stack<>('o');

        assertEquals(Arrays.asList('o'), stack.asList());
    }

    @Test
    public void shouldReturnNULL() {
        assertTrue(Stack.NULL.asList().isEmpty());
    }

}
