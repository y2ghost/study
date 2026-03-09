package study.ywork.basis.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class KillFilesByName {
    public static void main(String[] argv) throws IOException {
        if (argv.length != 2) {
            System.err.println("usage: KillFilesByName dirname pattern");
            System.exit(1);
        }

        File dir = new File(argv[0]);
        if (!dir.exists()) {
            System.out.println(argv[0] + " does not exist");
            return;
        }

        String pattern = argv[1];
        String[] info = dir.list();

        for (String fn : info) {
            File n = new File(argv[0] + File.separator + fn);
            if (!n.isFile() || fn.indexOf(pattern) == -1) {
                continue;
            }

            System.out.println("removing " + n.getPath());
            Files.delete(n.toPath());
        }
    }
}
