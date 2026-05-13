====编译模块简单说明====
必要路径
mkdir out mods

## 使用@filename的方式
find src/common.widget -name '*.java' > common-widget.txt
javac -d mods/common.widget @common-widget.txt
find src/data.widget -name '*.java' > data-widget.txt
javac --module-path mods -d out/data.widget @data-widget.txt

## 使用module-source-path参数
## 注意这种编译模式下源代码目录的名称必须和模块名称相同
javac -d mods --module-source-path src -m common.widget
javac --module-path mods -d out --module-source-path src -m data.widget

## 运行程序
java --module-path mods:out -m data.widget/com.example.Component

====自动模块====
目的
说明自动模块的功能

编译
rm -rf out mods
mkdir out mods
javac -d out src/easy.math/easy/math/BasicCalc.java
jar --create --file mods/easy-math.jar -C out .
javac --module-path mods -d out --module-source-path src -m math.app

运行程序
java -p out:mods -m math.app/com.example.AppMain

检查依赖
jdeps --module-path out:mods -s --module math.app
jar --file mods/easy-math.jar --describe-module
java -p out:mods --describe-module math.app

====拆分包===
演示模块化系统不允许拆分包的情况
也就是两个不同的模块拥有相同的包名情况

编译
rm -rf out mods
mkdir out mods
javac -d out --module-source-path src -m core.parser
javac -d out --module-source-path src -m parser.app

运行程序
java -p out -m parser.app/parser.xml.ParserApp
注意:
必然报错，错误就是parser.xml包存在于两个模块中

====多模块模式====
编译
rm -rf out mods
mkdir out mods
find src/{core.app,main.app} -name '*.java' > javaFiles.txt
javac -d out --module-source-path src @javaFiles.txt

简单编译
rm -rf out mods
mkdir out mods
javac -d out --module-source-path src -m core.app,main.app

运行程序
java --module-path out --module main.app/example.app.AppMain

打包程序
rm -rf lib
mkdir lib
jar --create --file lib/core.app.jar -C out/core.app .
jar --create --file lib/app.main.jar --main-class=example.app.AppMain  -C out/main.app .

查看打包文件
jar --list --file lib/core.app.jar
jar --list --file lib/app.main.jar

运行打包文件
java --module-path lib --module main.app/example.app.AppMain

检查模块信息
jar --describe-module --file lib/core.app.jar
jar --describe-module --file lib/app.main.jar

====模块提供服务====
也就是提供传统的SPI服务
如果不是模块化的方式提供服务，就必须按照传统的方式，在类路径
创建目录META-INF/services，并提供实现类的配置信息

编译
rm -rf out mods
mkdir out mods
javac -d out --module-source-path src -m msg.service.api
javac -d out --module-source-path src -m msg.service.provider.swing
javac -d out --module-source-path src -m yy.app

打包程序
rm -rf lib
mkdir lib
jar --create --file lib/msg-service.jar -C out/msg.service.api .
jar --create --file lib/msg-service-swing.jar -C out/msg.service.provider.swing .

运行程序
java -p out:lib -m yy.app/study.service.AppMain

====指定导出包给模块====
编译
rm -rf out mods
mkdir out mods
javac -d out --module-source-path src -m core.ui
javac -d out --module-source-path src -m core.util
javac -d out --module-source-path src -m tt.app

运行程序
java -p out -m tt.app/com.example.Main

查看模块
java -p out --describe-module core.ui
java -p out --describe-module core.util
java -p out --describe-module tt.app
jdeps --module-path out -m tt.app

====反射访问模块====
需要配置module-info.java开放模块，配置语法如下:
open
opens
opens ... to ....
例子:
# 开放整个模块
open module client.module {
    requires framework.module;
}
# 开放部分包
module client.module {
    opens some.client.package;
    requires framework.module;
}
# 针对特定模块开发部分包
module client.module {
    opens some.client.package to framework.module;
    requires framework.module;
}

编译
rm -rf out mods
mkdir out mods
javac -d out --module-source-path src -m some.framework
javac -d out --module-source-path src -m the.client.app

运行程序
java -p out -m the.client.app/ywork.client.AppMain

