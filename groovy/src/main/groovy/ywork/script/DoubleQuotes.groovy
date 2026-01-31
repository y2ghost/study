package ywork.script
// 双引号字符串如果没有${}占位符，则类型是java.lang.String
// 否则类型是groovy.lang.GString
def b = true
def date = java.time.LocalDate.now()
def s = "${b}, here's the date: ${date} "
println s
println s.getClass()

String name = "Edward Campbell"
println "Employee's bytes: $name.bytes"
println name.getClass()

def num = 10
println "result = ${2 * 5 * num}"

def num2 = 10 // 转义${}
println "result = \${num2}"
