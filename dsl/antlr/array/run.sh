#!/bin/bash
# author: yy

source ../env.sh

$antlr4tool ArrayInit.g4
javac *.java

echo "打印tokens"
$antlr4test ArrayInit init -tokens <<EOF
{99, 3, 55}
EOF

echo "打印tree"
$antlr4test ArrayInit init -tree <<EOF
{99, 3, 55}
EOF

echo "生成可视化分析图"
$antlr4test ArrayInit init -gui <<EOF
{99, 3, 55}
EOF

echo "运行test类"
java Test <<EOF
{99, 3, 55}
EOF

echo "运行Translate类"
java Translate <<EOF
{99, 3, 55}
EOF

exit 0

