package study.ywork.jackson;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.IOException;

/*
 * 使用@JsonRootName自定义要序列化的POJO名称
 */
public class RootNameExample {
    public static void main(String[] args) throws IOException {
        PersonEntity person = new PersonEntity("yy", 33);
        System.out.println("-- 序列化之前 --");
        System.out.println(person);
        ObjectMapper om = new ObjectMapper();
        om.enable(SerializationFeature.WRAP_ROOT_VALUE);
        String jsonString = om.writeValueAsString(person);
        System.out.println("-- 序列化之后 --");
        System.out.println(jsonString);

        om.enable(DeserializationFeature.UNWRAP_ROOT_VALUE);
        PersonEntity person2 = om.readValue(jsonString, PersonEntity.class);
        System.out.println("-- 反序列化之后 --");
        System.out.println(person2);
    }

    @SuppressWarnings("unused")
    @JsonRootName("Person")
    private static class PersonEntity {
        private String name;
        private int age;

        public PersonEntity() {
        }

        public PersonEntity(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        @Override
        public String toString() {
            return "PersonEntity{" + "name='" + name + '\'' + ", age=" + age + '}';
        }
    }

}
