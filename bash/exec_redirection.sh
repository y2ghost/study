#!/bin/bash

# exec命令可以当前SHELL的会话期间
# 全局生效输入输出重定向

# 重定向标准错误
exec 2> test_error
echo "开始验证SHELL级别的文件重定向功能"
echo "这条内容重定向到标准错误文件" >&2

# 重定向标准输出
exec 1> test_out
echo "第1次测试输出"
echo "第2次测试输出"
echo "不用每个命令都使用重定向的方式" >&1

# 重定向标准输入
exec 0< exec_redirection.sh
count=1

while read line
do
    echo "#$count: $line"
    count=$[ $count + 1 ]
done


# 自定义重定向
exec 3> test_out3
exec 6> test_out6
exec 7< exec_redirection.sh

# 查看重定向信息
lsof -a -p $$ -d0,1,2,3,6,7

# 关闭重定向
exec 3>&-
exec 6>&-
exec 7>&-

exit 0

