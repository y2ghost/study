#!/bin/bash
# author: yy

source ../env.sh

$antlr4tool -no-listener Rows.g4
javac *.java

for i in `seq 3`; do
    echo "打印t.rows的第${i}列内容"
    java Col $i < t.rows
done

exit 0

