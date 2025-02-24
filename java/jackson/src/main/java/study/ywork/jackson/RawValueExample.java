package study.ywork.jackson;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

/*
 * 使用@JsonRawValue按原样序列化属性，不进行转义或是引号处理
 * 适合本身就是JSON序列化的内容这样的场景
 */
public class RawValueExample {
    public static void main(String[] args) throws IOException {
        Report r1 = new Report();
        r1.setId(1);
        r1.setName("Test report1");
        r1.setContent("\"data\"");
        processReport(r1);

        Report r2 = new Report();
        r2.setId(2);
        r2.setName("Test report2");
        r2.setContent("{\"author\":\"Peter\", \"content\":\"Test content\"}");
        processReport(r2);
    }

    private static void processReport(Report r) throws IOException {
        System.out.println("-- 序列化之前 --");
        System.out.println(r);

        ObjectMapper om = new ObjectMapper();
        String jsonString = om.writeValueAsString(r);
        System.out.println("-- 序列化之后 --");
        System.out.println(jsonString);
        System.out.println("-- 序列化结束 --\n");
    }

    @SuppressWarnings("unused")
    private static class Report {
        private long id;
        private String name;
        @JsonRawValue
        private String content;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        @Override
        public String toString() {
            return "Report [id=" + id + ", name=" + name + ", content=" + content + "]";
        }
    }
}
