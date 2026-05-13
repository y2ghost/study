package ywork.script

println '实现了call方法的类都可以当作函数调用'
class SumParser {
    private String input;

    SumParser(String input) {
        this.input = input
    }

    int call() {
        int sum = 0;
        for (String s : input.split("\\+")) {
            sum += Integer.parseInt(s)
        }

        return sum;
    }
}

def sumParser = new SumParser("1+3+7")
def result = sumParser()
println result;
println "显示调用call()"
def result2 = sumParser.call()
println result2;
