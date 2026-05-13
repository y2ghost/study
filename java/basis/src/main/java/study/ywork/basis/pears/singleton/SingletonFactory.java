package study.ywork.basis.pears.singleton;

/* 单例工厂的方式：适合使用函数引用、不变API、编写范型函数 */
public class SingletonFactory {
    private static final SingletonFactory INSTANCE = new SingletonFactory();

    public static SingletonFactory getInstance() {
        return INSTANCE;
    }

    private SingletonFactory() {
        Long sum = 0L;
        for (int i = 0; i<= Integer.MAX_VALUE; i++) {
            sum += i;
        }
        
        System.out.println(sum);
        // DO NOTHING
    }
}
