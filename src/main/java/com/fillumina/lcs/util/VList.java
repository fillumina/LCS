package com.fillumina.lcs.util;

import java.util.Collections;
import java.util.List;

/**
 * Reversable, sublistable vector with starting index 1.
 */
public class VList<T> {

    @SuppressWarnings(value = "unchecked")
    private static final VList<?> EMPTY = new VList<>(Collections.EMPTY_LIST);
    private final List<T> list;
    private final int size;
    private final int start;
    private final int end;
    private final boolean reverse;

    public VList(List<T> list) {
        this(list, false);
    }

    private VList(List<T> list, boolean reverse) {
        this(list, 0, list == null ? 0 : list.size(), reverse);
    }

    private VList(List<T> list, int start, int end, boolean reverse) {
        this.list = list;
        this.start = start;
        this.end = end;
        this.size = end - start;
        this.reverse = reverse;
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
        if (idx < 0 || idx >= list.size()) {
            throw new IndexOutOfBoundsException(
                    "index (list size=" + size + "): " + index);
        }
        return list.get(idx);
    }

    private int calculateIndex(int index) {
        int idx = start;
        if (reverse) {
            idx += (size - index);
        } else {
            idx += index - 1;
        }
        return idx;
    }

    @SuppressWarnings(value = "unchecked")
    public VList<T> subList(int fromIndex, int toIndex) {
        if (fromIndex < 1 || toIndex < 1) {
            throw new IndexOutOfBoundsException(
                    "indexes cannot be less than 1, to=" + toIndex + ", from=" +
                            fromIndex + " " + toString());
        }
        if (toIndex < fromIndex) {
            toIndex = fromIndex;
        }
        final int correctedFromIndex = calculateIndex(fromIndex);
        int correctedToIndex = calculateIndex(toIndex);
        if (correctedFromIndex < 0 || correctedFromIndex > list.size()) {
            throw new IndexOutOfBoundsException(
                    "from index out of boundaries " + correctedFromIndex + " " +
                            toString());
        }
        if (correctedFromIndex + size < correctedToIndex) {
            throw new IndexOutOfBoundsException(
                    "toIndex=" + toIndex + " " + toString());
            //correctedToIndex = correctedFromIndex + size;
        }
        if (reverse) {
            return new VList<>(list, correctedToIndex + 1,
                    correctedFromIndex + 1, true);
        }
        return new VList<>(list, correctedFromIndex, correctedToIndex, false);
    }

    public VList<T> reverse() {
        if (size <= 1) {
            return this;
        }
        return new VList<>(list, start, end, !reverse);
    }

    /**
     * Note that the last index is {@code size}.
     */
    public int size() {
        return size;
    }

    @Override
    public String toString() {
        if (size() == 0) {
            return "[]";
        }
        StringBuilder buf = new StringBuilder().append('[');
        buf.append(get(1));
        for (int i = 2; i <= size;
                i++) {
            buf.append(", ").append(get(i));
        }
        buf.append(']');
        return buf.toString();
    }

}
