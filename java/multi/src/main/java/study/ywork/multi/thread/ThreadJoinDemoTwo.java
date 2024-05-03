package study.ywork.multi.thread;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ThreadJoinDemoTwo {
    public static void main(String[] args) {
        final List<Integer> integers = Arrays.asList(10, 12, 13, 14, 15, 20);
        new Thread(() -> {
            List<FactorialCalculator> threads = new ArrayList<>();
            for (Integer integer : integers) {
                FactorialCalculator calc = new FactorialCalculator(integer);
                threads.add(calc);
                calc.start();
            }
            for (FactorialCalculator calc : threads) {
                try {
                    calc.join();
                    System.out.println(calc.getNumber() + "! = " + calc.getFactorial());
                } catch (InterruptedException e) {
                    System.err.println(e);
                    Thread.currentThread().interrupt();
                }
            }
        }).start();
    }

    private static class FactorialCalculator extends Thread {
        private final int number;
        private BigDecimal factorial;

        FactorialCalculator(int number) {
            this.number = number;
        }

        @Override
        public void run() {
            factorial = calculateFactorial(number);
        }

        private static BigDecimal calculateFactorial(int number) {
            BigDecimal factorial = BigDecimal.ONE;
            for (int i = 1; i <= number; i++) {
                factorial = factorial.multiply(new BigDecimal(i));
            }

            return factorial;
        }

        public BigDecimal getFactorial() {
            return factorial;
        }

        public int getNumber() {
            return number;
        }
    }
}
