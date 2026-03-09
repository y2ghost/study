package study.ywork.basis.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class Ls {
    public static void main(String[] args) throws IOException {
        try (Stream<Path> files = Files.list(Path.of("."))) {
            files.sorted().forEach(System.out::println);
        }
    }
}
