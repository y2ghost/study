#!/bin/bash

echo "测试子SHELL"
echo $BASH_SUBSHELL

if (echo $BASH_SUBSHELL)
then
    echo "测试子SHELL执行命令成功"
else
    echo "测试子SHELL执行命令失败"
fi

