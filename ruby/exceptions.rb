# 异常示例
class FileNotFound < StandardError
end

def read_file(path)
  File.exist?(path) || raise(FileNotFound, "文件#{path}没找到")
  File.read(path)
end


read_file("missing.txt")
read_file("exceptions.rb")

# 处理异常
begin
  # 业务代码
  rescue StandardError => e
    # 执行异常处理的逻辑
  else
    # 执行正常处理的逻辑
  ensure
    # 无论如何执行的逻辑
end

