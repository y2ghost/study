package ywork.script
// 函数返回值不需要使用return关键字
// 直接根据最后一行语句的表达式决定返回值
int findFactorial(int num) {
    num == 1 ? num : num * findFactorial(num - 1);
}

println findFactorial(6)

// void类型返回值为null
void test() {
    println "inside test method"
}

println test()
