package study.ywork.basis.file;

import java.nio.file.FileSystems;

public class ListRoots {
    public static void main(String[] args) {
        FileSystems.getDefault().getRootDirectories().forEach(System.out::println);
    }
}
