package study.ywork.basis.reflection;

public class GetPrivateClass {
    public static void main(String[] args) {
        X x = new X();
        GetPrivateClassPart2.discover(x);
    }

    private static class X {
        private int secret = 24;
    }
}
