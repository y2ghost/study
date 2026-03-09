package study.ywork.basis.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Delete2 {
    static boolean hard = false;

    public static void main(String[] argv) {
        for (String arg : argv) {
            if ("-h".equals(arg)) {
                hard = true;
                continue;
            }
            delete(arg);
        }
    }

    private static void deleteError(String fileName, Object e) {
        System.out.println("Deleting " + fileName + " threw " + e);
    }

    public static void delete(String fileName) {
        final Path target = Path.of(fileName);
        if (hard) {
            try {
                System.out.print("Using Files.delete(): ");
                Files.delete(target);
                System.err.println("** Deleted " + fileName + " **");
            } catch (IOException e) {
                deleteError(fileName, e);
            }
        } else {
            try {
                System.out.print("Using deleteIfExists(): ");
                if (Files.deleteIfExists(target)) {
                    System.out.println("** Deleted " + fileName + " **");
                } else {
                    deleteError(fileName, Boolean.FALSE);
                }
            } catch (IOException e) {
                deleteError(fileName, e);
            }
        }
    }
}
