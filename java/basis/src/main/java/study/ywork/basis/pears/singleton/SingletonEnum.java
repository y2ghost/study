package study.ywork.basis.pears.singleton;

/*
 * 枚举自动实现了序列化的功能
 * 不适合需要继承某个父类的情况
 */
public enum SingletonEnum {
    INSTANCE;

    public void doSomething() {
        // DO THINGS HERE
    }
}
