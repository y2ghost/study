package ywork.script

println '相等判断使用==操作符，类似与equals方法'
println '如果判断对象引用是否相同，使用is方法'
def num1 = new BigDecimal(12);
def num2 = new BigDecimal(12);
println num1 == num2
println num1.equals(num2)
println num1.is(num2)
println num1.is(num1)

println '自定义类实现equals方法, is方法则是继承而来'
class YyClass {
    int num;

    YyClass(int num) {
        this.num = num
    }

    @Override
    boolean equals(Object obj) {
        return obj == null ? false : obj.num == this.num;
    }
}

def myClass1 = new YyClass(1);
def myClass2 = new YyClass(1);
println  myClass1==myClass2
println myClass1.equals(myClass2)
println myClass1.is(myClass2)
