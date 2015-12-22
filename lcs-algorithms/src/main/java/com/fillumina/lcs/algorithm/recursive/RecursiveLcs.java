package com.fillumina.lcs.algorithm.recursive;

import com.fillumina.lcs.helper.LcsList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * The simplest LCS algorithm. It is based on recursion so it's quite
 * impractical (it's time is exponential) but it offers a very concise and
 * clear implementation.
 *
 * @see MomoizedRecursiveLcs
 * @author Francesco Illuminati
 */
public class RecursiveLcs implements LcsList {

    @Override
    public <T> List<T> lcs(T[] a, T[] b) {
        return recursiveLcs(a, a.length, b, b.length).asList();
    }

    @SuppressWarnings("unchecked")
    <T> Stack<T> recursiveLcs(T[] a, int n, T[] b, int m) {
        if (n == 0 || m == 0) {
            return (Stack<T>) Stack.NULL;
        }

        T x = a[n-1];
        T y = b[m-1];

        if (Objects.equals(x, y)) {
            return recursiveLcs(a, n-1, b, m-1).push(x);
        } else {
            return longest(recursiveLcs(a, n, b, m-1), recursiveLcs(a, n-1, b, m));
        }
    }

    static <T> Stack<T> longest(Stack<T> a, Stack<T> b) {
        return (a.size() > b.size() ? a : b);
    }

    static class Stack<T> {
        static final Stack<?> NULL = new Stack<Object>(null, null, 0) {
            @Override
            Stack<Object> push(Object e) {
                return new Stack<>(e);
            }
        };

        private final T value;
        private final Stack<T> next;
        private final int size;

        public Stack(T value) {
            this(value, null, 1);
        }

        private Stack(T value, Stack<T> next, int size) {
            this.value = value;
            this.next = next;
            this.size = size;
        }

        public int size() {
            return size;
        }

        Stack<T> push(T e) {
            return new Stack<>(e, this, size + 1);
        }

        @SuppressWarnings("unchecked")
        List<T> asList() {
            if (this == NULL) {
                return Collections.<T>emptyList();
            }
            final T[] array = (T[]) new Object[size];
            int index = size;
            Stack<T> current = this;
            while (current != null) {
                index--;
                array[index] = current.value;
                current = current.next;
            }
            return Arrays.asList(array);
        }
    }
}
