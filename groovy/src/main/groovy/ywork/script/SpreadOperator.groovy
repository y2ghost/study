package ywork.script

// 展开操作符，适用于任何实现了Iterable的对象
// 对lists的每一个元素执行操作
def lists = [[1], [10, 20, 30], [6, 8]]
def sizes = lists*.size()
println sizes

class Person {
    String name;
    int age;

    Person(String name, int age) {
        this.name = name
        this.age = age
    }
}

def persons = [
    new Person("yy", 31),
    new Person("tt", 41)
];
def names = persons*.name
println names

// 对于可迭代的对象存在Getter方法可以省略'*'
// 建议还是采用: def ages = persons*.age
def ages = persons.age
println ages

// 嵌套展开运算符
def lengths = persons*.name*.length()
println lengths

// 可以传入参数调用函数
def bds = [
    BigDecimal.valueOf(12),
    BigDecimal.valueOf(1.1)
]
def sums = bds*.add(BigDecimal.TEN)
println sums

// null安全
def lists2 = [null, [10, 20, 30], [6, 8]]
def sizes2 = lists2*.size()
println sizes2

// 用于函数参数
def sum1(int x, int y, int z) {
    return x + y + z;
}

def sum2(int[] args) {
    def result = 0;
    for (int i = 0; i < args.length; i++) {
        result += args[i]
    }

    return result;
}

def numbers = [1, 3, 5]
def result = sum1(*numbers);
println result
result = sum2(*numbers);
println result

// 扩展列表
numbers = [1,2,3]
def numbers2 = [0, *numbers, 4,5,6]
println numbers2

// 扩展Map对象
def m1 = [x: 1, y: 2]
def m2 = [a: 0, *:m1, z: 3]
println m2
