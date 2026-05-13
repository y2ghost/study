工具安装
- ```bash
- # 提前下载antlr-4.10.1-complete.jar并放到/usr/local/lib里面
- export CLASSPATH=".:/usr/local/lib/antlr-4.10.1-complete.jar"
- alias antlr4tool="java org.antlr.v4.Tool"
- alias antlr4test="java org.antlr.v4.gui.TestRig"
- ```

工具使用
- ```bash
- antlr4tool XXXX.g4
- antlr4test GrammarName startRuleName [-tokens] [-tree] ...
- ```

