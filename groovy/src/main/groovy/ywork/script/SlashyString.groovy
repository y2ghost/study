package ywork.script
// 斜线字符串用来表示正则表达式
def pattern = /\\data\\.*/
def result = "d:\\example\\data\\files".replaceAll(pattern, "")
println result

pattern = /\/data\/.*/
result = "http://example.com/data/files".replaceAll(pattern, "")
println result

// 多行无需特别的断行符: '\'
def s = /1
2\a
3\
4/

println s

// 支持占位符
def x = "yy"
s = /The employee name is $x/
println s

// 说明: $/ ... $/ 支持占位符，多行内容无需断行符
// 内部转义字符是'$'
x = 5
s = $/price/cost
is \
$x i.e. $$$x.
And this string used $$/...$/$ quotes
/$
println s
