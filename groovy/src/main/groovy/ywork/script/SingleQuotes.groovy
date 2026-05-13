package ywork.script
// 单引号则表示纯粹的字符串，不支持占位符功能
// 变量类型: java.lang.String
def b = true
def date = java.time.LocalDate.now()
def s = '${b}, here\'s the date: ${date} '
println s + 'done!'
println s.getClass()
