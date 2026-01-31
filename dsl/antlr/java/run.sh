#!/bin/bash
# author: yy

source ../env.sh

$antlr4tool Java.g4
javac *.java

echo "从类中提取方法生成接口类"
java ExtractInterfaceTool Demo.java
echo ""

echo "给类加上默认的序列号"
java InsertSerialID Demo.java

exit 0

