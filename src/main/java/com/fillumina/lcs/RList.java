package com.fillumina.lcs;

import java.util.AbstractList;
import java.util.ArrayList;
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

    private static final RList<?> EMPTY_RLIST = new RList<Object>() {
            @Override
            public int size() {
                return 0;
            }

            @Override
            public RList<Object> reverse() {
                return this;
            }

            @Override
            public Object get(int index) {
                throw new IndexOutOfBoundsException("empty list!");
            }

            @Override
            public RList<Object> subList(int fromIndex, int toIndex) {
                if (fromIndex == 0 && toIndex == 0) {
                    return this;
                }
                throw new IndexOutOfBoundsException("empty list!");
            }

            @Override
            public RList<Object> add(RList<Object> list) {
                return list;
            }
        };

    private static class SingletonRList<T> extends RList<T> {
            private final T t;

            public SingletonRList(T t) {
                this.t = t;
            }

            @Override
            public int size() {
                return 1;
            }

            @Override
            public RList<T> reverse() {
                return this;
            }

            @Override
            public T get(int index) {
                if (index != 0) {
                    throw new IndexOutOfBoundsException();
                }
                return t;
            }

            @Override
            @SuppressWarnings("unchecked")
            public RList<T> subList(int fromIndex, int toIndex) {
                if (fromIndex != 0) {
                    throw new IndexOutOfBoundsException("fromIndex: " + fromIndex);
                }
                if (toIndex == 0) {
                    return (RList<T>) EMPTY_RLIST;
                } else if (toIndex == 1) {
                    return this;
                } else {
                    throw new IndexOutOfBoundsException("toIndex: " + toIndex);
                }
            }

            @Override
            public RList<T> add(RList<T> list) {
                if (list == null || list.isEmpty()) {
                    return this;
                } else if (list instanceof SingletonRList) {
                    List<T> l = new ArrayList<>();
                    l.add(t);
                    l.add(((SingletonRList<T>)list).t);
                    return new RList<>(l);
                }
                return super.add(list);
            }
        };

    @SuppressWarnings("unchecked")
    public static <T> RList<T> emptyList() {
        return (RList<T>) EMPTY_RLIST;
    }

    public static <T> RList<T> singleton(T t) {
        return new SingletonRList<>(t);
    }

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

    public RList<T> add(RList<T> list) {
        final ArrayList<T> arrayList = new ArrayList<>(this);
        arrayList.addAll(list);
        return new RList<>(arrayList);
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
