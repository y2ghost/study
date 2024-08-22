# 正则表达式示例
if /hay/ =~ 'haystack'
  puts 'hay in haystack'
end

case "Ruby is #1!"
when /\APython/
  puts "Boooo."
when /\ARuby/
  puts "Right."
else
  puts "unknown what."
end

name_reg = /h(i|ello), my name is (?<name>.*)/i
name_input = "Hi, my name is Zaphod Beeblebrox"
match_data = name_reg.match(name_input)
match_data = name_input.match(name_reg)

if match_data.nil?
  puts "No match"
else
match_data[0]       #=> "Hi, my name is Zaphod Beeblebrox"
match_data[1]       #=> "i" #the first group, (i|ello)
match_data[2]       #=> "Zaphod Beeblebrox"
match_data[:name]   #=> "Zaphod Beeblebrox"
match_data["name"]  #=> "Zaphod Beeblebrox"
puts "Hello #{match_data[:name]}!"
end

# 技巧示例
string = "My not so long string"
string[/so/]            # gives so
string[/present/]       # gives nil
string[/present/].nil?  # gives true
puts "found" if string[/so/]

