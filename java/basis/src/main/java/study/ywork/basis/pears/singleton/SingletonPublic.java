package study.ywork.basis.pears.singleton;

/* 最简单的单例对象方式：直接访问INSTANCE成员即可 */
public class SingletonPublic {

    public static final SingletonPublic INSTANCE = new SingletonPublic();

    private SingletonPublic() {
        // DO NOTHING
    }
}
