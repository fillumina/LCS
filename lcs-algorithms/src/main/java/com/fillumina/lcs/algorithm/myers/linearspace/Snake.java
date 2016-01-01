package com.fillumina.lcs.algorithm.myers.linearspace;

import java.util.Iterator;

/**
 * Represents a series of consecutive matches followed or preceeded by a
 * single non matching pair (depending if it is a {@link ForwardSnake} or
 * a {@link ReverseSnake}. It extends {@link Rectangle} because it
 * defines a rectangular region identified by its first and last pair
 * read as Cartesian coordinates.
 *
 * @author Francesco Illuminati
 */
class Snake extends Rectangle implements Iterable<Snake> {
    protected final int xMid;
    protected final int yMid;
    private Snake next;

    Snake(int xStart, int yStart, int xMid, int yMid, int xEnd, int yEnd) {
        super(xStart, yStart, xEnd, yEnd);
        this.xMid = xMid;
        this.yMid = yMid;
    }

    /**
     * @return the given {@link Snake}, or this if it is null.
     */
    static Snake chain(Snake... snakes) {
        Snake head = null;
        Snake current = null;
        for (Snake s : snakes) {
            if (s != null) {
                if (head == null) {
                    current = head = s;
                } else {
                    current.next = s;
                }
                while (current.next != null) {
                    current = current.next;
                }
            }
        }
        return head;
    }

    public Interval getDiagonal() {
        return Interval.EMPTY;
    }

    @Override
    public Iterator<Snake> iterator() {
        return new Iterator<Snake>() {
            private Snake current = Snake.this;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public Snake next() {
                Snake tmp = current;
                current = current.next;
                return tmp;
            }
        };
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() +
                "{xStart=" + getxStart() +
                ", yStart=" + getyStart() +
                ", xMid=" + xMid +
                ", yMid=" + yMid +
                ", xEnd=" + getxEnd() +
                ", yEnd=" + getyEnd() + '}';
    }

}
