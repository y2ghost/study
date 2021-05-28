====antlr Expr.g4====
antlr Expr.g4
javac Expr*.java
grun Expr prog -tokens

====LabeledExpr.g4====
antlr -no-listener -visitor LabeledExpr.g4
javac Calc.java LabeledExpr*.java
java Calc t.expr

LabeledExpr.g4 file notes:
we’d like a different visitor method for each alternative so that
we can get different “events” for each kind of input phrase. Labels
appear on the right edge of alternatives and start with
the # symbol in our new grammar, LabeledExpr.

examples:
expr op=('*'|'/') expr  # MulDiv
we can use MulDiv label the rule:   expr op=('*'|'/') expr

====Java.g4====
antlr Java.g4
javac InsertSerialID*.java Java*.java Extract*.java
java ExtractInterfaceTool Demo.java
java InsertSerialID Demo.java

====Rows.g4====
antlr -no-listener Rows.g4
javac Rows*.java Col.java
java Col 1 < t.rows
java Col 2 < t.rows
java Col 3 < t.rows

====Data.g4====
antlr -no-listener Data.g4
javac Data*.java
grun Data file -tree t.data

====XMLLexer.g4====
antlr XMLLexer.g4
javac XML*.java
grun XML tokens -tokens t.xml
