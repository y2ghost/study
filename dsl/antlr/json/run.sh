#!/bin/bash
# author: yy

source ../env.sh

$antlr4tool JSON.g4
javac *.java

echo "分析JSON文件"
$antlr4test JSON json -tree t.json

echo "JSON2XML测试"
java JSON2XML t.json

exit 0

