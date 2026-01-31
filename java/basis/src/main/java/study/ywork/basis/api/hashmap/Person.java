package study.ywork.basis.api.hashmap;

public class Person {
    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Person)) {
            return false;
        }

        Person person = (Person) o;
        if (age != person.age) {
            return false;
        }

        return name.equals(person.name);
    }

    /*
     * name相同的情况下根据age来区分哈希码组，会有4个，也就是bucket Array的大小为4
     */
    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (age / 4);
        return result;
    }

    @Override
    public String toString() {
        return "Person [name=" + name + ", age=" + age + "]";
    }
}
