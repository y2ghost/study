package ywork.client;

import com.util.PropertyUtil;
import java.util.Map;

public class AppMain {
    public static void main(String[] args) throws IllegalAccessException {
        MyObj obj = new MyObj(4, "test string");
        Map<String, Object> map = PropertyUtil.getProperties(obj);
        System.out.println(map);
    }
}

