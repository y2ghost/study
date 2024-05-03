package ywork.script

println 'IN操作符示例，等价于contains()函数调用'
def numbers = [2, 4, 6, 8]
println 2 in numbers
println 3 in numbers
println numbers.contains(2)

println 'Map对象示例'
numbers = [two: 2, four: 4, six: 6, Eight: 8]
println 'two' in numbers
println 2 in numbers

println '自定义isCase方法实现IN操作'
class Multiple {
    int num;
    Multiple(int num) {
        this.num = num
    }

    boolean isCase(otherNum){
        return otherNum % num == 0
    }
}

def multipleOfThree = new Multiple(3);
println 6 in multipleOfThree
println 7 in multipleOfThree
