package study.ywork.basis.regex;

/*
 * \G 匹配上一次匹配的位置，或者文本开头
 */
class GBoundaryMatcher {
    public static void main(String[] args) {
        final String input1 = ",,,,,123,45,67";
        final String input2 = "%var1%, %var2%, %var3% - %var4%, %var5%, %var6%";
        final String input3 = "{%var1%, %var2%, %var3%} {%var4%, %var5%, %var6%}";
        final String re1 = "\\G,";
        final String re2 = "\\G%([^%]+)%(,?\\s+)";
        final String re3 = "\\G(\\{|,\\h+)%([^%]+)%";

        // 匹配上一次位置的例子1
        String repl1 = input1.replaceAll(re1, "-");
        System.out.println(repl1);

        // 匹配上一次位置的例子2
        String repl2 = input2.replaceAll(re2, "@$1@$2");
        System.out.println(repl2);
        
     // 匹配上一次位置的例子3
        String repl3 = input3.replaceAll(re3, "$1#$2#");
        System.out.println(repl3);
    }
}
