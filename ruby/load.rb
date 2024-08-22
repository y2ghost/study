# 模块示例
module LoadDemo

@@unavailableModules = []

[
  { name: 'CoreExtend', file: 'core_extend/lib/core_extend' },
  { name: 'Fs' , file: 'fs/lib/fs' },
  { name: 'Options' , file: 'options/lib/options' },
  { name: 'Susu' , file: 'susu/lib/susu' }
].each do |lib|
  begin
    require_relative lib[ :file ]
  rescue LoadError
    @@unavailableModules.push lib
  end
end

end # module LoadDemo

