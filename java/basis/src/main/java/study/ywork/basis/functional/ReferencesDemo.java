package study.ywork.basis.functional;

public class ReferencesDemo {
    public void walk() {
        System.out.println("ReferencesDemo.walk(): Stand-in run method called");
    }

    public void doIt() {
        Runnable r = this::walk;
        new Thread(r).start();
    }

    public static void main(String[] args) {
        new ReferencesDemo().doIt();
    }
}

