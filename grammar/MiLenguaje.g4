grammar MiLenguaje;

@ header {
    package org.example;
}

inicio  : 'hola' ID (',' ID)* ;
ID : 	[a-zA-Z]+ ;
ESP : [ \t\r\n]+ -> skip ;
