#!/bin/sh
# 直接构建程序无需使用库的方式

cc -c -g prog.c mod1.c mod2.c mod3.c
cc -g -o nolib-prog prog.o mod1.o mod2.o mod3.o
./nolib-prog
exit 0

