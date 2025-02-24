#!/bin/bash
# author: yy

source ../env.sh

$antlr4tool Expr.g4
javac *.java

echo "打印tokens"
$antlr4test Expr prog t.expr -tokens

echo "打印tree"
$antlr4test Expr prog t.expr -tree

echo "生成可视化分析图"
$antlr4test Expr prog t.expr -gui

echo "ExprJoyRide打印tree"
java ExprJoyRide t.expr

exit 0

