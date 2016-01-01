package com.fillumina.lcs.algorithm.myers.linearspace;

import com.fillumina.lcs.algorithm.myers.BidirectionalVector;
import java.util.ArrayList;
import java.util.List;

/**
 * Myers algorithm that uses forward and backward snakes. It is not designed
 * to be fast but to be easy to understand and close to the description
 * given by Myers on his paper.
 *
 * @see <a href='www.xmailserver.org/diff2.pdf'>
 *  An O(ND) Difference Algorithm and Its Variations (PDF)
 * </a>
 *
 * @author Francesco Illuminati
 */
class LinearSpaceMyersLcsSolver<T> {

    private final T[] a;
    private final T[] b;

    public LinearSpaceMyersLcsSolver(T[] a, T[] b) {
        this.a = a;
        this.b = b;
    }

    List<T> calculateLcs() {
        Snake snakes = new Section(0, 0, a.length, b.length).lcs();
        return extractLcs(snakes);
    }

    /** @return the common subsequence elements. */
    private List<T> extractLcs(Snake snakes) {
        List<T> list = new ArrayList<>();
        for (Snake snake : snakes) {
            for (int index : snake.getDiagonal()) {
                list.add(a[index]);
            }
        }
        return list;
    }

    class Section extends Rectangle {

        Section(int xStart, int yStart, int xEnd, int yEnd) {
            super(xStart, yStart, xEnd, yEnd);
        }

        <T> Snake lcs() {
            if (isImproper()) {
                return createNullSnake();
            }

            Snake snake = findMiddleSnake();
            if (equals(snake)) {
                return snake;
            }

            Snake before = getSectionBefore(snake).lcs();
            Snake after = getSectionAfter(snake).lcs();

            return Snake.chain(before, snake, after);
        }

        <T> Snake findMiddleSnake() {
            final int max = (int) Math.ceil((m + n) / 2.0) + 1;
            final int delta = n - m;
            final boolean evenDelta = delta % 2 == 0;

            final BidirectionalVector vf = new BidirectionalVector(max);
            final BidirectionalVector vb = new BidirectionalVector(max, delta);

            vf.set(1, 0);
            vb.set(0, n);
            vb.set(delta - 1, n);

            int kk, xf, xr, start, end;

            for (int d = 0; d <= max; d++) {
                start = delta - (d - 1);
                end = delta + (d - 1);

                for (int k = -d; k <= d; k += 2) {
                    xf = findFurthestReachingDPath(d, k, vf);
                    if (!evenDelta && isIn(k, start, end) && vb.get(k) <= xf) {
                        return findLastSnake(d, k, vf, xf);
                    }
                }

                for (int k = -d; k <= d; k += 2) {
                    kk = k + delta;
                    xr = findFurthestReachingDPathReverse(d, kk, delta, vb);
                    if (evenDelta && isIn(kk, -d, d) && xr >= 0 && xr <= vf.get(kk)) {
                        return findLastSnakeReverse(d, kk, delta, vb, xr);
                    }
                }
            }
            return createNullSnake();
        }

        private int findFurthestReachingDPath(int d, int k,
                BidirectionalVector vf) {
            int x, y;

            int next = vf.get(k + 1);
            int prev = vf.get(k - 1);

            if (k == -d || (k != d && prev < next)) {
                x = next;
            } else {
                x = prev + 1;
            }

            y = x - k;
            while (x >= 0 && y >= 0 && x < n && y < m && sameItem(x, y)) {
                x++;
                y++;
            }

            vf.set(k, x);
            return x;
        }

        private Snake findLastSnake(int d, int k, BidirectionalVector vf, int x) {
            int y = x - k;

            int xEnd = x;
            int yEnd = y;

            int next = vf.get(k + 1);
            int prev = vf.get(k - 1);

            int xStart, yStart, xMid, yMid;

            if (k == -d) {
                xStart = next;
                yStart = xStart - k;
                xMid = xStart;
            } else if (k != d && prev < next) {
                xStart = next;
                yStart = xStart - k - 1;
                xMid = xStart;
            } else {
                xStart = prev;
                yStart = xStart - k + 1;
                xMid = xStart + 1;
            }

            yMid = xMid - k;
            if (xMid == xEnd && yMid == yEnd) {
                xMid = xStart;
                yMid = yStart;
                while (xStart > 0 && yStart > 0 && sameItem(xStart - 1,
                        yStart - 1)) {
                    xStart--;
                    yStart--;
                }
                return createReverseSnake(xStart, yStart, xMid, yMid, xEnd, yEnd);
            }
            return createForwardSnake(xStart, yStart, xMid, yMid, xEnd, yEnd);
        }

        private <T> int findFurthestReachingDPathReverse(int d, int k, int delta,
                BidirectionalVector vb) {
            int x, y;

            final int next = vb.get(k + 1); // left
            final int prev = vb.get(k - 1); // up

            if (k == d + delta || (k != -d + delta && prev < next)) {
                x = prev; // up
            } else {
                x = next - 1; // left
            }

            y = x - k;
            while (x > 0 && y > 0 && x <= n && y <= m && sameItem(x - 1, y - 1)) {
                x--;
                y--;
            }

            if (x >= 0) {
                vb.set(k, x);
            }
            return x;
        }

        private <T> Snake findLastSnakeReverse(int d, int k, int delta,
                BidirectionalVector vb, int xe) {
            final int xStart = xe;
            final int yStart = xe - k;
            final int next = vb.get(k + 1);
            final int prev = vb.get(k - 1);

            int xEnd, yEnd, xMid, yMid;

            if (k == d + delta) {
                xEnd = prev;
                yEnd = prev - k;
                xMid = xEnd;
            } else if (k != -d + delta && prev != 0 && prev < next) {
                xEnd = prev;
                yEnd = prev - k + 1;
                xMid = xEnd;
            } else {
                xEnd = next;
                yEnd = next - k - 1;
                xMid = xEnd - 1;
            }

            yMid = xMid - k;
            if (xMid == xStart && yMid == yStart) {
                xMid = xEnd;
                yMid = yEnd;
                while (xEnd < n && yEnd < m && sameItem(xEnd, yEnd)) {
                    xEnd++;
                    yEnd++;
                }
                return createForwardSnake(xStart, yStart, xMid, yMid, xEnd, yEnd);
            }
            return createReverseSnake(xStart, yStart, xMid, yMid, xEnd, yEnd);
        }

        /** @return the innner rectangle getSectionBefore the given one. */
        Section getSectionBefore(Rectangle o) {
            return new Section(getxStart(), getyStart(),
                    o.getxStart(), o.getyStart());
        }

        /** @return the innner rectangle getSectionAfter the given one. */
        Section getSectionAfter(Rectangle o) {
            return new Section(o.getxEnd(), o.getyEnd(), getxEnd(), getyEnd());
        }

        Snake createForwardSnake(int xStart, int yStart, int xMid, int yMid,
                int xEnd, int yEnd) {
            int x0 = getxStart();
            int y0 = getyStart();
            return new ForwardSnake(x0 + xStart, y0 + yStart, x0 + xMid,
                    y0 + yMid, x0 + xEnd, y0 + yEnd);
        }

        Snake createReverseSnake(int xStart, int yStart, int xMid, int yMid,
                int xEnd, int yEnd) {
            int x0 = getxStart();
            int y0 = getyStart();
            return new ReverseSnake(x0 + xStart, y0 + yStart, x0 + xMid,
                    y0 + yMid, x0 + xEnd, y0 + yEnd);
        }

        Snake createNullSnake() {
            return new Snake(getxStart(), getyStart(), getxStart(), getyStart(),
                    getxEnd(), getyEnd());
        }

        boolean sameItem(int aIndex, int bIndex) {
            T aItem = a[getxStart() + aIndex];
            T bItem = b[getyStart() + bIndex];
            return aItem == bItem || (aItem != null && aItem.equals(bItem));
        }
    }

    static boolean isIn(int value, int startInterval, int endInterval) {
        if (startInterval < endInterval) {
            if (value < startInterval) {
                return false;
            }
            if (value > endInterval) {
                return false;
            }
        }
        else {
            if (value > startInterval) {
                return false;
            }
            if (value < endInterval) {
                return false;
            }
        }
        return true;
    }
}
