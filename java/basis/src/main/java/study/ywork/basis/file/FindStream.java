package study.ywork.basis.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class FindStream {
    public static void main(String[] args) {
        var dirName = "/home/test/book";
        try (Stream<Path> paths = Files.find(Path.of(dirName),
                Integer.MAX_VALUE,
                (path, attr) ->
                        attr.isRegularFile() && (path.toString().endsWith(".adoc") ||
                                path.toString().endsWith(".asciidoc"))
        )) {
            paths.forEach(System.out::println);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
