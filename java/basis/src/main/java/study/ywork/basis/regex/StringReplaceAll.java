package study.ywork.basis.regex;

public class StringReplaceAll {
    public static void main(String[] args) {
        String input = "Let''''''s learn::: how to    write cool regex...";
        input = input.replaceAll("(\\W)\\1+", "$1");
        System.out.printf("Replaced result: %s%n", input);
    }
}
