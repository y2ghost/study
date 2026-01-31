#!/bin/bash
export myenv=first
echo "myenv生成构建的值: $myenv"
cmake -B build .
cd build
export myenv=second
echo "myenv执行构建的值: $myenv"
cmake --build .
