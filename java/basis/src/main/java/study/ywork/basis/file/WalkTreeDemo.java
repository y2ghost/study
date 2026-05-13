package study.ywork.basis.file;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.MessageFormat;

public class WalkTreeDemo {
    public static void main(String[] args) throws IOException {
        String dir = args.length == 0 ? "." : args[0];
        Files.walkFileTree(Path.of(dir), myVisitor);
    }

    static final FileVisitor<Path> myVisitor = new FileVisitor<>() {
        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
            System.out.println("Starting Directory " + dir);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException ex) {
            System.out.println("Finished Directory " + dir +
                    " " + (ex != null ? ex : ""));
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
            System.out.println("Visiting File " + file);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) {
            System.out.println(MessageFormat.format("FAILURE visiting {0} ({1})", file, exc));
            return FileVisitResult.CONTINUE;
        }
    };
}
