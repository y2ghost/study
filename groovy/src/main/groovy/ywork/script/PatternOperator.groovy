package ywork.script

// 正则表达式操作符'~'示例
def pattern = ~"e.+?s"
println pattern.getClass()
def matcher = pattern.matcher("aggressiveness")
while (matcher.find()) {
    printf "match: %s, start: %s end: %s%n",
            matcher.group(), matcher.start(), matcher.end()
}

// 正则表达式操作符'=~'示例
matcher = "aggressiveness" =~ "e.+?s"
while (matcher.find()) {
    printf "match: %s, start: %s end: %s%n",
            matcher.group(), matcher.start(), matcher.end()
}

// 正则表达式操作符'==~'示例
match = "aggressiveness" ==~ ".*ess.*"
println match
