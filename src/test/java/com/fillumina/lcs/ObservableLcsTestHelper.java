package com.fillumina.lcs;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class ObservableLcsTestHelper extends LcsTestHelper {

    protected void testObservableLcs(ObservableLcs<Character> lcs, String a,
            String b, String result) {
        CountingListener<Character> countingListener = new CountingListener<>();
        lcs.setListener(countingListener);
        testLcs(lcs, a, b, result);
        System.out.println(countingListener.toString());
    }

}
