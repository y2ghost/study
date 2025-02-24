package study.ywork.basis.file;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PathsFilesDemo {
    public static void main(String[] args) throws Exception {
        Path p = Paths.get("my_junk_file");
        Files.deleteIfExists(p);
        InputStream is = PathsFilesDemo.class.getResourceAsStream("/demo.txt");
        long newFileSize = Files.copy(is, p);
        System.out.println(newFileSize);
        final Path realPath = p.toRealPath();
        System.out.println(realPath);
        realPath.forEach(System.out::println);
        Files.delete(p);
    }
}
