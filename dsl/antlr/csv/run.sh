#!/bin/bash
# author: yy

source ../env.sh

$antlr4tool CSV.g4
javac *.java

echo "分析CSV文件"
$antlr4test CSV file -tokens data.csv
$antlr4test CSV file -tree data.csv

echo "LoadCSV测试"
java LoadCSV data.csv

exit 0

