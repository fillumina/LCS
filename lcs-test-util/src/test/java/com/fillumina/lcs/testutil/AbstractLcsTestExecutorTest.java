package com.fillumina.lcs.testutil;

import com.fillumina.lcs.helper.LcsLength;
import com.fillumina.lcs.testutil.AbstractLcsTestExecutor.Result;
import java.util.List;
import org.junit.Test;
import com.fillumina.lcs.helper.LcsList;
import java.util.ArrayList;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author Francesco Illuminati 
 */
public class AbstractLcsTestExecutorTest {

    @Test
    public void checkSingleCall() {
        Result result = test("result", new String[][] {
            {"alfa", "beta"}
        });
        result.assertNumberOfCalls(1);
        result.assertResult("result");
    }

    @Test(expected = AssertionError.class)
    public void checkSingleCallError() {
        Result result = test("result", new String[][] {
            {"alfa", "beta"}
        });
        result.assertNumberOfCalls(4);
    }

    @Test(expected = AssertionError.class)
    public void checkSingleCallResultError() {
        Result result = test("result", new String[][] {
            {"alfa", "beta"}
        });
        result.assertResult("error");
    }

    @Test
    public void checkTwoCalls() {
        Result result = test("two", new String[][] {
            {"alfa", "beta"}, {"gamma", "delta"}
        });
        result.assertNumberOfCalls(2);
        result.assertResult("two");
    }

    @Test
    public void check100Calls() {
        String[][] inputs = new String[100][];
        for (int i=0; i<100; i++) {
            inputs[i] = new String[2];
            inputs[i][0] = "one_" + i;
            inputs[i][1] = "two_" + i;
        }
        Result result = test("hundred", inputs);
        result.assertNumberOfCalls(100);
        result.assertResult("hundred");
    }

    @Test(expected = AssertionError.class)
    public void check100CallsError() {
        String[][] inputs = new String[100][];
        for (int i=0; i<100; i++) {
            inputs[i] = new String[2];
            inputs[i][0] = "one_" + i;
            inputs[i][1] = "two_" + i;
        }
        Result result = test("hundred", inputs);
        result.assertNumberOfCalls(101); // <--------------- *
        result.assertResult("hundred");
    }

    private Result test(String result, String[][] inputs) {
        AbstractLcsTestExecutor executor =
            new AbstractLcsTestExecutorImpl(result, inputs);
        return executor.lcs(inputs[0][0], inputs[0][1]);
    }

    private static class AbstractLcsTestExecutorImpl
            extends AbstractLcsTestExecutor {

        private final String[][] inputs;
        private final String result;
        private int index = 1;

        public AbstractLcsTestExecutorImpl(String result, String[][] inputs) {
            this.result = result;
            this.inputs = inputs;
        }

        @Override
        public LcsList getLcsAlgorithm() {
            return new LcsList() {

                @Override
                @SuppressWarnings("unchecked")
                public <T> List<? extends T> lcs(
                        List<? extends T> xs,
                        List<? extends T> ys) {
                    count(xs, ys);
                    try {
                        final String[] inputLine = inputs[index++];
                        List<Character> a = CharacterLcsHelper.toList(inputLine[0]);
                        List<Character> b = CharacterLcsHelper.toList(inputLine[1]);
                        return (List<? extends T>) lcs(a, b);
                    } catch (IndexOutOfBoundsException e) {
                        return (List<? extends T>) CharacterLcsHelper.toList(result);
                    }
                }
            };
        }
    }

    @Test
    public void shouldExecuteTheLcsAlgorithm() {
        assertEquals("ALFABETA",
                AbstractLcsTestExecutor.executeLcs(new ConcatLcs(), "ALFA", "BETA"));
    }

    static class ConcatLcs implements LcsList {

        @Override
        public <T> List<? extends T> lcs(
                List<? extends T> xs,
                List<? extends T> ys) {
            List<T> list = new ArrayList<>(xs.size() + ys.size() + 1);
            list.addAll(xs);
            list.addAll(ys);
            return list;
        }
    }


}
