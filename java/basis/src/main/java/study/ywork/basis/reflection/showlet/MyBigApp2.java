package study.ywork.basis.reflection.showlet;

public class MyBigApp2 {
    public static void main(String[] args) throws Exception {
        String className = "study.ywork.basis.reflection.showlet.IanShow";
        Class<?> c = Class.forName(className);
        Object o = c.getDeclaredConstructor().newInstance();
        if (o instanceof Showlet) {
            Showlet s = (Showlet) c.getDeclaredConstructor().newInstance();
            s.show();
        } else {
            System.err.printf("Sorry, class %s is not a Showlet%n", className);
        }
    }
}
