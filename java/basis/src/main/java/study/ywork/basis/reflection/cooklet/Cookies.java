package study.ywork.basis.reflection.cooklet;

public class Cookies {
    public static void main(String[] argv) {
        System.out.println("Cookies Application Version 0.0");
        String cookLetClassName = argv[0];

        try {
            @SuppressWarnings("unchecked")
            Class<CookLet> cookletClass = (Class<CookLet>) Class.forName(cookLetClassName);
            CookLet cookLet = cookletClass.getConstructor().newInstance();
            cookLet.initialize();
            cookLet.work();
            cookLet.terminate();
        } catch (Exception e) {
            System.err.println("Error " + cookLetClassName + e);
        }
    }
}

