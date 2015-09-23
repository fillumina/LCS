package com.fillumina.lcs;

import org.junit.Test;

/**
 *
 * @author Francesco Illuminati <fillumina@gmail.com>
 */
public class ObservableLcsTest extends ObservableLcsTestHelper {

    @Test
    public void shouldGetTheRightResult() {
        testObservableLcs(new ObservableRecursiveLcs<Character>(),
                "HUMAN", "CHIMPANZEE", "HMAN");
    }
}
