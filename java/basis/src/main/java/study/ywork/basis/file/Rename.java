package study.ywork.basis.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Rename {
    public static void main(String[] argv) throws IOException {
        final Path p = Path.of("MyCoolDocument");
        final Path oldName = Files.exists(p) ? p : Files.createFile(p);
        final Path newName = Path.of("My-doc.bak");
        Files.deleteIfExists(newName);
        Path p2 = Files.move(oldName, newName);
        System.out.println(p + " renamed to " + p2);

        Path target = Path.of("temp");
        if (!Files.isDirectory(target)) {
            Files.createDirectory(target);
        }

        try {
            Files.move(p2, target);
        } catch (java.nio.file.FileAlreadyExistsException ex) {
            System.out.println("Caught expected FileAlreadyExistsException");
        }

        Files.delete(p2);
        Files.delete(target);
    }
}

