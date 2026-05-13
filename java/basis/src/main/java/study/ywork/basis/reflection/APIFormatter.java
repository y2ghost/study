package study.ywork.basis.reflection;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

public abstract class APIFormatter {
    protected static boolean doingStandardClasses = true;

    protected int doArgs(String[] argv) throws IOException {
        int n = 0;
        if (argv.length == 0) {
            String s = System.getProperty("java.class.path");
            String pathSep = System.getProperty("path.separator");
            StringTokenizer st = new StringTokenizer(s, pathSep);

            while (st.hasMoreTokens()) {
                String thisFile = st.nextToken();
                System.err.println("Trying path " + thisFile);
                if (thisFile.endsWith(".zip") || thisFile.endsWith(".jar"))
                    processOneZip(thisFile);
            }
        } else {
            for (int i = 0; i < argv.length; i++)
                processOneZip(argv[i]);
        }

        return n;
    }

    public void processOneZip(String fileName) throws IOException {
        List<ZipEntry> entries = new ArrayList<>();
        try (ZipFile zipFile = new ZipFile(new File(fileName))) {
            Enumeration<? extends ZipEntry> all = zipFile.entries();

            while (all.hasMoreElements()) {
                ZipEntry zipEntry = all.nextElement();
                entries.add(zipEntry);
            }

        } catch (ZipException zz) {
            throw new FileNotFoundException(zz + fileName);
        }

        Collections.sort(entries, (o1, o2) -> o1.getName().compareTo(o2.getName()));
        for (ZipEntry zipEntry : entries) {
            String zipName = zipEntry.getName();
            if (zipEntry.isDirectory() || !zipName.endsWith(".class")) {
                continue;
            }

            String className = zipName.replace('/', '.').
                    substring(0, zipName.length() - 6);    // 6 for ".class"
            Class<?> c = null;
            try {
                c = Class.forName(className);
            } catch (ClassNotFoundException ex) {
                System.err.println("Error: " + ex);
            }

            doClass(c);
        }
    }

    protected abstract void doClass(Class<?> c) throws IOException;
}
