C语言代码示例

编译技巧概述
- ```bash
- # 输出预处理结果，可以使用- P参数屏蔽无用信息
- gcc -E function.c -o function.i
- # 输出汇编结果
- gcc -S -masm=att function.c  -o function.s
- gcc -S -masm=intel function.c -o function.s
- # 输出二进制代码结果
- gcc -c function.c -o function.o
- objdump -D function.o
- objdump -D -M intel function.o
- # 静态库编译需要glibc-static支持
- gcc -static main.cpp
- ```
