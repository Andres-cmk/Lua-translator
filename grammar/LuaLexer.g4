lexer grammar LuaLexer;

@header {
    package org.example;
}

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
NUMBER : [0-9]+ ('.' [0-9]+)? | '0' [x] [0-9a-fA-F]+ ;
STRING : '"' (ESC | ~["\\])* '"' | '\'' (ESC | ~['\\])* '\'' ;
fragment ESC : '\\' [btnfr"'\\] ;
COMMENT : '--' ~[\r\n]* ;
WS : [ \t\r\n]+ -> skip ;

