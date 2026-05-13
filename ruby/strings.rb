# 字符串示例
puts "只有双引号支持字符串插入 #{Time.now}"

# 字符串常用函数
"string".upcase     # => "STRING"
"STRING".downcase   # => "string"
"String".swapcase   # => "sTRING"
"string".capitalize # => "String"

s1 = "hello"
s2 = "     "
s3 = "ruby!"
puts s1 + s2 + s3

# 字符串分割
"alpha,beta".split(",")

# 字符串连接
["alpha", "beta"].join(",")

# 字符串格式化 - 建议使用开头的字符串插入技巧
puts "hello %s, my name is %s!" % ['guys', 'li']

# 字符串替换
"string".tr('r', 'l')
"string ring".sub('r', 'l')
"string ring".gsub('r', 'l')

