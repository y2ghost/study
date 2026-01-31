package study.ywork.tika;

import org.apache.tika.Tika;

import java.io.File;
import java.io.IOException;

public class TypeDetection {
    public static void main(String[] args) throws IOException {
        File file = new File("example.mp3");
        Tika tika = new Tika();
        String fileType = tika.detect(file);
        System.out.println(fileType);
    }
}
