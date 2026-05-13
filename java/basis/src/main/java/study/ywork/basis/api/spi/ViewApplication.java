package study.ywork.basis.api.spi;

import java.util.ServiceLoader;

// 演示SPI实现类的加载和使用例子
public class ViewApplication {
    public static void main(String[] args) {
        ServiceLoader<MyView> views = ServiceLoader.load(MyView.class);
        views.forEach(v -> System.out.println(v.getName()));
    }
}
