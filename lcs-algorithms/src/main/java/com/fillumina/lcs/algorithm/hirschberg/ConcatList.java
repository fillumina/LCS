package com.fillumina.lcs.algorithm.hirschberg;

import java.util.AbstractList;

/** A list optimized for concatenation. */
class ConcatList<T> extends AbstractList<T> {

    private static final ConcatList<?> EMPTY = new ConcatList<>();
    private T[] array;
    private int size;

    @SuppressWarnings(value = "unchecked")
    static <T> ConcatList<T> empty() {
        return (ConcatList<T>) EMPTY;
    }

    /** Creates an empty list. */
    public ConcatList() {
    }

    /** Creates a one item list. */
    @SuppressWarnings(value = "unchecked")
    public ConcatList(T t) {
        this.array = (T[]) new Object[]{t};
        size = 1;
    }

    @Override
    public T get(int index) {
        return array[index];
    }

    public ConcatList<T> concat(ConcatList<T> c) {
        if (c == null || c.size == 0) {
            return this;
        }
        if (size == 0) {
            return c;
        }
        final int newsize = size + c.size;
        if (array == null) {
            init(Math.max(10, newsize));
        } else if (newsize > array.length) {
            resize(newsize << 1);
        }
        System.arraycopy(c.array, 0, array, size, c.size);
        size = newsize;
        return this;
    }

    @SuppressWarnings(value = "unchecked")
    private void init(final int length) {
        this.array = (T[]) new Object[length];
    }

    @SuppressWarnings(value = "unchecked")
    private void resize(final int length) {
        Object[] tmp = new Object[length];
        System.arraycopy(array, 0, tmp, 0, size);
        this.array = (T[]) tmp;
    }

    @Override
    public int size() {
        return size;
    }

}
