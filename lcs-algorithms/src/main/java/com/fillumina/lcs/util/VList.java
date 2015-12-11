package com.fillumina.lcs.util;

import java.util.List;

/**
 * Sublistable vector that keeps track of its start index.
 */
public class VList<T> {

    private final List<T> list;
    private final int size;
    private final int start;
    private final int end;

    public VList(List<T> list) {
        this(list, 0, list == null ? 0 : list.size());
    }

    public VList(List<T> list, int start, int end) {
        this.list = list;
        this.start = start;
        this.end = end;
        this.size = end - start;
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
        final int idx = calculateIndex(index);
        if (idx < start || idx >= end) {
            throw new IndexOutOfBoundsException(
                    "index (size=" + size + "): " + index);
        }
        return list.get(idx);
    }

    @SuppressWarnings(value = "unchecked")
    public VList<T> subList(int fromIndex, int toIndex) {
        if (toIndex < fromIndex) {
            throw new IllegalArgumentException(
                    "end index cannot be less than start");
        }
        if (fromIndex < 0 || fromIndex > size ||
                toIndex < 0 || toIndex > size) {
            throw new IndexOutOfBoundsException(
                    "indexes cannot be outside interval " + start +
                            " to " + end);
        }
        final int correctedFromIndex = calculateIndex(fromIndex);
        int correctedToIndex = calculateIndex(toIndex);
        if (correctedFromIndex + size < correctedToIndex) {
            throw new IndexOutOfBoundsException("toIndex=" + toIndex);
        }
        return new VList<>(list, correctedFromIndex, correctedToIndex);
    }

    private int calculateIndex(int index) {
        return start + index;
    }

    public int size() {
        return size;
    }

    @Override
    public String toString() {
        if (size() == 0) {
            return "[]";
        }
        StringBuilder buf = new StringBuilder().append('[');
        buf.append(get(0));
        for (int i = 1; i < size;
                i++) {
            buf.append(", ").append(get(i));
        }
        buf.append(']');
        return buf.toString();
    }

}
