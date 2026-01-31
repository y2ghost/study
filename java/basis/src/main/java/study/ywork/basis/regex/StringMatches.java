package study.ywork.basis.regex;

public class StringMatches {
    public static void main(String[] args) {
        String input = "Sky is blue";
        String regex = "\\b(red|blue|green)\\b";
        boolean result = input.matches(regex);
        System.out.printf("Match result: %s%n", result);

        regex = ".*\\b(red|blue|green)\\b.*";
        result = input.matches(regex);
        System.out.printf("Match result: %s%n", result);
    }
}
