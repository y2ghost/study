# 操作符重载为函数示例
class OperatorMethodDemo1
  def **(x)
    puts "取#{x}的幂"
  end
  def <<(y)
    puts "向左移动#{y}位"
  end
  def !
    puts "取反操作"
  end
end

OperatorMethodDemo1.new ** 2
OperatorMethodDemo1.new << 2
!OperatorMethodDemo1.new

class OperatorMethodDemo2
  def [](x)
    puts "查找元素#{x}"
  end
  def []=(x,y)
    puts "设置元素#{x}的值为#{y}"
  end
end

demo2 = OperatorMethodDemo2.new
demo2[:cats] = 42
demo2[18]

class OperatorMethodDemo3
  def -@
    puts "减一"
  end
  def +@
    puts "加一"
  end
  def ==(x)
    puts "检查等于#{x}"
  end
  def !=(x)
    puts "检查不等#{x}"
  end
end

demo3 = OperatorMethodDemo3.new
+demo3
-demo3
demo3 == 88
demo3 != 66

# ===操作符表示集合a是否包含元素b
# 常见的类重载了===操作符的含义
# Array 等价于 ==
# Date 等价于 ==
# Module 等价于 is_a?
# Object 等价于 ==
# Range 等价于 include?
# Regexp 等价于 =~
# String 等价于 ==
(1..5) === 3        # => true
(1..5) === 6        # => false
Integer === 42      # => true
Integer === '42'    # => false
/ell/ === 'Hello'   # => true
/ell/ === 'Foobar'  # => false

# '&.'安全导航操作符示例
house = 678
if house&.to_s
  puts house&.to_s
end

