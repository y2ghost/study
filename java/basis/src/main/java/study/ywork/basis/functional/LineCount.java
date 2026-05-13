package study.ywork.basis.functional;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LineCount {
    public static void main(String[] args) throws IOException {
        String fileName = "lines.txt";
        BufferedReader br = new BufferedReader(new FileReader(fileName));
        long numberLines = br.lines().count();
        br.close();
        System.out.println(fileName + " contains " + numberLines + " lines.");
    }
}
