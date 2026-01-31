package study.ywork.mapstruct.model;

// 演示builder模式的转换，注意必须满足通用的模式标准
public class StudentBuilder {
    private final String name;
    private final int id;

    protected StudentBuilder(StudentBuilder.Builder builder) {
        this.name = builder.name;
        this.id = builder.id;
    }

    public static StudentBuilder.Builder builder() {
        return new StudentBuilder.Builder();
    }

    public static class Builder {
        private String name;
        private int id;

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public StudentBuilder create() {
            return new StudentBuilder(this);
        }
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
