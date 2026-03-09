package study.ywork.jackson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.databind.ObjectMapper;

/*
 * 使用@JsonAnySetter反序列化未映射的JSON属性
 */
public class AnySetterExample {
    public static void main(String[] args) throws IOException {
        String json = "{\"id\":\"TradeDetails\",\"title\":\"Trade Details\","
            + "\"width\":500,\"height\":300,\"xLocation\":400,\"yLocation\":200}";

        System.out.println("-- 反序列化之前 --");
        System.out.println(json);

        ObjectMapper om = new ObjectMapper();
        ScreenInfo screenInfo = om.readValue(json, ScreenInfo.class);
        System.out.println("-- 反序列化之后 --");
        System.out.println(screenInfo);
    }

    @SuppressWarnings("unused")
    private static class ScreenInfo {
        private String id;
        private String title;
        private Integer width;
        private Integer height;
        private Map<String, Object> otherInfo;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Integer getWidth() {
            return width;
        }

        public void setWidth(Integer width) {
            this.width = width;
        }

        public Integer getHeight() {
            return height;
        }

        public void setHeight(Integer height) {
            this.height = height;
        }

        @JsonAnySetter
        public void addOtherInfo(String propertyKey, Object value) {
            if (this.otherInfo == null) {
                this.otherInfo = new HashMap<>();
            }

            this.otherInfo.put(propertyKey, value);
        }

        @Override
        public String toString() {
            return "ScreenInfo [id=" + id + ", title=" + title + ", width=" + width + ", height=" + height
                + ", otherInfo=" + otherInfo + "]";
        }
    }

}
