package study.ywork.spring.example.databinder;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.validation.DataBinder;

public class DataBinderDemo {
    public static void main(String[] args) {
        MutablePropertyValues mpv = new MutablePropertyValues();
        mpv.add("anInt", "10");
        TestBean testBean = new TestBean();
        DataBinder db = new DataBinder(testBean);
        db.bind(mpv);
        System.out.println(testBean);
    }
}
