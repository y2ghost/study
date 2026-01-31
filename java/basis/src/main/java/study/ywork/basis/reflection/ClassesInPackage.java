package study.ywork.basis.reflection;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassesInPackage {
    public static String[] getPackageContent(String packageName)
            throws IOException {
        final String packageAsDirName = packageName.replace(".", "/");
        final List<String> list = new ArrayList<>();
        final Enumeration<URL> urls =
                Thread.currentThread().
                        getContextClassLoader().
                        getResources(packageAsDirName);
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            String file = url.getFile();
            switch (url.getProtocol()) {
                case "file":
                    File dir = new File(file);
                    for (File f : dir.listFiles()) {
                        list.add(packageAsDirName + "/" + f.getName());
                    }
                    break;
                case "jar":
                    int colon = file.indexOf(':');
                    int bang = file.indexOf('!');
                    String jarFileName = file.substring(colon + 1, bang);

                    try (JarFile jarFile = new JarFile(jarFileName)) {
                        Enumeration<JarEntry> entries = jarFile.entries();
                        while (entries.hasMoreElements()) {
                            JarEntry e = entries.nextElement();
                            String jarEntryName = e.getName();
                            if (!jarEntryName.endsWith("/") &&
                                    jarEntryName.startsWith(packageAsDirName)) {
                                list.add(jarEntryName);
                            }
                        }
                    }
                    break;
                default:
                    throw new IllegalStateException(
                            "Dunno what to do with URL " + url);
            }
        }
        return list.toArray(new String[]{});
    }

    public static void main(String[] args) throws IOException {
        String[] names = getPackageContent("study.ywork.basis.reflection");
        for (String name : names) {
            System.out.println(name);
        }
        System.out.println("Done");
    }
}

