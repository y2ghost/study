package ywork.script

println '强制操作符(as)示例'
String s = "1.1"
BigDecimal bd = s as BigDecimal;
println bd
println bd.class.name

println '强制操作符本质是调用对象的asType方法'
println '自定义类实现asType方法示例'
class Fraction {
    int numerator;
    int denominator;

    Fraction(int numerator, int denominator) {
        this.numerator = numerator
        this.denominator = denominator
    }

    def asType(Class target) {
        switch (target) {
            case BigDecimal.class:
                return BigDecimal.valueOf((double) numerator / (double) denominator)
            case String.class:
                return numerator + "/" + denominator
            case Double.class:
                return Double.valueOf((double) numerator / (double) denominator)
            default:
                throw new RuntimeException("not supported: "+target);
        }
    }
}

def f = new Fraction(3, 11);
def result = f as BigDecimal
println result

result = f as Double
println result

result = f as String
println result
