lexer grammar LuaLexer;

SEMI : ';';
EQ   : '=';

BREAK    : 'break';
GOTO     : 'goto';
DO       : 'do';
END      : 'end';
WHILE    : 'while';
REPEAT   : 'repeat';
UNTIL    : 'until';
IF       : 'if';
THEN     : 'then';
ELSEIF   : 'elseif';
ELSE     : 'else';
FOR      : 'for';
COMMA    : ',';
IN       : 'in';
FUNCTION : 'function';
LOCAL    : 'local';
LT       : '<';
GT       : '>';
RETURN   : 'return';
CC       : '::';
NIL      : 'nil';
FALSE    : 'false';
TRUE     : 'true';
DOT      : '.';
SQUIG    : '~';
MINUS    : '-';
POUND    : '#';
OP       : '(';
CP       : ')';
NOT      : 'not';
LL       : '<<';
GG       : '>>';
AMP      : '&';
SS       : '//';
PER      : '%';
COL      : ':';
LE       : '<=';
GE       : '>=';
AND      : 'and';
OR       : 'or';
PLUS     : '+';
STAR     : '*';
OCU      : '{';
CCU      : '}';
OB       : '[';
CB       : ']';
EE       : '==';
DD       : '..';
PIPE     : '|';
CARET    : '^';
SLASH    : '/';
DDD      : '...';
SQEQ     : '~=';



// lexical rules
NAME : [a-zA-Z_][a-zA-Z0-9_]* ;
NUMBER : '0' [xX] HEXDIGIT* ('.' HEXDIGIT*)? ([pP] [+-]? DIGIT+)?
       | DIGIT+ ('.' DIGIT*)? EXPONENT?
       | '.' DIGIT+ EXPONENT?
       ;
STRING : '"' (ESC | ~["\\\r\n])* '"' | '\'' (ESC | ~['\\\r\n])* '\'' | LONGSTRING ;
fragment LONGSTRING : '[' NESTED_STR ']' ;
fragment NESTED_STR : '=' NESTED_STR '=' | '[' .*? ']' ;
fragment ESC : '\\' ( ['"\\abfnrtv\r\n] | 'z' [ \t\r\n\f]* | 'x' HEXDIGIT HEXDIGIT | DIGIT DIGIT? DIGIT? | 'u{' HEXDIGIT+ '}' ) ;
fragment EXPONENT : [eE] [+-]? DIGIT+ ;
fragment DIGIT : [0-9] ;
fragment HEXDIGIT : [0-9a-fA-F] ;
LONGCOMMENT : '--[' NESTED_STR ']' -> skip ;
// Comentario de linea que NO empieza con un corchete largo (si no, ganaria por ser mas largo).
COMMENT : '--' ( '[' '='* ~[=[\r\n] ~[\r\n]* | '[' '='* | ~[[\r\n] ~[\r\n]* )? -> skip ;
SHEBANG : '#!' ~[\r\n]* -> skip ;
WS : [ \t\r\n\f]+ -> skip ;
