package study.ywork.basis.functional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class UnixPipesFiltersReplacement {
    public static void main(String[] args) throws IOException {
        try (Stream<String> lines = Files.lines(Path.of(("lines.txt")))) {
            long numberLines = lines
                    .distinct()
                    .count();
            System.out.printf("lines.txt contains %d unique lines.", numberLines);
        }
    }
}
