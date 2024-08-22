# 常见设计模式实现示例

# 装饰器模式
class Pizza
  def cost
    300
  end
end

module CheesePizza
  def cost
    super + 50
  end
end

module LargePizza
  def cost
    super + 100
  end
end

pizza = Pizza.new         #=> cost = 300
pizza.extend(CheesePizza) #=> cost = 350
pizza.extend(LargePizza)  #=> cost = 450
puts "pizza cost #{pizza.cost}"

# 观察者模式
require "observer"
class Moderator
  include Observable

  def initialize(name)
    @name = name
  end
  def write
    message = "computer says: No"
    changed
    notify_observers(message)
  end
end

class Warner
  def initialize(moderator, limit)
    @limit = limit
    moderator.add_observer(self)
  end
end

class Subscriber < Warner
  def update(message)
    puts "#{message}"
  end
end

moderator = Moderator.new("Rupert")
Subscriber.new(moderator, 1)
moderator.write
moderator.write

# 单例模式
require "singleton"
class Logger
  include Singleton
end

# Logger.new方法会出错的
# 使用示例如下
first, second = Logger.instance, Logger.instance
first == second

