package ywork.script

// 静态函数引用示例
def power = Math.&pow
def result = power(2, 4)
println result

// 实例函数引用示例
def bd = BigDecimal.valueOf(4)
def pow = bd.&pow
result = pow(4)
println result

// 脚本函数引用示例
void doSomething(def param) {
    println "param: " + param
}

def m = this.&doSomething
m("test param")

// 函数引用参数示例
void printResults(List list, def param, def method){
    for (def e : list){
        println method(e, param);
    }
}

printResults([1, 3, 5, 6], 3, Math.&pow)

// 函数重载例子，运行时决定具体的函数
void doSomething(int i){
    println "integer $i"
}

void doSomething(String s){
    println "String $s"
}

m = this.&doSomething
m(4)
m("hi")

// 函数引用类型: org.codehaus.groovy.runtime.MethodClosure
m = Math.&floor
println m.getClass()
