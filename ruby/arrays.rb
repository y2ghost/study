# 数组用法示例
array1 = %w(one two three four)
array1 = ['one', 'two', 'three', 'four']
a = Array.new 3,:x
b = Array.new(3) { "X"}

# 分解赋值
a1, a2, a3 = b

# 支持任意类型元素
c = [1, 'b', nil, [3, 4]]

# 数组集合运算
x = [5, 5, 1, 3]
y = [5, 2, 4, 3]
# 并集
puts (x | y).to_s
# 交集
puts (x & y).to_s
# 差集
puts (x - y).to_s

# 数组过滤
a = [1, 2, 3, 4, 5, 6]
# 下面结果值[5, 6]
a.select { |number| number > 3 }.reject { |number| number < 5 } 

%w(1 2 3 4 5 6 7 8 9 10).map(&:to_i)
%w(1 2 3 4 5 6 7 8 9 10).map(&->(i){ i.to_i * 2})
