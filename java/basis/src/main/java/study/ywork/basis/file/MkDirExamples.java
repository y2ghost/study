package study.ywork.basis.file;

import java.io.File;
import java.nio.file.Path;

public class MkDirExamples {
    private static final String ROOT_DIR = ".";
    private static final String SRC_DIR_NAME = "src";
    private static final String BIN_DIR_NAME = "bin";

    public static void main(String[] args) {
        boolean status = new File(Path.of(ROOT_DIR, BIN_DIR_NAME).toString()).mkdir();
        report(status);

        status = new File(Path.of(ROOT_DIR, SRC_DIR_NAME).toString()).mkdir();
        report(status);

        status = new File(Path.of(ROOT_DIR, SRC_DIR_NAME, BIN_DIR_NAME).toString()).mkdir();
        report(status);

        status = new File(Path.of(ROOT_DIR, BIN_DIR_NAME, SRC_DIR_NAME).toString()).mkdirs();
        report(status);
    }

    static void report(boolean b) {
        System.out.println(b ? "success" : "failure");
    }
}
