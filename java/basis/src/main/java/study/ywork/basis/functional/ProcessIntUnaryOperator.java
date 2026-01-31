package study.ywork.basis.functional;

import java.util.function.IntUnaryOperator;

public class ProcessIntUnaryOperator {
    static int[] integers = {0, 1, 2, 3, 4, 5};

    static int doTheMath(int n, IntUnaryOperator func) {
        return func.applyAsInt(n);
    }

    public static void main(String[] args) {
        int total = 0;
        for (int i : integers) {
            total += doTheMath(i, k -> k * k + 1);
        }

        System.out.println(total);
    }
}
