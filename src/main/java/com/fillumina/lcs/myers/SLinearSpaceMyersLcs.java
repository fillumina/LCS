package com.fillumina.lcs.myers;

import com.fillumina.lcs.util.VList;
import com.fillumina.lcs.Lcs;
import com.fillumina.lcs.util.BidirectionalVector;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class SLinearSpaceMyersLcs<T> implements Lcs<T> {

    @Override
    public List<T> lcs(List<T> a, List<T> b) {
        final VList<T> va = new VList<>(a);
        final VList<T> vb = new VList<>(b);
        Segment snakes = lcs(va, vb);
        return extractLcs(snakes, va);
    }

    Segment lcs(VList<T> a, VList<T> b) {
        final int n = a.size();
        final int m = b.size();

        final int a0 = a.zero();
        final int b0 = b.zero();

        if (n == 0) {
            if (m != 0) {
                return createSegment(false, -111, a0, b0, a0, b0+m, a0, b0+m);
            }
            return Segment.NULL;
        }

        if (n == 1) {
            T t = a.get(1);
            for (int i=1; i<=m; i++) {
                if (t.equals(b.get(i))) {
                    if (i == 1) {
                        return createSegment(true, -100, a0, b0, a0+1, b0+i, a0+1, b0+m);
                    } else if (i == m) {
                        return createSegment(false, -100, a0, b0, a0, b0+m-1, a0+1, b0+m);
                    } else {
                        return Segment.chain(
                            createSegment(false, -100, a0, b0, a0, b0+i-1, a0+1, b0+i),
                            createSegment(false, -100, a0+1, b0+i, a0+1, b0+i, a0+1, b0+m)
                        );
                    }
                }
            }
            return createSegment(false, -222, a0,b0, a0,b0+m, a0,b0+m);
        }

        if (m == 0) {
            if (n != 0) {
                return createSegment(false, -111, a0, b0, a0+n, b0, a0+n, b0);
            }
            return Segment.NULL;
        }

        if (m == 1) {
            T t = b.get(1);
            for (int i=1; i<=n; i++) {
                if (t.equals(a.get(i))) {
                    if (i == 1) {
                        return createSegment(true, -100, a0, b0, a0+i, b0+1, a0+n, b0+1);
                    } else if (i == n) {
                        return createSegment(false, -100, a0, b0, a0+n-1, b0, a0+n, b0+1);
                    }
                    return Segment.chain(
                            createSegment(false, -100, a0, b0, a0+i-1, b0+1, a0+i, b0+1),
                            createSegment(false, -100, a0+i, b0+1, a0+n, b0+m, a0+n, b0+m));
                }
            }
            return createSegment(false, -222, a0,b0, a0+n,b0, a0+n,b0);
        }

        Segment segment = findMiddleSnake(a, n, b, m);
        int d = segment.getSteps();
        if (d == 0) {
            return Segment.NULL;
        }
        if (d < n) {
            Segment before =
                lcs(a.subList(1, segment.getXStart() + 1 - a0),
                        b.subList(1, segment.getYStart() + 1 - b0));

            final Segment lastSegment = segment.getLastSegment();
            Segment after =
                lcs(a.subList(lastSegment.getXEnd() + 1 - a0, n + 1),
                        b.subList(lastSegment.getYEnd() + 1 - b0, m + 1));
            return Segment.chain(before, segment, after);
        }
        return segment;
    }

    Segment findMiddleSnake(VList<T> a, int n, VList<T> b, int m) {
        final int max = (int) Math.ceil((n + m) / 2.0);
        final int delta = n - m;
        final boolean oddDelta = (delta & 1) == 1;

        final BidirectionalVector vf = new BidirectionalVector(max);
        final BidirectionalVector vb = new BidirectionalVector(max);

        vb.set(delta-1, n);

        int kk, xf, xr;
        for (int d = 0; d <= max; d++) {
            final int dMinusOne = d - 1;
            for (int k = -d; k <= d; k += 2) {
                xf = findFurthestReachingDPath(d, k, a, n, b, m, vf);
                if (oddDelta &&
                        (delta - dMinusOne) <= k && k <= (delta + dMinusOne)) {
                    if (vb.get(k) <= xf) {
                        return findLastSnake(d, k, xf, a.zero(),b.zero(), vf, a, b);
                    }
                }
            }

            for (int k = -d; k <= d; k += 2) {
                kk = k + delta;
                xr = findFurthestReachingDPathReverse(d, kk, a, n, b, m, delta, vb);
                if (!oddDelta && -delta <= kk && kk <= delta) {
                    if (xr >= 0 && xr <= vf.get(kk)) {
                        return findLastSnakeReverse(d, kk, xr, a.zero(),b.zero(), vb, delta, a, n, b, m);
                    }
                }
            }
        }
        return Segment.NULL;
    }

    private int findFurthestReachingDPath(int d, int k,
            VList<T> a, int n, VList<T> b, int m,
            BidirectionalVector v) {
        int x, y;

        int next = v.get(k + 1);
        int prev = v.get(k - 1);
        if (k == -d || (k != d && prev < next)) {
            x = next;
        } else {
            x = prev + 1;
        }
        y = x - k;
        while (x >= 0 && y >= 0 && x < n && y < m &&
                a.get(x + 1).equals(b.get(y + 1))) {
            x++;
            y++;
        }
        v.set(k, x);
        return x;
    }

    private Segment findLastSnake(int d, int k, int x, int x0, int y0,
            BidirectionalVector v,
            VList<T> a, VList<T> b) {
        int y = x - k;

        int xEnd = x;
        int yEnd = y;

        int next = v.get(k + 1);
        int prev = v.get(k - 1);
        int xStart, yStart, xMid, yMid;
        if (k == -d || (k != d && prev < next)) {
            xStart = next;
            yStart = next - k - 1;
            xMid = xStart;
        } else {
            xStart = prev;
            yStart = prev - k + 1;
            xMid = xStart + 1;
        }

        yMid = xMid - k;

        boolean reverse = false;
        if (xMid == xEnd && yMid == yEnd) {
            xMid = xStart;
            yMid = yStart;
            while (xStart >0 && yStart >0 && a.get(xStart).equals(b.get(yStart))) {
                xStart--;
                yStart--;
            }
            reverse = true;
        }

        return createSegment(reverse, d, x0+xStart, y0+yStart,
                x0+xMid, y0+yMid, x0+xEnd, y0+yEnd);
    }

    private int findFurthestReachingDPathReverse(int d, int k,
            VList<T> a, int n, VList<T> b, int m,
            int delta, BidirectionalVector v) {
        int x, y;

        final int next = v.get(k + 1); // left
        final int prev = v.get(k - 1); // up
        if (k == d+delta || (k != -d+delta && prev < next)) {
            x = prev;   // up
        } else {
            x = next - 1;   // left
        }
        y = x - k;
        while (x > 0 && y > 0 && x <= n && y <= m &&
                a.get(x).equals(b.get(y))) {
            x--;
            y--;
        }
        if (x >= 0) {
            v.set(k, x);
        }
        return x;
    }

    private Segment findLastSnakeReverse(int d, int k, int xe, int x0, int y0,
            BidirectionalVector v, int delta,
            VList<T> a, int n, VList<T> b, int m) {
        final int xStart = xe;
        final int yStart = xe - k;
        int xEnd, yEnd, xMid, yMid;

        final int next = v.get(k + 1);
        final int prev = v.get(k - 1);
        if (k == d+delta || (k != -d+delta && prev != 0 && prev < next)) {
            xEnd = prev;
            yEnd = prev - k + 1;
            xMid = xEnd;
        } else {
            xEnd = next;
            yEnd = next - k - 1;
            xMid = xEnd - 1;
        }
        yMid = xMid - k;

        boolean reverse = true;
        if (xMid == xStart && yMid == yStart) {
            xMid = xEnd;
            yMid = yEnd;
            while (xEnd < n && yEnd < m && a.get(xEnd+1).equals(b.get(yEnd+1))) {
                xEnd++;
                yEnd++;
            }
            reverse = false;
        }

        return createSegment(reverse, d, x0+xStart, y0+yStart,
                x0+xMid, y0+yMid, x0+xEnd, y0+yEnd);
    }

    /**
     * @return the common subsequence elements.
     */
    // TODO use a better visitor way to extract the list without the need to create a temporary list
    private List<T> extractLcs(Segment snakes, VList<T> a) {
        System.out.println("SOLUTION:");
        List<T> list = new ArrayList<>();
        for (Segment segment : snakes) {
            System.out.println(segment);
            segment.addEquals(list, a);
        }
        return list;
    }

    private Segment createSegment(boolean reverse, int d,
                int xStart, int yStart, int xMid, int yMid, int xEnd, int yEnd) {
//        return new Snake(reverse, d, xStart, yStart, xMid, yMid, xEnd, yEnd);
        Segment segment = Segment.create(xStart, yStart, xMid, yMid);
        segment.add(xEnd, yEnd);
        System.out.println(segment.toString());
        return segment;
    }

    static abstract class Segment implements Iterable<Segment>, Serializable {
        private static final long serialVersionUID = 1L;
        public static final Segment NULL = new DiagonalSegment(-1,-1,-1);
        protected final int x,y,steps;
        protected Segment next;

        public Segment(int x, int y, int steps) {
            this.x = x;
            this.y = y;
            this.steps = steps;
        }

        int getXStart() { return x; }
        int getYStart() { return y; }
        int getSteps() {
            if (next == null) {
                return steps;
            }
            return steps + next.steps;
        }

        Segment getLastSegment() {
            Segment s = this;
            while (s.next != null) {
                s = s.next;
            }
            return s;
        }

        abstract int getXEnd();
        abstract int getYEnd();

        <T> void addEquals(List<T> list, VList<T> a) {
            // do nothing;
        }

        static Segment create(int xStart, int yStart, int xEnd, int yEnd) {
            int steps = xEnd - xStart;
            if (steps > 0) {
                if (yStart < yEnd) {
                    return new DiagonalSegment(xStart, yStart, steps);
                }
                return new HorizontalSegment(xStart, yStart, steps);
            }
            steps = yEnd - yStart;
            if (steps > 0) {
                return new VerticalSegment(xStart, yStart, steps);
            }
            throw new IllegalArgumentException();
        }

        static Segment chain(Segment... segments) {
            Segment head = NULL;
            Segment current = null;
            for (Segment s : segments) {
                if (s != null && s != NULL) {
                    if (head == NULL) {
                        current = head = s;
                    } else {
                        current.next = s;
                    }
                    while(current.next != null) {
                        current = current.next;
                    }
                }
            }
            return head;
        }

        public Segment add(int nextX, int nextY) {
            final int xEnd = getXEnd();
            final int yEnd = getYEnd();
            int distance = nextX - xEnd;
            if (distance > 0) {
                if (yEnd < nextY) {
                    Segment diagonal = new DiagonalSegment(xEnd, yEnd, distance);
                    this.next = diagonal;
                    return diagonal;
                }
                Segment horizontal = new HorizontalSegment(xEnd, yEnd, distance);
                this.next = horizontal;
                return horizontal;
            }
            distance = nextY - yEnd;
            if (distance > 0) {
                Segment vertical = new VerticalSegment(xEnd, yEnd, distance);
                this.next = vertical;
                return vertical;
            }
            return this;
        }



        @Override
        public Iterator<Segment> iterator() {
            return new Iterator<Segment>() {
                private Segment current = Segment.this;

                @Override
                public boolean hasNext() {
                    return current != null && current != NULL;
                }

                @Override
                public Segment next() {
                    Segment tmp = current;
                    current = current.next;
                    return tmp;
                }
            };
        }

        static String toString(Segment s) {
            Segment current = s;
            StringBuilder buf = new StringBuilder("[");
            buf.append(s.toSingleString());
            while(current.next != null && current.next != NULL) {
                current = current.next;
                buf.append(", ").append(current.toSingleString());
            }
            buf.append("]");
            return buf.toString();
        }

        @Override
        public String toString() {
            if (this == NULL) {
                return "Segment{NULL}";
            }
            if (next != null && next.next == null) {
                return "Snake{d=" + getSteps() +
                        ", xStart=" + x + ", yStart=" + y +
                        ", xMid=" + getXEnd() + ", yMid=" + getYEnd() +
                        ", xEnd=" + next.getXEnd() +
                        ", yEnd=" + next.getYEnd() + '}';
            }
            return toSingleString();
        }

        private String toSingleString() {
            return getClass().getSimpleName() +
                    "{ steps=" + steps +
                    ", xStart=" + x + ", yStart=" + y +
                    ", xEnd=" + getXEnd() +
                    ", yEnd=" + getYEnd() + '}';
        }
    }

    static class DiagonalSegment extends Segment {
        private static final long serialVersionUID = 1L;
        public DiagonalSegment(int x, int y, int steps) {
            super(x,y,steps);
        }
        @Override int getXEnd() { return x + steps; }
        @Override int getYEnd() { return y + steps; }

        @Override
        <T> void addEquals(List<T> list, VList<T> a) {
            for (int i=x+1; i<=x+steps; i++) {
                list.add(a.get(i));
            }
        }
    }

    static class HorizontalSegment extends Segment {
        private static final long serialVersionUID = 1L;
        public HorizontalSegment(int x, int y, int steps) {
            super(x,y,steps);
        }
        @Override int getXEnd() { return x + steps; }
        @Override int getYEnd() { return y; }
    }

    static class VerticalSegment extends Segment {
        private static final long serialVersionUID = 1L;
        public VerticalSegment(int x, int y, int steps) {
            super(x,y,steps);
        }
        @Override int getXEnd() { return x; }
        @Override int getYEnd() { return y + steps; }
    }
}
