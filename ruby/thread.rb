# 线程示例
counter = 0
counter_mutex = Mutex.new

3.times.map do |index|
  Thread.new do
    counter_mutex.synchronize {counter += 1}
  end
end.each(&:join)

puts "counter: #{counter}"

# 停止线程示例
require 'thread'
class CounterThread < Thread
  def initialize
    @count = 0
    @continue = true
    super do
      @count += 1 while @continue
      puts "I counted up to #{@count} before I was cruelly stopped."
    end
  end
  def stop
    @continue = false
  end
end

counter = CounterThread.new
sleep 2
counter.stop

# 杀死线程示例
# Thread.kill(...)
# 等待线程结束
# thread.join(...)
