package study.ywork.basis.functional;

import java.util.Arrays;
import java.util.Spliterator;

public class SpliteratorDemo {
    static int[] data;

    public static void main(String[] args) {
        Spliterator<int[]> spl;
        spl = Arrays.asList(data).spliterator();
        spl.characteristics();
    }
}
