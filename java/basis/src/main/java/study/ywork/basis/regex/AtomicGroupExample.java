package study.ywork.basis.regex;

// 原子分组例子
// 匹配字符后不会进行回溯动作
class AtomicGroupExample {
    public static void main(String[] args) {
        final String input = "foodie";
        final String regexs[] = {
            // 非捕获型分组正则表达式
            "foo(?:d|die|lish)",
            // 原子分组正则表达式
            "foo(?>d|die|lish)",
            // 优化顺序的原子分组正则表达式
            "foo(?>lish|die|d)",
        };

        for (String regex : regexs) {
            System.out.printf("%s: %s%n", regex, input.matches(regex));
        }
    }
}
