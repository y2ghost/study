package ywork.script
// 可以省略public关键字
import java.lang.reflect.Modifier

void testMethod() {
}

// 使用反射查看
println Modifier.toString(this.getClass()
        .getDeclaredMethod("testMethod")
        .getModifiers())
