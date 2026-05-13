package study.ywork.basis.regex;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatchResultDemo {
    private static final String REGEX = "HTTP/1\\.[01] (\\d+) [a-zA-Z]+";
    private static final String TEST_STRING = "HTTP/1.1 302 Found";
    private static final Pattern PATTERN = Pattern.compile(REGEX);

    public static void main(String[] args) {
        Matcher m = PATTERN.matcher(TEST_STRING);
        if (m.matches()) {
            MatchResult mr = m.toMatchResult();
            System.out.println("groupCount(): " + mr.groupCount());
            System.out.println("group(): " + mr.group());
            System.out.println("start(): " + mr.start());
            System.out.println("end(): " + mr.end());
            System.out.println("group(1): " + mr.group(1));
            System.out.println("start(1): " + mr.start(1));
            System.out.println("end(1): " + mr.end(1));
        }
    }
}