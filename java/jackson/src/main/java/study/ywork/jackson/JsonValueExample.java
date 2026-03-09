package study.ywork.jackson;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

/*
 * 使用@JsonValue注解序列化方法或字段返回的单个值
 */
public class JsonValueExample {
    public static void main(String[] args) throws IOException {
        Person person1 = new Person("yy", 33);
        process(person1);
        Person2 person2 = new Person2("yy", 33);
        process(person2);
    }

    private static void process(Object obj) throws IOException {
        System.out.println("-- 序列化之前 --");
        System.out.println(obj);
        ObjectMapper om = new ObjectMapper();
        String jsonString = om.writeValueAsString(obj);
        System.out.println("-- 序列化之后 --");
        System.out.println(jsonString);
    }

    @SuppressWarnings("unused")
    private static class Person {
        private String name;
        private Integer age;

        public Person() {
        }

        public Person(String name, Integer age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public Integer getAge() {
            return age;
        }

        @JsonValue
        public String toPersonInfo() {
            return name + ":" + age;
        }

        @Override
        public String toString() {
            return "Person{" + "name='" + name + '\'' + ", age=" + age + '}';
        }
    }

    @SuppressWarnings("unused")
    private static class Person2 {
        private String name;
        private Integer age;
        @JsonValue
        private String personInfo;

        public Person2() {
        }

        public Person2(String name, Integer age) {
            this.name = name;
            this.age = age;
            this.personInfo = name + ":" + age;
        }

        public String getName() {
            return name;
        }

        public Integer getAge() {
            return age;
        }

        @Override
        public String toString() {
            return "Person{" + "name='" + name + '\'' + ", age=" + age + '}';
        }
    }
}
