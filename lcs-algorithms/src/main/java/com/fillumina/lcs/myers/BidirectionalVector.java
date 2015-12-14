package com.fillumina.lcs.myers;

import java.util.Arrays;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
class BidirectionalVector {

    private final int[] array;
    private final int zero;

    public BidirectionalVector(int size) {
        this(size, 0);
    }

    /**
     * @param size specify the positive size (the total size will be
     *             {@code size * 2 + 1}.
     * @param offset is always subtracted to the given index
     */
    public BidirectionalVector(int size, int offset) {
        int length = size + Math.abs(offset);
        this.array = new int[(length << 1) + 1];
        this.zero = length - offset;
    }

    private BidirectionalVector(int zero, int[] array) {
        this.zero = zero;
        this.array = array;
    }

    public int get(int x) {
        return array[zero + x];
    }

    public void set(int x, int value) {
        array[zero + x] = value;
    }

    @Override
    public String toString() {
        return "" + zero + ":" + Arrays.toString(array);
    }

    /** Same as {@link Object#clone()} but without throwing anything. */
    public BidirectionalVector copy() {
        int [] cloned = new int[array.length];
        System.arraycopy(array, 0, cloned, 0, array.length);
        return new BidirectionalVector(zero, cloned);
    }

}
