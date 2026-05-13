# hash示例
h = { "first_name" => "first", "last_name" => "last" }
h.each_key do |key|
  puts key
end

# 过滤操作示例
{ :a => 1, :b => 2, :c => 3 }.select { |k, v| k != :a && v.even? } # => { :b => 2 }
{ :a => 1, :b => 2, :c => 3 }.select { |_, v| v.even? } # => { :b => 2 }
{ :a => 1, :b => 2, :c => 3 }.select { |k, _| k == :c } # => { :c => 3 }

# 和数组直接进行转换
{ :a => 1, :b => 2 }.to_a # => [[:a, 1], [:b, 2]]
[[:x, 3], [:y, 4]].to_h # => { :x => 3, :y => 4 }