package ywork.script
import java.lang.reflect.Modifier

println '自动化处理类的属性示例'
println '如果定义类属性，没有定义任何的权限修饰符'
println '那么默认private，自动添加Getter和Setter方法'
class People1 {
    String name;
    int age;
    // 定义为final表示只读属性
    final String address;

    People1(String address) {
        this.address = address
    }
}

println '检查默认权限修饰符'
def modifiers = People1.class.getDeclaredField("name").getModifiers()
println Modifier.toString(modifiers)
println '可以通过Setter或是属性设置值'
def p = new People1("yueyang city");
p.setName("yy")
p.name = "yy"
p.setAge(25)
p.age = 25
println '可以通过Getter或是属性名称获取值'
println p.getName() + p.name
println p.getAge() + p.age
println p.getAddress() + p.address
println '不可以执行p.SetAddress()操作'
println '类可以没有属性，只要有Setter和Getter方法'
println '一样可以执行上面类似的操作'
class People2 {
    Map<String, Object> map = new HashMap<>();

    String getName(){
        return "tt";
    }

    def getAge(){
        return map.get("age")
    }

    void setAge(int age){
        map.put("age", age);
    }
}

p = new People2();
p.age = 25
println p.age
print p.name
