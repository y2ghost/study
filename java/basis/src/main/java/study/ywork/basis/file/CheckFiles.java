package study.ywork.basis.file;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CheckFiles {
    public static void main(String[] argv) {
        System.out.println("CheckFiles starting.");
        CheckFiles cf = new CheckFiles();

        try {
            var filesToCheck = cf.getListFromConfigFile();
            var filesThatExist = cf.getListFromDirectory();
            cf.reportMissingFiles(filesToCheck, filesThatExist);
            System.out.println("CheckFiles done.");
        } catch (FileNotFoundException e) {
            System.err.println("Can't open file list file.");
        } catch (IOException e) {
            System.err.println("Error reading file list");
        }
    }

    public static final String FILE_NAME = "files.txt";

    protected List<String> getListFromConfigFile() throws IOException {
        var list = new ArrayList<String>();
        try (var stream = Files.lines(Path.of(FILE_NAME))) {
            stream.forEach(list::add);
        }

        return list;
    }

    protected List<String> getListFromDirectory() {
        String[] array = new java.io.File(".").list();
        return null != array ? Arrays.asList(array) : Collections.emptyList();
    }

    protected void reportMissingFiles(
            List<String> listFromFile, List<String> listFromDir) {

        for (String name : listFromFile) {
            if (!listFromDir.contains(name))
                System.err.println("File " + name + " missing.");
        }
    }
}
