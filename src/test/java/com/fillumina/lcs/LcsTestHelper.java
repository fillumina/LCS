package com.fillumina.lcs;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class LcsTestHelper {

    protected void testLcs(Lcs<Character> lcs, String a, String b, String result) {
        List<Character> resultList = lcs.lcs(toList(a), toList(b));
        assertEquals(result, toString(resultList));
    }

    protected List<Character> toList(String s) {
        char[] array = s.toCharArray();
        List<Character> list = new ArrayList<>(array.length);
        for (char c : array) {
            list.add(c);
        }
        return list;
    }

    protected String toString(List<Character> list) {
        int i = 0;
        char[] array = new char[list.size()];
        for (Character c : list) {
            array[i++] = c;
        }
        return new String(array);
    }

}
