lexer grammar Common;

ID: [a-zA-Z]+;          // 匹配标识符
INT: [0-9]+;            // 匹配整数
NEWLINE: '\r' ? '\n';   // 匹配换行
WS: [ \t]+ -> skip;     // 丢弃空白符

