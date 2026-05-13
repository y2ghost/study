package study.ywork.spring.example.databinder;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.validation.DataBinder;

/*
 * 绑定出错，回反馈错误原因例子
 */
public class BindingResultDemo {
    public static void main(String[] args) {
        MutablePropertyValues mpv = new MutablePropertyValues();
        mpv.add("anInt", "10x");

        TestBean testBean = new TestBean();
        DataBinder db = new DataBinder(testBean);

        db.bind(mpv);
        db.getBindingResult().getAllErrors().stream().forEach(System.out::println);
        System.out.println(testBean);
    }
}
