# 元编程示例
def with(object, &block)
  object.instance_eval &block
end

hash = Hash.new
with hash do
  store :key, :value
  has_key? :key
  values
end

class Hello
  def hello(*args)
    puts "hello " + args.join(" ")
  end
end

# send方法实现动态调用对象方法
h = Hello.new
h.send :hello, "gentle", "readers"

class Account
  attr_accessor :name, :email, :notes, :address

  def assign_values(values)
    values.each_key do |k, v|
      self.send("#{k}=", values[k])
    end
  end
end

user_info = {
  name: "zhangsan",
  email: "zhangsan@test.com",
  address: "1号楼1单元",
  notes: "普通客户"
}

account = Account.new
account.assign_values(user_info)
puts account.inspect
