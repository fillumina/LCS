package com.fillumina.lcs.util;

import java.util.List;

/** A vector that starts from index 1 instead of 0. */
public class OneBasedVector<T> {
    private final List<T> list;
    private final int size;

    public OneBasedVector(List<T> list) {
        this.list = list;
        this.size = list.size();
    }

    public T get(int x) {
        return list.get(x - 1);
    }

    public void set(int x, T value) {
        list.set(x - 1, value);
    }

    public int size() {
        return size;
    }

    @Override
    public String toString() {
        return "OneBasedVector{" + "list=" + list + ", size=" + size + '}';
    }
}
