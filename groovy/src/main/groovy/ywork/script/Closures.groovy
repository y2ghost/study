package ywork.script

println '学习闭包的示例'
println '闭包定义: { [closureParameters -> ] statements }'
println '注释的例子可以参考下'
// { item++ }
// { -> item++ }
// { println it }
// { it -> println it }
// { name -> println name }
// { String x, int y -> println "hey ${x} the value is ${y}" }
// { reader ->
//    def line = reader.readLine()
//    line.trim()
// }

println '闭包隐含自动参数: it'
println '闭包本质是类: groovy.lang.Closure的实例'

def listener = { e -> println "Clicked on $e.source" }
assert listener instanceof Closure
Closure callback = { println 'Done!' }
Closure<Boolean> isTextFile = { File it ->
    it.name.endsWith('.txt')
}

// 简单返回值测试
def code = { 123 }
assert code() == 123
assert code.call() == 123

// 条件判断测试
def isOdd = { int i -> i%2 != 0 }
assert isOdd(3) == true
assert isOdd.call(2) == false
def isEven = { it%2 == 0 }
assert isEven(3) == false
assert isEven.call(2) == true

println '闭包参数测试'


def closureWithOneArg = { str -> str.toUpperCase() }
assert closureWithOneArg('groovy') == 'GROOVY'

def closureWithOneArgAndExplicitType = { String str -> str.toUpperCase() }
assert closureWithOneArgAndExplicitType('groovy') == 'GROOVY'

def closureWithTwoArgs = { a,b -> a+b }
assert closureWithTwoArgs(1,2) == 3

def closureWithTwoArgsAndExplicitTypes = { int a, int b -> a+b }
assert closureWithTwoArgsAndExplicitTypes(1,2) == 3

def closureWithTwoArgsAndOptionalTypes = { a, int b -> a+b }
assert closureWithTwoArgsAndOptionalTypes(1,2) == 3

def closureWithTwoArgAndDefaultValue = { int a, int b=2 -> a+b }
assert closureWithTwoArgAndDefaultValue(1) == 3

println '闭包隐式参数测试'
def greeting = { "Hello, $it!" }
assert greeting('YY') == 'Hello, YY!'
// 上面的代码等价于下面的注释代码
// def greeting = { it -> "Hello, $it!" }
// assert greeting('YY') == 'Hello, YY!'

println '测试闭包的this值示例'
println 'getThisObject()方法返回闭包封闭类'
class Enclosing {
    void run() {
        def whatIsThisObject = { getThisObject() }
        assert whatIsThisObject() == this
        def whatIsThis = { this }
        assert whatIsThis() == this
    }
}
println '测试Enclosing'
new Enclosing().run()

class EnclosedInInnerClass {
    class Inner {
        // this指的是Inner实例
        Closure cl = { this }
    }

    void run() {
        def inner = new Inner()
        assert inner.cl() == inner
    }
}

println '测试EnclosedInInnerClass'
new EnclosedInInnerClass().run()

class NestedClosures {
    void run() {
        def nestedClosures = {
            // 虽然被闭包封闭，但封闭类是NestedClosures
            def cl = { this }
            cl()
        }

        assert nestedClosures() == this
    }
}

println '测试NestedClosures'
new NestedClosures().run()
println '-----------------'

println '测试闭包owner值示例'
println 'getOwner()方法返回闭包封闭的类型: 闭包或是类'

class Enclosing2 {
    void run() {
        // owner就是Enclosing2
        def whatIsOwnerMethod = { getOwner() }
        assert whatIsOwnerMethod() == this
        // owner一样是Enclosing2
        def whatIsOwner = { owner }
        assert whatIsOwner() == this
    }
}

println '测试Enclosing2'
new Enclosing2().run()

class EnclosedInInnerClass2 {
    class Inner {
        // owner是Inner实例
        Closure cl = { owner }
    }

    void run() {
        def inner = new Inner()
        assert inner.cl() == inner
    }
}

println '测试EnclosedInInnerClass2'
new EnclosedInInnerClass2().run()

class NestedClosures2 {
    void run() {
        def nestedClosures = {
            // owner是nestedClosures实例
            def cl = { owner }
            cl()
        }
        assert nestedClosures() == nestedClosures
    }
}

println '测试NestedClosures2'
new NestedClosures2().run()
println '-----------------'
println '测试闭包delegate值示例'
println 'getDelegate()方法返回委托的对象'
class EnclosingDelegate {
    void run() {
        def cl = { getDelegate() }
        def cl2 = { delegate }
        assert cl() == cl2()
        assert cl() == this
        def enclosed = {
            { -> delegate }.call()
        }

        assert enclosed() == enclosed
    }
}

println '测试EnclosingDelegate'
new EnclosingDelegate().run()

println '测试Delegate属性的绑定'
class MyPerson {
    String name
}

class MyThing {
    String name
}

def pp = new MyPerson(name:'yy')
def tt = new MyThing(name: 'tt')
def upperCasedName = { delegate.name.toUpperCase() }
upperCasedName.delegate = pp
assert upperCasedName() == 'YY'
upperCasedName.delegate = tt
assert upperCasedName() == 'TT'

// 动态绑定delegate的好处，默认情况下闭包解析属性的时候会使用delegate对象的
def cl = { name.toUpperCase() }
cl.delegate = pp
assert cl() == 'YY'

println '测试闭包的委托策略'
class TtPerson {
    String name
    def pretty = { "My name is $name" }
    String toString() {
        pretty()
    }
}
class TtThing {
    String name
}

println '默认的是Closure.OWNER_FIRST的委托策略'
def tpp = new TtPerson(name: 'xy')
def ttt = new TtThing(name: 'xh')

assert tpp.toString() == 'My name is xy'
// 虽然此时委托对象给了ttt，但是匹配owner:tppp属性优先
tpp.pretty.delegate = ttt
assert tpp.toString() == 'My name is xy'
// 修改委托策略匹配delegate对象优先
tpp.pretty.resolveStrategy = Closure.DELEGATE_FIRST
assert tpp.toString() == 'My name is xh'
