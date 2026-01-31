# ruby自省的各种方法示例
class A
  def a
  end
end

module B
  def b
  end
end

class C < A
  include B
  def c
  end
end

puts C.instance_methods.to_s
puts C.instance_methods(false).to_s
puts C.ancestors.to_s
puts C.superclass.to_s

s = "test string"
puts s.class.to_s
puts s.methods.to_s
puts s.instance_of?(String)
puts s.public_methods.to_s
puts s.private_methods.to_s
puts s.respond_to?(:upper).to_s

