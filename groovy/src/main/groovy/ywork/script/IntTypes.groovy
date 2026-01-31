package ywork.script
// 根据值决定数值类型
def i = 1;
println i.getClass().getName()

def j = 2147483648; //Integer.MAX_VALUE+1
println j.getClass().getName()

def k =  9223372036854775808; // Long.MAX_VALUE+1
println k.getClass().getName()

// Decimal类型
def d = 4.1
println d.getClass().getName()

// 整数类型后缀
i = 2
println "2 = ${i.getClass().getName()}"

def a = 2G
println "2G = ${a.getClass().getName()}"

def b = 2L
println "2L = ${b.getClass().getName()}"

def c = 2I
println "2I = ${c.getClass().getName()}"

// 浮点数类型后缀
i = 2.1
println "2.1 = ${i.getClass().getName()}"

a = 2.1G
println "2.1G = ${a.getClass().getName()}"

b = 2.1D
println "2.1D = ${b.getClass().getName()}"

c = 2.1F
println "2.1F = ${c.getClass().getName()}"

//non fractional values
d = 2D
println "2D = ${d.getClass().getName()}"

def e = 2F
println "2F = ${e.getClass().getName()}"
