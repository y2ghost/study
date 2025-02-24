package study.ywork.jackson;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/*
 * jackson库基本功能概览
 */
public class QuickExample {
    public static void main(String[] args) {
        String json = null;
        ObjectMapper om = new ObjectMapper();
        Map<String, Integer> map = Map.of("one", 1, "two", 2);

        try {
            json = om.writeValueAsString(map);
            System.out.println(json);
            map = om.readValue(json, new TypeReference<Map<String, Integer>>() {
            });
            System.out.println(map);
            System.out.println(map.getClass());
        } catch (JsonProcessingException e) {
            System.err.println(e);
        }

        MyObject pojo = new MyObject();
        pojo.setAge(33);
        pojo.setName("yy");
        pojo.setAdresses(List.of("add1", "add2"));

        try {
            om.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            json = "{\"test\":0,\"age\":33,\"name\":\"yy\",\"adresses\":[\"add1\",\"add2\"]}";
            pojo = om.readValue(json, MyObject.class);
            System.out.println(json);
            System.out.println(pojo);
        } catch (JsonProcessingException e) {
            System.err.println(e);
        }

        List<MyObject> mine = List.of(pojo);
        try {
            // 美化输出JSON字符串，默认没有
            om.enable(SerializationFeature.INDENT_OUTPUT);
            json = om.writeValueAsString(mine);
            System.out.println(json);
            mine = om.readValue(json, new TypeReference<List<MyObject>>() {
            });
            System.out.println(mine);
        } catch (JsonProcessingException e) {
            System.err.println(e);
        }

        try {
            File tempFile = File.createTempFile("jackson-", ".txt");
            tempFile.deleteOnExit();
            System.out.println("-- 保存到临时文件 --");
            System.out.println(tempFile);
            om.writeValue(tempFile, pojo);
            System.out.println("-- 从临时文件读取到字符串对象中 --");
            json = new String(Files.readAllBytes(tempFile.toPath()));
            System.out.println(json);
            System.out.println("-- 从临时文件读取到MyObject对象中 --");
            pojo = om.readValue(tempFile, MyObject.class);
            System.out.println(pojo);
        } catch (IOException e) {
            System.err.println(e);
        }

        try {
            // 确保URL返回的是JSON格式的数据
            URL url = new URL("http://www.weather.com.cn/data/cityinfo/101251001.html");
            Object obj = om.readValue(url, Object.class);
            System.out.println(obj);
            System.out.println(obj.getClass());
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    @SuppressWarnings("unused")
    private static class MyObject {
        private Integer age;
        private String name;
        private List<String> adresses;

        public Integer getAge() {
            return age;
        }

        public String getName() {
            return name;
        }

        public List<String> getAdresses() {
            return adresses;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setAdresses(List<String> adresses) {
            this.adresses = adresses;
        }

        @Override
        public String toString() {
            return "MyObject [age=" + age + ", name=" + name + ", adresses=" + adresses + "]";
        }
    }
}
