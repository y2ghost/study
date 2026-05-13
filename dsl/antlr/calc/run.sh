#!/bin/bash
# author: yy

source ../env.sh

# 使用了语法标签，文件中标签以'#'开头
$antlr4tool -no-listener -visitor LabeledExpr.g4
javac *.java

echo "Calc执行计算"
java Calc t.expr

exit 0

