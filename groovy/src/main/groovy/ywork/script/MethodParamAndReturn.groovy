package ywork.script
// 函数定义的返回值必须是明确的类型或者def关键字
int sum1(int x, int y) {
    x + y;
}

println sum1(1, 3)

def sum2(def x, def y) {
    x + y;
}

println sum2(1, 3)

def sum3(x, y) {
    x + y;
}

println sum3(1, 3)
