package study.ywork.basis.functional;

@FunctionalInterface
interface MyFunctionalInterface {
    int compute(int x);
}

public class ProcessIntegersUsingFunctional {
    static int[] integers = {1, 2, 3};

    public static void main(String[] args) {
        int total = 0;
        for (int i : integers)
            total += process(i, x -> x * x + 1);
        System.out.println("The total is " + total);
    }

    private static int process(int i, MyFunctionalInterface o) {
        return o.compute(i);
    }
}
