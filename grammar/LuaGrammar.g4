parser grammar LuaGrammar;

options {
    tokenVocab = LuaLexer;
}

// grammar rules
chunk : block ;
block : (stat)* (retstat)? ;
stat : SEMI
        | varlist EQ explist
        | functioncall
        | label
        | BREAK
        | GOTO NAME
        | DO block END
        | WHILE exp DO block END
        | REPEAT block UNTIL exp
        | IF exp THEN block (ELSEIF exp THEN block)* (ELSE block)? END
        | FOR NAME EQ exp COMMA exp (COMMA exp)? DO block END
        | FOR namelist IN explist DO block END
        | FUNCTION funcname funcbody
        | LOCAL FUNCTION NAME funcbody
        | LOCAL attnamelist (EQ explist)? ;


attnamelist : NAME attrib (COMMA NAME attrib)* ;
attrib : (LT NAME GT)? ;
retstat : RETURN (explist)? ;
label : CC NAME CC ;
funcname : NAME (DOT NAME)* (COL NAME)? ;
varlist: var (COMMA var)* ;
var : NAME | prefixexp OB exp CB | prefixexp DOT NAME ;
namelist : NAME (COMMA NAME)* ;
explist : exp (COMMA exp)* ;

exp: NIL
    | FALSE
    | TRUE
    | NUMBER
    | STRING
    | DDD
    | functiondef
    | prefixexp
    | tableconstructor
    | exp binop exp
    | unop exp ;

prefixexp : OP exp CP
    | NAME
    | prefixexp OB exp CB
    | prefixexp DOT NAME
    | prefixexp args
    | prefixexp COL NAME args ;

functioncall  : prefixexp args | prefixexp COL NAME args ;


args : OP (explist)? CP | tableconstructor | STRING ;

functiondef : FUNCTION funcbody ;
funcbody : OP (parlist)? CP block END ;
parlist : namelist (COMMA DDD)? | DDD ;
tableconstructor : OCU (fieldlist)? CCU ;
fieldlist : field (fieldsep field)* (fieldsep)? ;
field : OB exp CB EQ exp | NAME EQ exp | exp ;
fieldsep : COMMA | SEMI ;
binop : PLUS | MINUS | STAR | SLASH | SS | CARET | PER | DD |
        LL | GG | AMP | PIPE | SQUIG |
        LT | LE | GT | GE | EE | SQEQ |
        AND | OR ;
unop : MINUS | NOT | POUND | SQUIG ;
