package study.ywork.basis.regex;

import java.util.Arrays;

class StringSplit {
    public static void main(String[] args) {
        String input = "green-red-blue-yellow";
        Arrays.stream(input.split("-")).forEach(System.out::println);
    }
}