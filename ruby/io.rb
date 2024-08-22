# IO相关操作示例
file = File.new("tmp.txt", "w")
file.write("first line\n")
file.write("second line\n")
file.close

File.open("tmp.txt", "a") do |f|
  f.write("nananan\n")
  f.write("batman!\n")
end

