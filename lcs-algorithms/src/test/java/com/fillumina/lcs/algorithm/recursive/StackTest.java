package com.fillumina.lcs.algorithm.recursive;

import java.util.Arrays;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class StackTest {

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

        assertEquals(Arrays.asList('c', 'a', 't'), cat.toList());
        assertEquals(Arrays.asList('c', 'a', 'm'), cam.toList());
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
                stack.toList());
    }

    @Test
    public void shouldReturnTheSingleton() {
        Stack<Character> stack = new Stack<>('o');

        assertEquals(Arrays.asList('o'), stack.toList());
    }

    @Test
    public void shouldReturnNULL() {
        assertTrue(Stack.emptyStack().toList().isEmpty());
    }

}
