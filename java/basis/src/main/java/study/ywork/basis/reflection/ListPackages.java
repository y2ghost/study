package study.ywork.basis.reflection;

public class ListPackages {
    public static void main(String[] argv) {
        java.lang.Package[] all = java.lang.Package.getPackages();
        for (int i = 0; i < all.length; i++) {
            System.out.println(all[i]);
        }
    }
}
