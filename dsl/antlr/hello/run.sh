#!/bin/bash
# author: yy

source ../env.sh

$antlr4tool Hello.g4
javac *.java

echo "打印tokens"
$antlr4test Hello r -tokens <<EOF
hello yy
EOF

echo "打印tree"
$antlr4test Hello r -tree <<EOF
hello yy
EOF

echo "运行test类"
java Test <<EOF
hello yy
EOF

exit 0

