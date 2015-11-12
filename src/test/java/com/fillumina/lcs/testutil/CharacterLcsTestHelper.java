package com.fillumina.lcs.testutil;

import java.util.ArrayList;
import java.util.List;
import com.fillumina.lcs.Lcs;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class CharacterLcsTestHelper {

    public static String executeLcs(Lcs<Character> lcs, String a, String b) {
        List<Character> resultList = lcs.lcs(toList(a), toList(b));
        return toString(resultList);
    }

    public static  List<Character> toList(String s) {
        char[] array = s.toCharArray();
        List<Character> list = new ArrayList<>(array.length);
        for (char c : array) {
            list.add(c);
        }
        return list;
    }

    public static  String toString(List<Character> list) {
        if (list == null || list.isEmpty()) {
            return "";
        }
        int i = 0;
        char[] array = new char[list.size()];
        for (Character c : list) {
            array[i++] = c;
        }
        return new String(array);
    }

}
