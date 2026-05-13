package study.ywork.basis.reflection.showlet;

public class MyBigApp {
    public static void main(String[] args) throws Exception {
        String className = "study.ywork.basis.reflection.showlet.IanShow";
        Class<?> c = Class.forName(className);
        Showlet s = (Showlet) c.getDeclaredConstructor().newInstance();
        s.show();
    }
}
