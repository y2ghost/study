# json示例
require "json"
j = '{"a": 1, "b": 2}'
puts JSON.parse(j)
puts JSON.parse(j, symbolize_names: true)
