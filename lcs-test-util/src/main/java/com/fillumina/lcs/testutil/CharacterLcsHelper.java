package com.fillumina.lcs.testutil;

import java.util.ArrayList;
import java.util.List;
import com.fillumina.lcs.helper.LcsList;

/**
 *
 * @author Francesco Illuminati 
 */
public class CharacterLcsHelper {

    public static  Character[] toArray(String s) {
        char[] array = s.toCharArray();
        Character[] ca = new Character[array.length];
        for (int i=0; i<array.length; i++) {
            ca[i] = array[i];
        }
        return ca;
    }

    public static  List<Character> toList(String s) {
        char[] array = s.toCharArray();
        List<Character> list = new ArrayList<>(array.length);
        for (char c : array) {
            list.add(c);
        }
        return list;
    }

    public static String toString(List<? extends Character> list) {
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
