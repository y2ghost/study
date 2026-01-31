package ywork.script

println '指定方法或是类的访问权限为package-private示例'
import groovy.transform.PackageScope
import java.lang.reflect.Modifier

@PackageScope
class Task {
    @PackageScope
    void run() {
    }
}

def modifiers = Task.class.modifiers
printf "'%s'%n", Modifier.toString(modifiers)

modifiers = Task.class.getDeclaredMethod("run").modifiers
printf "'%s'%n", Modifier.toString(modifiers)
