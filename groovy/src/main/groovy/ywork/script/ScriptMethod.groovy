package ywork.script
void printMultiples(int num, int times) {
    for (int i = 1; i <= times; i++) {
        print i * num + " "
    }
}

// 脚本里面可以直接定义函数然后调用
printMultiples(3, 5)