MY_CONSTANT = "常量示例1"
Constant = "常量示例2"
my_variable = "不是常量"

# 修改常量会收到告警
MY_CONSTANT = "修改常量示例"

# 内置的常量
puts "__FILE__: %s" % __FILE__
puts "PROGRAM_NAME: %s" % $PROGRAM_NAME
puts "$$: %s" % $$
puts "$1: %s" % $1
puts "ARGV: %s" % ARGV
puts "ARGV: %s" % $*
puts "STDIN: %s" % STDIN

