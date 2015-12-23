package com.fillumina.lcs.algorithm.recursive;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Specialized stack with 2 main features:
 * <ul>
 * <li>pushing a value into the static empty instance ({@link #EMPTY})
 *     creates a new instance containing the value.
 *     This is useful because the {@link RecursiveLcs}
 *     algorithm creates a lot of empty instances and only on some of them
 *     it pushes some actual elements.
 * <li>creates list from this stack very efficiently with {@link #toList() };
 * </ul>
 *
 * @author Francesco Illuminati
 */
class Stack<T> {
    static final Stack<?> EMPTY = new Stack<Object>(null, null, 0) {
        @Override
        Stack<Object> push(Object e) {
            return new Stack<>(e);
        }
    };

    @SuppressWarnings("unchecked")
    public static <T> Stack<T> emptyStack() {
        return (Stack<T>) EMPTY;
    }

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
    List<T> toList() {
        if (this == EMPTY) {
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
