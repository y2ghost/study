package util.common;

import java.util.Arrays;
import java.util.List;

public class StringUtil {
    public static List<String> split(String str, String expr) {
        return Arrays.asList(str.split(expr));
    }
}

