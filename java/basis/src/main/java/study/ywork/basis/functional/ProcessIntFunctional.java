package study.ywork.basis.functional;

import java.util.function.IntFunction;

public class ProcessIntFunctional {
    static int[] integers = {0, 1, 2, 3, 4, 5};

    static int doTheMath(int n, IntFunction<Integer> func) {
        return func.apply(n);
    }

    public static void main(String[] args) {
        int total = 0;
        for (int i : integers) {
            total += doTheMath(i, k -> k * k + 1);
        }
        System.out.println(total);
    }
}
