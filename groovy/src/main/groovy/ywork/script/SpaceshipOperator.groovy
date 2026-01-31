package ywork.script

println '宇宙飞船运算符示例'
assert (1 <=> 1) == 0
assert (1 <=> 2) == -1
assert (2 <=> 1) == 1
assert ('a' <=> 'z') == -1

def x = 1 <=> 2; // 调用Integer.compareTo
println x

x = 1 <=> 1; // 调用Integer.compareTo
println x

x = "b" <=> "a" // 调用String.compareTo
println x

println '自定义比较函数示例'
def strings = [
    "banana",
    "fig",
    "apple",
    "orange",
    "pie"
];
def comparator = new Comparator<String>() {
            @Override
            int compare(String o1, String o2) {
                return o1.length() != o2.length() ? o1.length() <=> o2.length() : o1 <=> o2;
            }
        }

Collections.sort(strings, comparator);
for (def s : strings) {
    println s
}
