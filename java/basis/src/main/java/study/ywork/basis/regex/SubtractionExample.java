package study.ywork.basis.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SubtractionExample {
    public static void main(String[] args) {
        final Pattern p = Pattern.compile("[\\p{Punct}&&[^+*/-]]");
        final String[] arr = new String[] { "!", "@", "#", "$", "%", "+", "-", "*", "/" };

        for (String s : arr) {
            Matcher m = p.matcher(s);
            System.out.printf("[%s] matches: %s%n", s, m.matches());
        }
    }
}