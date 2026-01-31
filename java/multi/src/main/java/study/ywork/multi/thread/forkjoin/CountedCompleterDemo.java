package study.ywork.multi.thread.forkjoin;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountedCompleter;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicReference;

public class CountedCompleterDemo {
    public static void main(String[] args) {
        List<BigInteger> list = new ArrayList<>();
        for (int i = 3; i < 20; i++) {
            list.add(new BigInteger(Integer.toString(i)));
        }

        BigInteger sum = ForkJoinPool.commonPool()
            .invoke(new FactorialTask(null, new AtomicReference<>(BigInteger.valueOf(0)), list));
        System.out.println("Sum of the factorials = " + sum);
    }

    private static class FactorialTask extends CountedCompleter<BigInteger> {
        private static final long serialVersionUID = 1L;
        private static final int SEQUENTIAL_THRESHOLD = 5;
        private List<BigInteger> integerList;
        private AtomicReference<BigInteger> result;

        private FactorialTask(CountedCompleter<BigInteger> parent, AtomicReference<BigInteger> result,
            List<BigInteger> integerList) {
            super(parent);
            this.integerList = integerList;
            this.result = result;
        }

        @Override
        public BigInteger getRawResult() {
            return result.get();
        }

        @Override
        public void compute() {
            while (integerList.size() > SEQUENTIAL_THRESHOLD) {
                List<BigInteger> newTaskList = integerList.subList(integerList.size() - SEQUENTIAL_THRESHOLD,
                    integerList.size());
                integerList = integerList.subList(0, integerList.size() - SEQUENTIAL_THRESHOLD);
                addToPendingCount(1);
                FactorialTask task = new FactorialTask(this, result, newTaskList);
                task.fork();
            }

            sumFactorials();
            propagateCompletion();
        }

        private void addFactorialToResult(BigInteger factorial) {
            result.getAndAccumulate(factorial, (b1, b2) -> b1.add(b2));
        }

        private void sumFactorials() {
            for (BigInteger i : integerList) {
                addFactorialToResult(CalcUtil.calculateFactorial(i));
            }
        }
    }
}
