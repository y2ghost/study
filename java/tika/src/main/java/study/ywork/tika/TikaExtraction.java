package study.ywork.tika;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;

import java.io.File;
import java.io.IOException;

public class TikaExtraction {
    public static void main(String[] args) throws IOException, TikaException {
        File file = new File("example.txt");
        Tika tika = new Tika();
        String content = tika.parseToString(file);
        System.out.println("file content: " + content);
    }
}
