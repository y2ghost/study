package ywork.script

import ywork.domain.Employee

def employee1 = new Employee();
// 调用Setter方法
employee1.name = "yy1"
employee1.dept = "IT1"
// 调用Getter方法
println employee1.name
println employee1.dept

def employee2 = new Employee();
// 属性直接更改
employee2.@name = "yy2"
employee2.@dept = "IT2"
// 属性直接读取
println employee2.@name
println employee2.@dept
