package ywork.script
// 定义了全局变量，任意类型
// 全局变量维护在groovy.lang.Binding对象里面，类似
// Java的Map对象
a = 2
printf "%s - %s%n", a.getClass().getName(), a
a = "apple"
printf "%s - %s%n", a.getClass().getName(), a

void printVars() {
    println a;
    // 定义了全局变量
    b = 3;
}

printVars();
print b;
