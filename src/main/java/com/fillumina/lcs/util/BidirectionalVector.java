package com.fillumina.lcs.util;

import java.util.Arrays;

/** A vector that allows for negative indexes. */
public class BidirectionalVector {
    private final int[] array;
    private final int halfSize;

    public BidirectionalVector(int size) {
        this(size, 0);
    }

    public BidirectionalVector(int[] array) {
        this.halfSize = array.length >> 1;
        this.array = array;
//        Arrays.fill(array, -999);
    }

    /**
     * @param size specify the positive size (the total size will be
     *             {@code size * 2 + 1}.
     * @param constant is always subtracted to the given index
     */
    public BidirectionalVector(int size, int constant) {
        int length = size + Math.abs(constant);
        this.array = new int[(length << 1) + 1];
        this.halfSize = length - constant;
        Arrays.fill(array, -999);
    }

    public int get(int x) {
        int index = halfSize + x;
        if (index < 0 || index >= array.length) {
            return -1;
        }
        return array[index];
    }

    public void set(int x, int value) {
        int index = halfSize + x;
        if (index < 0 || index >= array.length) {
            return;
        }
        array[index] = value;
    }

    public void copyToArray(int[] vector) {
        if (vector.length < array.length) {
            throw new IllegalArgumentException("vector too small");
        }
        System.arraycopy(array, 0, vector, 0, array.length);
    }

    @Override
    public String toString() {
        return "" + halfSize + ":" + Arrays.toString(array);
    }

}
