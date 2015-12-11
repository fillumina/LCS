package com.fillumina.lcs.util;

import java.util.List;

/**
 * Sublistable vector that keeps track of its start index.
 */
public class VList<T> {

    private final List<T> list;
    private final int start;

    public VList(List<T> list) {
        this(0, list);
    }

    private VList(int start, List<T> list) {
        this.list = list;
        this.start = start;
    }

    /**
     * @return the first index of the wrapped list.
     */
    public int zero() {
        return start;
    }

    /**
     * Note that the first index is 1 and not 0.
     */
    public T get(int index) {
        return list.get(index);
    }

    @SuppressWarnings(value = "unchecked")
    public VList<T> subList(int fromIndex, int toIndex) {
        return new VList<>(start + fromIndex, list.subList(fromIndex, toIndex));
    }

    public int size() {
        return list.size();
    }

    @Override
    public String toString() {
        if (size() == 0) {
            return "[]";
        }
        StringBuilder buf = new StringBuilder().append('[');
        buf.append(get(0));
        for (int i = 1; i < list.size();
                i++) {
            buf.append(", ").append(get(i));
        }
        buf.append(']');
        return buf.toString();
    }

}
