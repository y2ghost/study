package study.ywork.basis.file;

import java.io.File;

public class DirTree {
    public static void main(String[] argv) {
        DirTree dt = new DirTree();
        if (argv.length == 0)
            dt.doDir(".");
        else for (String arg : argv) {
            dt.doDir(arg);
        }
    }

    private void doDir(String s) {
        File f = new File(s);
        if (!f.exists()) {
            System.out.println(s + " does not exist");
            return;
        }

        if (f.isFile()) {
            doFile(f);
        } else if (f.isDirectory()) {
            System.out.println("d " + f.getPath());
            String[] objects = f.list();

            for (int i = 0; i < objects.length; i++)
                doDir(s + File.separator + objects[i]);
        } else {
            System.err.println("Unknown: " + s);
        }
    }

    private static void doFile(File f) {
        System.out.println("f " + f.getPath());
    }
}
