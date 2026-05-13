package study.ywork.basis.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface AutoImplement {
    /**
     * 将要实现的名称，有效的/唯一的java限定符名称
     */
    String as();

    /**
     * 是否基于构建器设计模式
     */
    boolean builder() default false;
}
