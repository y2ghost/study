package study.ywork.basis.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TempFiles {
    public static void main(String[] argv) throws IOException {
        File backup = new File("Rename.java~");
        backup.deleteOnExit();
        Path tmp = Files.createTempFile("foo", "tmp");
        System.out.println("Your temp file is " + tmp.normalize());
        tmp.toFile().deleteOnExit();
        writeDataInTemp(tmp);
    }

    public static void writeDataInTemp(Path tempFile) throws IOException {
        Files.writeString(tempFile, "This is a temp file");
    }
}

