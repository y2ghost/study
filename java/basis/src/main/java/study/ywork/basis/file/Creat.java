package study.ywork.basis.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Creat {
    public static void main(String[] argv) throws IOException {
        if (argv.length == 0) {
            throw new IllegalArgumentException("Usage: Creat filename [...]");
        }

        for (String arg : argv) {
            final Path p = Path.of(arg);
            final Path created = Files.createFile(p);
            System.out.println(created);
        }
    }
}
