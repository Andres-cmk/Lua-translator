lexer grammar LuaLexer;

options {
    superClass=org.antlr.v4.runtime.Lexer;
}

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
NUMBER : '0' [xX] HEXDIGIT+ ('.' HEXDIGIT*)? ([pP] [+-]? [0-9]+)? | [0-9]+ ('.' [0-9]+)? ([eE] [+-]? [0-9]+)? ;
STRING : '"' (ESC | ~["\\])* '"' | '\'' (ESC | ~['\\])* '\'' | LONGSTRING ;
fragment LONGSTRING : '[' '='* '[' .*? ']' '='* ']' ;
fragment ESC : '\\' (['"abtnfrv\\z] | 'x' HEXDIGIT HEXDIGIT | [0-9] [0-9]? [0-9]? | 'u{' HEXDIGIT+ '}') ;
fragment HEXDIGIT : [0-9a-fA-F] ;
COMMENT : '--' ~[\r\n]* -> skip ;
WS : [ \t\r\n]+ -> skip ;

