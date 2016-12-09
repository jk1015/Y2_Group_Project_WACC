parser grammar WACCParser;

options {
  tokenVocab=WACCLexer;
}

program: BEGIN struct* function* stat END EOF;

header: struct* function* EOF;

function: type identifier OPEN_PARENTHESES paramList? CLOSE_PARENTHESES IS stat END;

struct: STRUCT identifier IS (fixedSizeType identifier)* END;

paramList: param (COMMA param)*;

param: type identifier;

identifier: IDENTIFIER;
funcIdent: DIVIDE IDENTIFIER;

stat: SKIPPER                           #skipStat
  | type identifier ASSIGN assignRHS    #initAssignStat
  | assignLHS ASSIGN assignRHS          #assignStat
  | READ assignLHS                      #readStat
  | FREE expr                           #freeStat
  | RETURN expr                         #returnStat
  | EXIT expr                           #exitStat
  | PRINT expr                          #printStat
  | PRINTLN expr                        #printlnStat
  | IF expr THEN stat ELSE stat FI      #ifStat
  | WHILE expr DO stat DONE             #whileStat
  | BREAK                               #breakStat
  | CONTINUE                            #continueStat
  | BEGIN stat END                      #blockStat
  | stat SEMICOLON stat                 #seqStat
  | FOR identifier FROM expr TO expr BY expr DO stat DONE #forStat
  ;

assignLHS: identifier
  | arrayElem
  | pairElem
  | structContents
  | derefLHS
  ;

assignRHS: expr
  | arrayLiter
  | newPair
  | newArray
  | pairElem
  | callFunction
  | structList
  ;

structList: OPEN_CURLY (assignRHS (COMMA assignRHS)*)? CLOSE_CURLY;

newPair: NEWPAIR OPEN_PARENTHESES expr COMMA expr CLOSE_PARENTHESES;

newArray: NEWARRAY type OPEN_SQUARE expr CLOSE_SQUARE ;

callFunction: CALL (identifier | derefLHS) OPEN_PARENTHESES (argList)? CLOSE_PARENTHESES;

argList: expr (COMMA expr)*;

pairElem: FST expr
  | SND expr
  ;

type: baseType
  | arrayType
  | pairType
  | structType
  | ptrType
  | funcPtrType
  ;

fixedSizeType: baseType
  | structType
  | ptrType
  ;

baseType: INT_TYPE
  | BOOL_TYPE
  | CHAR_TYPE
  | STRING_TYPE
  | FLOAT_TYPE
  ;

arrayType: (baseType | pairType | funcPtrType) (OPEN_SQUARE CLOSE_SQUARE)+;

pairType: PAIR OPEN_PARENTHESES pairElemType COMMA pairElemType CLOSE_PARENTHESES;

structType: identifier;

ptrType: ptrBaseType (MULTIPLY)+;

ptrBaseType: baseType
  | arrayType
  | pairType
  | structType
  | funcPtrType
  ;

funcPtrType: OPEN_PARENTHESES type LT MINUS OPEN_PARENTHESES (type (COMMA type)*)? CLOSE_PARENTHESES CLOSE_PARENTHESES MULTIPLY;

pairElemType: baseType
  | arrayType
  | pairNullType
  | ptrType
  | funcPtrType
  ;

pairNullType: PAIR;

unaryOper: NOT
  | LEN
  | ORD
  | CHR
  | MINUS
  ;

binaryOper1: MULTIPLY
  | DIVIDE
  | MOD
  ;

binaryOper2: PLUS
  | MINUS
  ;

binaryOper3: GT
  | GEQ
  | LT
  | LEQ
  ;

binaryOper4: EQ
  | NEQ
  ;

binaryOper5: AND
  ;

binaryOper6: OR
  ;

expr: expr6;

bracketsExpr: OPEN_PARENTHESES expr CLOSE_PARENTHESES;

unaryExpr: unaryOper expr1;

baseExpr: intLiter
  | boolLiter
  | charLiter
  | stringLiter
  | pairLiter
  | identifier
  | refLHS
  | derefLHS
  | arrayElem
  | structContents
  | floatLiter
  ;

structContentsExpr: identifier
                  | derefLHS
                  | arrayElem
                  | OPEN_PARENTHESES structContentsExpr CLOSE_PARENTHESES
                  ;

structContents: structContentsExpr (DOT identifier)+;

expr1: expr1 binaryOper1 expr1
  | baseExpr
  | unaryExpr
  | bracketsExpr
  ;

expr2: expr2 binaryOper2 expr2
  | expr1
  ;

expr3: expr3 binaryOper3 expr3
  | expr2
  ;

expr4: expr4 binaryOper4 expr4
  | expr3
  ;

expr5: expr5 binaryOper5 expr5
  | expr4
  ;

expr6: expr6 binaryOper6 expr6
  | expr5
  ;

structContentsExpr: identifier
                  | derefLHS
                  | arrayElem
                  ;

structContents: structContentsExpr (DOT identifier)+;

refLHS: AMP (assignLHS | funcIdent);

derefLHS: (MULTIPLY)+ assignLHS;

arrayElem: identifier (OPEN_SQUARE expr CLOSE_SQUARE)+;

arrayLiter: OPEN_SQUARE (expr (COMMA expr)*)? CLOSE_SQUARE;

intLiter: (PLUS | MINUS)? INT;

boolLiter: BOOL_LITERAL;

charLiter: CHAR_LITERAL;

stringLiter: STRING_LITERAL;

pairLiter: PAIR_LITERAL;

floatLiter: (PLUS | MINUS)? FLOAT_LITER;
