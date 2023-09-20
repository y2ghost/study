#!/bin/sh
# 使用静态库的方式构造

cc -g -c mod1.c mod2.c mod3.c
ar r libdemo.a mod1.o mod2.o mod3.o
cc -g -o static-prog prog.c libdemo.a
# cc -g -o static-prog prog.c -L. -ldemo
./static-prog
exit 0

