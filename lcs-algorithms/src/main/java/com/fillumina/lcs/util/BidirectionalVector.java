package com.fillumina.lcs.util;

import java.util.Arrays;

/** A vector that allows for negative indexes. */
public class BidirectionalVector {
    private final int[] array;
    private final int size;

    /**
     * @param size vector indexes goes from {@code -size} to {@code size}
     *             extremities excluded.
     */
    public BidirectionalVector(int size) {
        this.size = size;
        this.array = new int[(size << 1) + 1];
    }

    private BidirectionalVector(int[] array) {
        this.size = array.length >> 1;
        this.array = array;
    }

    public int get(int x) {
        int index = size + x;
        try {
            return array[index];
        } catch (IndexOutOfBoundsException e) {
            return -1;
        }
    }

    public void set(int x, int value) {
        try {
            array[size + x] = value;
        } catch (IndexOutOfBoundsException e) {
            // ignore it
        }
    }

    @Override
    public String toString() {
        return "" + size + ":" + Arrays.toString(array);
    }

    /** Same as {@link Object#clone()} but without throwing anything. */
    public BidirectionalVector copy() {
        int [] cloned = new int[array.length];
        System.arraycopy(array, 0, cloned, 0, array.length);
        return new BidirectionalVector(cloned);
    }
}
