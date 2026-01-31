package study.ywork.basis.functional;

public class ReferencesDemo2 {
    void cloz() {
        System.out.println("Stand-in close() method called");
    }

    public static void main(String[] args) throws Exception {
        ReferencesDemo2 rd2 = new ReferencesDemo2();
        try (AutoCloseable autoCloseable = rd2::cloz) {
            System.out.println("Some action happening here.");
        }
    }
}

