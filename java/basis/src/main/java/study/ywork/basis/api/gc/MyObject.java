package study.ywork.basis.api.gc;

// 用来测试GC的类
@SuppressWarnings("unused")
public class MyObject {
    private int[] ints = new int[1000];
    private final String name;

    public MyObject(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
