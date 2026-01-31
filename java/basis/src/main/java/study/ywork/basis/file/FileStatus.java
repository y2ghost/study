package study.ywork.basis.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.Date;

public class FileStatus {
    public static void main(String[] argv) throws IOException {
        if (argv.length == 0) {
            System.err.println("Usage: FileStatus filename");
            System.exit(1);
        }

        for (String a : argv) {
            status(a);
        }
    }

    public static void status(String fileName) throws IOException {
        System.out.println("---" + fileName + "---");
        Path p = Path.of(fileName);

        if (!Files.exists(p)) {
            System.out.println("file not found");
            System.out.println();    // Blank line
            return;
        }

        System.out.println("Canonical name " + p.normalize());
        Path parent = p.getParent();

        if (parent != null) {
            System.out.println("Parent directory: " + parent);
        }

        if (Files.isReadable(p)) {
            System.out.println(fileName + " is readable.");
        }

        if (Files.isWritable(p)) {
            System.out.println(fileName + " is writable.");
        }


        if (Files.isRegularFile(p)) {
            System.out.printf("File size is %d bytes, content type %s%n",
                    Files.size(p),
                    Files.probeContentType(p));
        } else if (Files.isDirectory(p)) {
            System.out.println("It's a directory");
        } else {
            System.out.println("I dunno! Neither a file nor a directory!");
        }

        final FileTime d = Files.getLastModifiedTime(p);
        System.out.println("Last modified " + d);
        System.out.println();
    }

    public static void statusLegacy(String fileName) throws IOException {
        System.out.println("---" + fileName + "---");
        File f = new File(fileName);

        if (!f.exists()) {
            System.out.println("file not found");
            System.out.println();    // Blank line
            return;
        }

        System.out.println("Canonical name " + f.getCanonicalPath());
        String p = f.getParent();

        if (p != null) {
            System.out.println("Parent directory: " + p);
        }

        if (f.canRead()) {
            System.out.println("File is readable.");
        }

        if (f.canWrite()) {
            System.out.println("File is writable.");
        }

        Date d = new Date(f.lastModified());
        System.out.println("Last modified " + d);

        if (f.isFile()) {
            System.out.println("File size is " + f.length() + " bytes.");
        } else if (f.isDirectory()) {
            System.out.println("It's a directory");
        } else {
            System.out.println("I dunno! Neither a file nor a directory!");
        }

        System.out.println();
    }
}
