package ywork.script
println "一切皆是对象"

int a = 2
printf("%s - %s%n", a.getClass(), a.getClass().isPrimitive())

def b = 2
printf "%s - %s%n", b.getClass(), b.getClass().isPrimitive()

c = 2
printf "%s - %s%n", c.getClass(), c.getClass().isPrimitive()

double d = 2.2
printf "%s - %s%n", d.getClass(), d.getClass().isPrimitive()

e = 2.3
printf "%s - %s%n", e.getClass(), e.getClass().isPrimitive()
