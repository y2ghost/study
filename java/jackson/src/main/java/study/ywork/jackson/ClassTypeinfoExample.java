package study.ywork.jackson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;

/*
 * 多态类的序列化需要指定具体的类信息
 * 一种方式是在基类进行类信息注解，一种是在属性上注解
 * 技术都是增加属性名称：使用类名信息
 */
public class ClassTypeinfoExample {
    public static void main(String[] args) throws IOException {
        View v = new View();
        v.setShapes(new ArrayList<>(List.of(Rectangle.of(3, 6), Circle.of(5))));

        System.out.println("-- 序列化 --");
        ObjectMapper om = new ObjectMapper();
        String s = om.writeValueAsString(v);
        System.out.println(s);

        System.out.println("-- 反序列化 --");
        View view = om.readValue(s, View.class);
        System.out.println(view);
    }

    /*
     * 基类上注解和属性上注解作用相同，选一种就可以了，同时存在是属性上的注解优先，此处就是为了学习示例
     */
    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "className")
    private abstract static class Shape {
    }

    @SuppressWarnings("unused")
    private static class Rectangle extends Shape {
        private Integer w;
        private Integer h;

        public Integer getW() {
            return w;
        }

        public void setW(Integer w) {
            this.w = w;
        }

        public Integer getH() {
            return h;
        }

        public void setH(Integer h) {
            this.h = h;
        }

        public static Rectangle of(Integer w, Integer h) {
            Rectangle r = new Rectangle();
            r.setW(w);
            r.setH(h);
            return r;
        }

        @Override
        public String toString() {
            return "Rectangle [w=" + w + ", h=" + h + "]";
        }
    }

    @SuppressWarnings("unused")
    private static class Circle extends Shape {
        Integer radius;

        public Integer getRadius() {
            return radius;
        }

        public void setRadius(Integer radius) {
            this.radius = radius;
        }

        public static Circle of(Integer radius) {
            Circle c = new Circle();
            c.setRadius(radius);
            return c;
        }

        @Override
        public String toString() {
            return "Circle{" + "radius=" + radius + '}';
        }
    }

    @SuppressWarnings("unused")
    private static class View {
        /*
         * 可以指定不同的include值，匹配不同的需求方案
         * 值的几种方式：
         * @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.WRAPPER_ARRAY)
         * @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.WRAPPER_OBJECT)
         * @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "className")
         */
        @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.WRAPPER_OBJECT)
        private List<Shape> shapes;

        public List<Shape> getShapes() {
            return shapes;
        }

        public void setShapes(List<Shape> shapes) {
            this.shapes = shapes;
        }

        @Override
        public String toString() {
            return "View{" + "shapes=" + shapes + '}';
        }
    }
}
