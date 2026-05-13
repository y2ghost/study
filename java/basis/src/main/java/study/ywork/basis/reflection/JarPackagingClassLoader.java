package study.ywork.basis.reflection;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarPackagingClassLoader extends ClassLoader {
    private static final String DEMO_CLASS_TO_LOAD = "study.ywork.basis.reflection.MultiDemo";
    protected Map<String, Class<?>> loadedClassesCache = new HashMap<>();
    protected Map<File, JarFile> jarsCache = new HashMap<>();
    protected List<File> classPathEntries = new java.util.ArrayList<>();
    private static final int[] CAFE_BABE = {0xca, 0xfe, 0xba, 0xbe};

    public JarPackagingClassLoader() {
        String cPath = System.getProperty("java.class.path");
        String[] cPaths = cPath.split(":");
        for (String path : cPaths) {

            File f = new File(path);
            if (!f.exists()) {
                System.err.printf("Warning: classpath entry %s not found%n", path);
                continue;
            }

            if (f.isFile() && (f.getName().endsWith(".jar") || f.getName().endsWith(".zip"))) {
                classPathEntries.add(f);
                continue;
            }

            if (f.isDirectory()) {
                classPathEntries.add(f);
                continue;
            }

            System.err.println("warning: invalid classpath entry " + f);
        }
    }

    private byte[] getClassData(String className) throws ClassNotFoundException {
        String fileName = className.replace("\\.", "/") + ".class";
        System.out.println("Munged fileName = " + fileName);

        for (File classPathEntry : classPathEntries) {
            if (classPathEntry.getName().endsWith(".jar")) {
                JarFile jf = jarsCache.get(classPathEntry);
                if (jf == null) {
                    try {
                        jf = new JarFile(classPathEntry);
                        jarsCache.put(classPathEntry, jf);
                        JarEntry je = jf.getJarEntry(fileName);

                        if (je == null) {
                            continue;
                        }

                        if (je.getSize() > Integer.MAX_VALUE) {
                            throw new IOException("Entry way too big to be class file!");
                        }

                        int classSize = (int) je.getSize();
                        if (classSize > Integer.MAX_VALUE) {
                            throw new ClassFormatError(className + " Jar entry too big");
                        }

                        InputStream is = jf.getInputStream(je);
                        byte[] data = new byte[classSize];
                        int n = 0;

                        do {
                            n += is.read(data, n, classSize - n);
                        } while (n < classSize);

                        if (n != classSize) {
                            String mesg = "Only read %d bytes of %d".formatted(n, classSize);
                            System.err.println(mesg);
                            throw new IOException(mesg);
                        }

                        System.out.printf("[JarPackagingClassLoader: Loaded class %s from jar %s, size %d%n", className,
                                classPathEntry, data.length);
                        for (int i = 0; i < CAFE_BABE.length; i++)
                            if (data[i] != (byte) CAFE_BABE[i]) {
                                throw new IOException("Not a valid Java .class");
                            }
                        System.out.println();
                        return data;
                    } catch (IOException e) {
                        System.err.printf("Warning: ClassPath Entry %s missing/invalid (%s).", classPathEntry, e);
                    }
                }
            } else {
                File f = new File(classPathEntry, fileName);
                if (f.exists()) {
                    long dataLength = f.length();
                    byte[] bd = new byte[(int) dataLength];
                    try (InputStream is = new FileInputStream(f)) {
                        is.read(bd);
                        System.out.println("[Loaded " + className + " from file " + f.getAbsolutePath());
                        return bd;
                    } catch (IOException ex) {
                        throw new ClassNotFoundException(ex.toString());
                    }
                }
            }
        }

        throw new ClassNotFoundException(className);
    }

    @Override
    public synchronized Class<?> loadClass(String className, boolean resolve)
            throws ClassNotFoundException {
        System.out.printf("JarPackagingClassLoader.loadClass(%s)%n", className);

        if (className.startsWith("java.") || className.startsWith("javax.") ||
                className.startsWith("sun;") || className.startsWith("com.sun.")) {
            System.out.println("loadClass: SystemLoading " + className);
            return findSystemClass(className);
        }

        Class<?> c = loadedClassesCache.get(className);
        if (c == null) {
            byte[] mydata = getClassData(className);
            c = defineClass(className, mydata, 0, mydata.length);
            loadedClassesCache.put(className, c);
        } else {
            System.out.println("loadClass: found " + className + " in cache.");
        }

        if (resolve) {
            System.out.println("loadClass: calling resolveClass " + className);
            resolveClass(c);
        }
        return c;
    }

    private void makeItSo() {
        System.err.println("Would write the classes to a new Jar file here.");
    }

    public static void main(String[] args) {
        System.out.println("JarPackagingClassLoader.main()");
        JarPackagingClassLoader loader = new JarPackagingClassLoader();
        String classToLoad = args.length == 0 ? DEMO_CLASS_TO_LOAD : args[0];
        try {
            System.out.printf("Target class %s%n", classToLoad);
            Class<?> c = loader.loadClass(classToLoad, true);
            System.out.printf("Finally ready to instantiate class %s%n", classToLoad);
            Object demo = c.getDeclaredConstructor().newInstance();
            System.out.println("SUCCESS: Demo class instantiated: " + demo);
            loader.makeItSo();
        } catch (Exception e) {
            System.out.println("Something is dreadfully wrong for I feel, a deep burning pain in my sta-aa-a-ck:");
            e.printStackTrace();
        }
    }
}
