package ywork.script

println '范围操作符示例'
def numbers = 1..5
println numbers
println numbers.getClass()
for (def n: numbers) {
    println n;
}

println '逆序示例'
numbers = 5..1
for (def n: numbers) {
    println n;
}

// 支持实现了Comparable接口的对象
def alphas = 'a'..'d'
println alphas.getClass()
for (def a : alphas) {
    println a
}

// 月份范围示例
import java.time.Month
def monthRange = Month.JANUARY..Month.APRIL
for (def month : monthRange) {
    println month
}

println '自定义支持范围操作的类'
class EvenNumber implements Comparable<EvenNumber> {
    private int num;

    EvenNumber(int num) {
        this.num = num;
    }

    public EvenNumber next() {
        return new EvenNumber(num + 2);
    }

    public EvenNumber previous() {
        return new EvenNumber(num - 2);
    }

    @Override
    int compareTo(EvenNumber o) {
        return Integer.compare(num, o.num);
    }

    @Override
    public String toString() {
        return "EvenNumber [num=" + num + "]";
    }
}

def theRange = new EvenNumber(4)..new EvenNumber(8)
for (def r : theRange) {
    println r
}

println '-- 自定义EvenNumber逆序例子 --'
theRange = new EvenNumber(8)..new EvenNumber(4)
for (def r : theRange) {
    println r
}

println 'a..<b 范围操作符示例'
numbers = 1..<5
for (def n : numbers) {
    println n;
}

println '参数传递范围对象'
def x = 5
def y = 10
numbers = x..<y
for (def n : numbers) {
    println n;
}
