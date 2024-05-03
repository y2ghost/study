#!/bin/bash
# author: yy

source ../env.sh

$antlr4tool -no-listener Data.g4
javac *.java

echo "语义动态判断示例"
$antlr4test Data file -tree t.data

exit 0

