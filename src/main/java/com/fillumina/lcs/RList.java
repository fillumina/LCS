package com.fillumina.lcs;

import java.util.AbstractList;
import java.util.List;

/**
 * A list wrapper that can't change its content but can be sub-listed and
 * reversed without the need to copy to a new list.
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
class RList<T> extends AbstractList<T> {
    private final List<T> list;
    private final int size;
    private final int start;
    private final int end;
    private final boolean reverse;

    public RList() {
        this(null);
    }

    public RList(List<T> list) {
        this(list, false);
    }

    private RList(List<T> list, boolean reverse) {
        this(list, 0, list == null ? 0 : list.size(), reverse);
    }

    private RList(List<T> list,
            int start, int end, boolean reverse) {
        this.list = list;
        this.size = end - start - 1;
        this.start = start;
        this.end = end;
        this.reverse = reverse;
    }

    @Override
    public T get(int index) {
        int idx = start;
        if (reverse) {
            idx += (size - index);
        } else {
            idx += index;
        }
        if (idx < start || idx >= end) {
            throw new IndexOutOfBoundsException("index: " + idx);
        }
        return list.get(idx);
    }

    @Override
    public RList<T> subList(int fromIndex, int toIndex) {
        return new RList<>(list,
                start + fromIndex,
                start + toIndex,
                reverse);
    }

    public RList<T> reverse() {
        return new RList<>(list, start, end, !reverse);
    }

    @Override
    public int size() {
        return size + 1;
    }
}
