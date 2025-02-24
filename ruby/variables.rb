# 变量类型和范围示例
# 本地变量就是在函数内部或代码块内部

# 定义全局变量
$i_am_global = "全局的"

class Dinosaur
  # 定义类变量
  @@type = "像爬行动物，更像鸟"
  # 定义实例变量，类级别可见
  @base_sound = "基本声音"
  
  def initialize(sound = nil)
    @sound = sound || self.class.base_sound
  end
  def speak
    puts @sound
  end
  def try_to_speak
    puts @base_sound || "没法访问base_sound"
  end
  def count_and_store_sound_length
    @sound.chars.each_with_index do |char, i|
      @sound_length = i + 1
      puts "#{char}: #{sound_length}"
    end
  end
  def sound_length
    puts @sound_length
    @sound_length
  end
  def self.base_sound
    @base_sound
  end
  def self.type
    puts @@type
  end
  def type
    puts @@type
  end
  def instance_method
    puts "全局变量: #{$i_am_global}, 其他全局变量: #{$another_global}"
  end
  def self.class_method
    $another_global = "其他全局的"
    puts "全局变量: #{$i_am_global}, 其他全局变量: #{$another_global}"
  end
end

dino1 = Dinosaur.new
dino1.type
dino1.instance_method
# 演示无法访问类级别的实例变量
dino1.try_to_speak
dino1.speak
dino1.count_and_store_sound_length
dino1.sound_length

dino2 = Dinosaur.new "小声的叫"
dino2.speak
dino2.sound_length
Dinosaur.type
Dinosaur.class_method
puts Dinosaur.base_sound

class Trex < Dinosaur
  # 子类可以覆盖父类的类变量
  @@type = "大牙齿的鸟"
end

Trex.type
Dinosaur.type

module StrangeBird
  # 定义在模块中的类变量不会被子类覆盖
  @@type = "奇怪的鸟"
end

class DuckDinosaur < Dinosaur
  include StrangeBird
  # 实例变量不会覆盖父类的 
  @base_sound = "鸭子的叫声"
end

puts DuckDinosaur.class_variables
puts StrangeBird.class_variables
DuckDinosaur.type

duck_dino = DuckDinosaur.new
duck_dino.speak
puts DuckDinosaur.base_sound
puts Dinosaur.base_sound

# 环境变量示例
puts ENV["HOME"]
puts ENV.fetch("notExist", "defaultValue")
