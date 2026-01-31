# 代码块和lambda表达式示例

# lambda表达式
hello = -> { 'hello ruby' }
hello[]

hello = ->(name) { "hello #{name}" }
print_args = lambda do |first, second, third|
  puts "第一个参数: #{first}"
  puts "第二个参数: #{second}"
  puts "第三个参数: #{third}"
end

print_args.call(1,2,3)

# 代码块示例
class Greeter
  def to_proc
    Proc.new do |item|
      puts "hello #{item}"
    end
  end
end

greet = Greeter.new
# 参数前加入&表示当作代码块
%w(apple orange).each(&greet)

p [ 'rabbit', 'grass' ].map( &:upcase )

