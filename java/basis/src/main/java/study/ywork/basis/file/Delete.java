package study.ywork.basis.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Delete {
    public static void main(String[] argv) throws IOException {
        String fileToDelete = "Delete.java~";
        System.out.println("Deleting " + fileToDelete);
        Path backup = Path.of(fileToDelete);

        if (Files.exists(backup)) {
            Files.delete(backup);
            System.out.println("Done");
        } else {
            System.out.println("File not found");
        }
    }
}

