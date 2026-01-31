package ywork.script
// 多行字符串，可以使用三个双引号或是单引号来定义
// 单引号不支持占位符
def name = 'yy'
println '''The
name is ${name}'''

// 双引号支持占位符
def x = 'smart'
def str="""you 
are ${x} people in \
large groups."""
println str
