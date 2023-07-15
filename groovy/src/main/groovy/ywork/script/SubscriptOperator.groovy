package ywork.script

println '列表下标运算符示例'
def list1 = [1, 4, 6, 8]
println list1[0]
list1[0] = 2
println list1

println 'Map下标运算符示例'
def m = [x: 2, y: 4]
println m['x']
println m

println '使用范围的下标示例'
def list2 = list1[0..2]
println list2
list1[0..3] = [1, 3, 5, 7]
println list1

println '实现getAt/putAt方法的类可以使用下标运算符'
class Person2 {
    String name;
    int age;

    Person2(String name, int age) {
        this.name = name
        this.age = age
    }

    def getAt(int i) {
        switch (i) {
            case 0: return name
            case 1: return age
        }
    }

    void putAt(int i, def value) {
        switch (i) {
            case 0: name = value; break
            case 1: age = value; break
        }
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}

def p = new Person2("yy", 31);
println p[0]
println p[1]
p[0] = "tt"
p[1] = 25
println p
