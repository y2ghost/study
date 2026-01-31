# 迭代器示例
(1..10).each { |i| puts i.even? ? 'even' : 'odd' }
(1..10).each do |i|
  if i.even?
    puts 'even'
  else
    puts 'odd'
  end
end

# 类实现迭代器示例
class NaturalNumbers
  include Enumerable
  def initialize(upper_limit)
    @upper_limit = upper_limit
  end
  def each(&block)
    0.upto(@upper_limit).each(&block)
  end
end

n = NaturalNumbers.new(6)
n.reduce(:+)
n.select(&:even?)
n.map { |number| number ** 2}

# 数组和字典迭代器示例
[[1, 2], [3, 4]].each { |(a, b)| p "a: #{ a }", "b: #{ b }" }
{a: 1, b: 2, c: 3}.each { |pair| p "pair: #{ pair }" }

names = ['Siva', 'Charan', 'Naresh', 'Manish']
for name in names
  puts name
end

[2,3,4].map.with_index { |e, i| puts "Element of array number #{i} => #{e}" }