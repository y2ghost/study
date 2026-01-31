package study.ywork.basis.file;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;

public class FNFilter {
    private static final String[] SUFFIXES = {".java", ".class", ".jar"};

    public static void main(String[] args) {
        String[] dirs = new java.io.File(".").list(new OnlyJava());
        Arrays.sort(dirs);

        for (String d : dirs) {
            System.out.println(d);
        }
    }

    private static class OnlyJava implements FilenameFilter {
        public boolean accept(File dir, String s) {
            return Arrays.stream(SUFFIXES).anyMatch(s::endsWith);
        }
    }
}
