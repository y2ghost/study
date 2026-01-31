package study.ywork.basis.system;

/*
 * 不可变的类示例
 */
public final class ImmutableClass {
    private final Integer i;
    private final String s;

    private ImmutableClass(Integer i, String s) {
        this.i = i;
        this.s = s;
    }

    public static ImmutableClass valueOf(Integer i, String s) {
        return new ImmutableClass(i, s);
    }

    public Integer getI() {
        return i;
    }

    public String getS() {
        return s;
    }

    @Override
    public String toString() {
        return "ImmutableClass [i=" + i + ", s=" + s + "]";
    }
}
