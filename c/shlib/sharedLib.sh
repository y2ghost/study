#!/bin/sh
# 使用共享库的方式构建

gcc -g -c -fPIC -Wall mod1.c mod2.c mod3.c

# 构建库名规则: libXYZ.so.maj#.min#.rel#
gcc -g -shared -Wl,-soname,libdemo.so.1 -o libdemo.so.1.0.1 \
    mod1.o mod2.o mod3.o
ln -sf libdemo.so.1.0.1 libdemo.so.1
ln -sf libdemo.so.1 libdemo.so
cc -g -Wall -c prog.c
cc -g -o shared-prog prog.o -L. -ldemo
LD_LIBRARY_PATH=. ./shared-prog
exit 0

