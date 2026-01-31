package study.ywork.mapstruct.dto;

public class StudentDTO {
    private int id;
    private String name;
    private String classVal;

    private SubjectDTO subject;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassVal() {
        return classVal;
    }

    public void setClassVal(String classVal) {
        this.classVal = classVal;
    }

    public SubjectDTO getSubject() {
        return subject;
    }

    public void setSubject(SubjectDTO subject) {
        this.subject = subject;
    }
}
