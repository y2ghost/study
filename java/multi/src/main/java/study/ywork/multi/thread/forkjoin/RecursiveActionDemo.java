package study.ywork.multi.thread.forkjoin;

import java.math.BigInteger;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

public class RecursiveActionDemo {
    public static void main(String[] args) {
        List<BigInteger> list = new ArrayList<>();
        for (int i = 3; i < 20; i++) {
            list.add(new BigInteger(Integer.toString(i)));
        }

        ForkJoinPool.commonPool().invoke(new FactorialTask(list));
    }

    private static class FactorialTask extends RecursiveAction {
        private static final long serialVersionUID = 1L;
        private static final int SEQUENTIAL_THRESHOLD = 5;
        private List<BigInteger> integerList;

        private FactorialTask(List<BigInteger> integerList) {
            this.integerList = integerList;
        }

        @Override
        protected void compute() {
            if (integerList.size() <= SEQUENTIAL_THRESHOLD) {
                showFactorials();
            } else {
                int middle = integerList.size() / 2;
                List<BigInteger> newList = integerList.subList(middle, integerList.size());
                integerList = integerList.subList(0, middle);
                FactorialTask task = new FactorialTask(newList);
                task.fork();
                this.compute();
            }
        }

        private void showFactorials() {
            for (BigInteger i : integerList) {
                System.out.printf("[%s] : %s! = %s, thread = %s %n", LocalTime.now(), i, CalcUtil.calculateFactorial(i),
                    Thread.currentThread().getName());
                try {
                    TimeUnit.MILLISECONDS.sleep(500);
                } catch (InterruptedException e) {
                    System.err.println(e);
                }
            }
        }
    }
}
