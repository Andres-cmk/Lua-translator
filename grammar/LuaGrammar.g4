parser grammar LuaGrammar;

options {
    tokenVocab = LuaLexer;
}

// Gramatica de Lua 5.4 (manual de referencia, seccion 9).
// Las etiquetas (# nombre) generan un metodo visitXxx por alternativa,
// que es lo que usa el interprete (patron visitor).

chunk : block EOF ;

block : stat* retstat? ;

stat : SEMI                                                         # emptyStat
     | varlist EQ explist                                           # assignStat
     | functioncall                                                 # callStat
     | label                                                        # labelStat
     | BREAK                                                        # breakStat
     | GOTO NAME                                                    # gotoStat
     | DO block END                                                 # doStat
     | WHILE exp DO block END                                       # whileStat
     | REPEAT block UNTIL exp                                       # repeatStat
     | IF exp THEN block (ELSEIF exp THEN block)* (ELSE block)? END # ifStat
     | FOR NAME EQ exp COMMA exp (COMMA exp)? DO block END          # numericForStat
     | FOR namelist IN explist DO block END                         # genericForStat
     | FUNCTION funcname funcbody                                   # functionStat
     | LOCAL FUNCTION NAME funcbody                                 # localFunctionStat
     | LOCAL attnamelist (EQ explist)?                              # localStat
     ;

attnamelist : NAME attrib (COMMA NAME attrib)* ;
attrib      : (LT NAME GT)? ;
retstat     : RETURN explist? SEMI? ;
label       : CC NAME CC ;
funcname    : NAME (DOT NAME)* (COL NAME)? ;
varlist     : var (COMMA var)* ;
namelist    : NAME (COMMA NAME)* ;
explist     : exp (COMMA exp)* ;

var : NAME                      # nameVar
    | prefixexp OB exp CB       # indexVar
    | prefixexp DOT NAME        # fieldVar
    ;

// Precedencia de Lua 5.4 (de mayor a menor): en ANTLR la alternativa que va
// primero liga mas fuerte. '^' y '..' son asociativos por la derecha.
exp locals [Object cache = null]   // cache de constantes (numeros y strings literales)
    : NIL                                               # nilExp
    | FALSE                                             # falseExp
    | TRUE                                              # trueExp
    | NUMBER                                            # numberExp
    | STRING                                            # stringExp
    | DDD                                               # varargExp
    | functiondef                                       # functionDefExp
    | prefixexp                                         # prefixExp
    | tableconstructor                                  # tableExp
    | <assoc=right> exp CARET exp                       # powExp
    | (NOT | POUND | MINUS | SQUIG) exp                 # unaryExp
    | exp (STAR | SLASH | SS | PER) exp                 # mulExp
    | exp (PLUS | MINUS) exp                            # addExp
    | <assoc=right> exp DD exp                          # concatExp
    | exp (LL | GG) exp                                 # shiftExp
    | exp AMP exp                                       # bandExp
    | exp SQUIG exp                                     # bxorExp
    | exp PIPE exp                                      # borExp
    | exp (LT | GT | LE | GE | SQEQ | EE) exp           # relExp
    | exp AND exp                                       # andExp
    | exp OR exp                                        # orExp
    ;

prefixexp : OP exp CP                   # parenPrefix
          | NAME                        # namePrefix
          | prefixexp OB exp CB         # indexPrefix
          | prefixexp DOT NAME          # fieldPrefix
          | prefixexp args              # callPrefix
          | prefixexp COL NAME args     # methodPrefix
          ;

functioncall : prefixexp args           # plainCall
             | prefixexp COL NAME args  # methodCall
             ;

args : OP explist? CP     # listArgs
     | tableconstructor   # tableArgs
     | STRING             # stringArgs
     ;

functiondef      : FUNCTION funcbody ;
funcbody         : OP parlist? CP block END ;
parlist          : namelist (COMMA DDD)? | DDD ;
tableconstructor : OCU fieldlist? CCU ;
fieldlist        : field (fieldsep field)* fieldsep? ;

field : OB exp CB EQ exp    # keyField
      | NAME EQ exp         # nameField
      | exp                 # posField
      ;

fieldsep : COMMA | SEMI ;
