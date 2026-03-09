#!/bin/bash
# author: yy

source ../env.sh

$antlr4tool DOT.g4
javac *.java

echo "分析DOT文件"
$antlr4test DOT graph -tokens t.dot
$antlr4test DOT graph -tree t.dot

exit 0

