package study.ywork.jackson;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;
import java.io.IOException;

/*
 * 使用@JsonAnyGetter注解序列化任何任意属性
 * 功能类似@JsonUnwrapped，但能处理任意类型，不仅仅嵌套类型
 */
public class AnyGetterExample {
    public static void main(String[] args) throws IOException {
        ScreenInfo si = new ScreenInfo();
        si.setId("TradeDetails");
        si.setTitle("Trade Details");
        si.setWidth(500);
        si.setHeight(300);
        si.addOtherInfo("xLocation", 400);
        si.addOtherInfo("yLocation", 200);

        System.out.println("-- 序列化之前 --");
        System.out.println(si);

        ObjectMapper om = new ObjectMapper();
        String jsonString = om.writeValueAsString(si);
        System.out.println("-- 序列化之后 --");
        System.out.println(jsonString);
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

        @JsonAnyGetter
        public Map<String, Object> getOtherInfo() {
            return otherInfo;
        }

        public void addOtherInfo(String key, Object value) {
            if (this.otherInfo == null) {
                this.otherInfo = new HashMap<>();
            }

            this.otherInfo.put(key, value);
        }

        @Override
        public String toString() {
            return "ScreenInfo [id=" + id + ", title=" + title + ", width=" + width + ", height=" + height
                + ", otherInfo=" + otherInfo + "]";
        }
    }

}
