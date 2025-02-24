# range示例
(10..1).to_a              #=> []
(1...3)                   #=> [1, 2]
(-6..-1).to_a             #=> [-6, -5, -4, -3, -2, -1]
('a'..'e').to_a           #=> ["a", "b", "c", "d", "e"]
('a'...'e').to_a          #=> ["a", "b", "c", "d"]
Range.new(1,3).to_a       #=> [1, 2, 3]
Range.new(1,3,true).to_a  #=> [1, 2]

(1..5).each do |i|
  print i
end


# 日期范围示例
require 'date'
date1 = Date.parse "01/06/2016"
date2 = Date.parse "05/06/2016"
puts ""
puts "Period #{date1.strftime("%d/%m/%Y")} to #{date2.strftime("%d/%m/%Y")}"

(date1..date2).each do |date|
  puts date.strftime("%d/%m/%Y")
end
