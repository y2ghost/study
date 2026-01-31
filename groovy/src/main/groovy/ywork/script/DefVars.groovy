package ywork.script
// 定义了本地的任意类型变量
def a = 2
printf "%s - %s%n", a.getClass().getName(), a
a = "hi"
printf "%s - %s%n", a.getClass().getName(), a

// 可以使用Java的类型系统，处理方式和Java一致
int b = 2
println b
// 下面的注释代码运行时出错
// b = "apple"
