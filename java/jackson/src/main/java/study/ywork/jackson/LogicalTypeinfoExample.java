package study.ywork.jackson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeId;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.ObjectMapper;

/*
 * 多态类的序列化需要指定具体的类信息
 * 一种方式是在基类进行类信息注解，一种是在属性上注解
 * 技术都是增加属性名称：使用逻辑类型名称
 * 一般用于非Java客户端
 */
public class LogicalTypeinfoExample {
    public static void main(String[] args) throws IOException {
        View v = new View();
        v.setShapes(new ArrayList<>(List.of(Rectangle.of("rectangle", 3, 6), Circle.of(5))));

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
     * 指定子类的名称一种是使用@JsonSubTypes注解指定，另一种就是子类上使用@JsonTypeName注解指定
     * 默认的属性名称为'@type'，可以自行指定 一样可以采用和ClassTypeinfoExample例子中的include值
     */
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT, property = "yytype")
    @JsonSubTypes({ @JsonSubTypes.Type(value = Rectangle.class), @JsonSubTypes.Type(value = Circle.class) })
    private abstract static class Shape {
    }

    @SuppressWarnings("unused")
    @JsonTypeName("rectangle")
    private static class Rectangle extends Shape {
        // 可以指定逻辑类型名称
        @JsonTypeId
        private String typeId;
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

        public static Rectangle of(String typeId, Integer w, Integer h) {
            Rectangle r = new Rectangle();
            r.setTypeId(typeId);
            r.setW(w);
            r.setH(h);
            return r;
        }

        public String getTypeId() {
            return typeId;
        }

        public void setTypeId(String typeId) {
            this.typeId = typeId;
        }

        @Override
        public String toString() {
            return "Rectangle [typeId=" + typeId + ", w=" + w + ", h=" + h + "]";
        }
    }

    @SuppressWarnings("unused")
    @JsonTypeName("circle")
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
