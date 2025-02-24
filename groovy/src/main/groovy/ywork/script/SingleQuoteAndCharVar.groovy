package ywork.script
// 字符类型
def c1 = 'c' // 类型为String
println c1.getClass().getName()

char c2 = 'c' // 显示char类型
println c2.getClass().getName()

def c3 = (char) "c" // 类型转换
println c3.getClass().getName()

def c4 = 'c' as char // 使用as关键字
println c4.getClass().getName()

def c5 = "my string" as char // 使用第一个字符
println c5.getClass().getName()
println c5
