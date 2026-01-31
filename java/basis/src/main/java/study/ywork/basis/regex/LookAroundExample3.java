package study.ywork.basis.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class LookAroundExample3 {
    public static void main(String[] args) {
        final Pattern p = Pattern.compile("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*\\W)\\S{6,12}$");
        String[] inputs = { "abZ#45", "$$$f5P###", "abc123", "xyz-7612", "AbC@#$qwer", "xYz@#$ 1278" };

        for (String s : inputs) {
            Matcher m = p.matcher(s);
            System.out.printf("%s => %s%n", s, m.matches());
        }
    }
}
