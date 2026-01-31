package study.ywork.jackson;

import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/*
 * 序列化为父类
 */
public class SupertypeExample {
    public static void main(String[] args) throws IOException {
        System.out.println("-- 序列化 --");
        View view = new View();
        view.setRoundRectangle(RoundRectangle.of(5, 7, 2));
        System.out.println("Java对象: " + view);

        ObjectMapper om = new ObjectMapper();
        String jsonString = om.writeValueAsString(view);
        System.out.println("JSON字符串: " + jsonString);

        System.out.println("-- 反序列化 --");
        View view2 = om.readValue(jsonString, View.class);
        System.out.println("Java对象: " + view2);
    }

    @SuppressWarnings("unused")
    private static class Rectangle {
        private Integer width;
        private Integer height;

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

        @Override
        public String toString() {
            return "Rectangle [width=" + width + ", height=" + height + "]";
        }
    }

    @SuppressWarnings("unused")
    private static class RoundRectangle extends Rectangle {
        private Integer arcWidth;

        public Integer getArcWidth() {
            return arcWidth;
        }

        public void setArcWidth(Integer arcWidth) {
            this.arcWidth = arcWidth;
        }

        public static RoundRectangle of(Integer w, Integer h, Integer arc) {
            RoundRectangle rr = new RoundRectangle();
            rr.setWidth(w);
            rr.setHeight(h);
            rr.setArcWidth(arc);
            return rr;
        }

        @Override
        public String toString() {
            return "RoundRectangle{" + "arcWidth=" + arcWidth + ", " + super.toString() + '}';
        }
    }

    @SuppressWarnings("unused")
    private static class View {
        @JsonSerialize(as = Rectangle.class)
        private RoundRectangle roundRectangle;

        public RoundRectangle getRoundRectangle() {
            return roundRectangle;
        }

        public void setRoundRectangle(RoundRectangle roundRectangle) {
            this.roundRectangle = roundRectangle;
        }

        @Override
        public String toString() {
            return "View{" + "roundRectangle=" + roundRectangle + '}';
        }
    }
}
