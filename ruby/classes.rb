# 类示例
class Customer
  # 构造函数定义
  def initialize(name)
    @name = name.capitalize
  end
  # 实例方法
  def name
    @name
  end
  # 类方法
  def Customer.hello(name)
    puts "hello #{name}"
  end
end

# 创建类对象实例
me = Customer.new('me')
puts me.name
puts Customer.hello('you')

# getter和setter方法示例
class Cat
  attr_reader :age
  attr_writer :name
  attr_accessor :breed

  def initialize(name, breed)
    @name = name
    @breed = breed
    @age = 2
  end
  def speak
    puts "猫的名字#{@name}, 品种#{@breed}, 年龄#{@age}"
  end
end

my_cat = Cat.new('小黑', '黄色')
my_cat.speak

# 继承示例
class Animal
  def say
    'meep!'
  end
  def eat
    'yumm!'
  end
end

class Dog < Animal
  def say
    'woof!'
  end
  # 处理函数未找到错误
  def method_missing(method, *args, &block)
    "cannot call #{method} on Animal"
  end
end

dog = Dog.new
puts dog.say
puts dog.eat
puts dog.say_moo

