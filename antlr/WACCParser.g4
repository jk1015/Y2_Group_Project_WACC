parser grammar WACCParser;

options {
  tokenVocab=WACCLexer;
}

program: BEGIN func* stat END EOF;

func: type IDENTIFIER OPEN_PARENTHESES param_list? CLOSE_PARENTHESES IS stat END;

param_list: param (COMMA param)*;

param: type IDENTIFIER;

stat: SKIPPER                           #skipStat
  | type IDENTIFIER ASSIGN assign_rhs   #initAssignStat
  | assign_lhs ASSIGN assign_rhs        #assignStat
  | READ assign_lhs                     #readStat
  | FREE expr                           #freeStat
  | RETURN expr                         #returnStat
  | EXIT expr                           #exitStat
  | PRINT expr                          #printStat
  | PRINTLN expr                        #printlnStat
  | IF expr THEN stat ELSE stat FI      #ifStat
  | WHILE expr DO stat DONE             #whileStat
  | BEGIN stat END                      #blockStat
  | stat SEMICOLON stat                 #baseStat
  ;

assign_lhs: IDENTIFIER  #var_lhs
  | array_elem          #arrayElem_lhs
  | pair_elem           #pairElem_lhs
  ;

assign_rhs: expr        #expr_rhs
  | array_liter         #arrayLiter_rhs
  | NEWPAIR OPEN_PARENTHESES expr COMMA expr CLOSE_PARENTHESES      #newPair_rhs
  | pair_elem           #pairElem_rhs
  | CALL IDENTIFIER OPEN_PARENTHESES (arg_list)? CLOSE_PARENTHESES  #callFunc_rhs
  ;

  arg_list: expr (COMMA expr)*;

  pair_elem: FST expr
    | SND expr
    ;

  type: base_type
    | array_type
    | pair_type
    ;

  base_type: INT_TYPE
    | BOOL_TYPE
    | CHAR_TYPE
    | STRING_TYPE
    ;

  array_type: (base_type | pair_type) (OPEN_SQUARE CLOSE_SQUARE)+;

  pair_type: PAIR OPEN_PARENTHESES pair_elem_type COMMA pair_elem_type CLOSE_PARENTHESES;

  pair_elem_type: base_type
    | array_type
    | PAIR
    ;

  expr: expr6;

  expr0: INT_LITERAL                             #intExpr
    | BOOL_LITERAL                              #boolExpr
    | CHAR_LITERAL                              #charExpr
    | STRING_LITERAL                            #stringExpr
    | PAIR_LITERAL                              #pairExpr
    | IDENTIFIER                                #identExpr
    | array_elem                                #arrayElemExpr
    | unary_oper expr                           #unOpExpr
    | OPEN_PARENTHESES expr CLOSE_PARENTHESES   #bracketsExpr
    ;

  expr1: expr1 binary_oper1 expr1
    | expr0
    ;

  expr2: expr2 binary_oper2 expr2
    | expr1
    ;

  expr3: expr3 binary_oper3 expr3
    | expr2
    ;

  expr4: expr4 binary_oper4 expr4
    | expr3
    ;

  expr5: expr5 binary_oper5 expr5
    | expr4
    ;

  expr6: expr6 binary_oper6 expr6
    | expr5
    ;



  unary_oper: NOT
    | LEN
    | ORD
    | CHR
    | MINUS
    ;

  binary_oper1: MULTIPLY
    | DIVIDE
    | MOD
    ;

  binary_oper2: PLUS
    | MINUS
    ;

  binary_oper3: GT
    | GEQ
    | LT
    | LEQ
    ;

  binary_oper4: EQ
    | NEQ
    ;

  binary_oper5: AND ;

  binary_oper6: OR ;

  array_elem: IDENTIFIER (OPEN_SQUARE expr CLOSE_SQUARE)+;

  array_liter: OPEN_SQUARE (expr (COMMA expr)*)? CLOSE_SQUARE;
