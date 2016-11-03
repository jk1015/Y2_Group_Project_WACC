parser grammar WACCParser;

options {
  tokenVocab=WACCLexer;
}

program: BEGIN func* stat END;

func: type ident OPEN_PARENTHESES param_list? CLOSE_PARENTHESES IS stat END;

param_list: param (COMMA param)*;

param: type ident;

stat: SKIPPER
  | type ident EQUALS assign_rhs
  | assign_lhs EQUALS assign_rhs
  | READ assign_lhs
  | FREE expr
  | RETURN expr
  | EXIT expr
  | PRINT expr
  | PRINTLN expr
  | IF expr THEN stat ELSE stat FI
  | WHILE expr DO stat DONE
  | BEGIN stat END
  | stat SEMICOLON stat
  ;

assign_lhs: ident
  | array_elem
  | pair_elem
  ;

assign_rhs: expr
  | array_liter
  | NEWPAIR OPEN_PARENTHESES expr COMMA expr CLOSE_PARENTHESES
  | pair_elem
  | CALL ident OPEN_PARENTHESES (arg_list)? CLOSE_PARENTHESES
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

  array_type: type OPEN_SQUARE CLOSE_SQUARE;

  pair_type: PAIR OPEN_PARENTHESES pair_elem_type COMMA pair_elem_type CLOSE_PARENTHESES;

  pair_elem_type: base_type
    | array_type
    | PAIR
    ;

  expr: int_liter
    | bool_liter
    | char_liter
    | str_liter
    | pair_liter
    | IDENTIFIER
    | array_elem
    | unary_oper expr
    | expr binary_oper expr
    | OPEN_PARENTHESES expr CLOSE_PARENTHESES
    ;

  unary_oper: NOT
    | LEN
    | ORD
    | CHR
    | MINUS
    ;

  binary_oper: PLUS
    | MINUS
    | MULTIPLY
    | DIVIDE
    | MOD
    | GT
    | GEQ
    | LT
    | LEQ
    | EQ
    | NEQ
    | AND
    | OR
    ;

  array_elem: IDENTIFIER (OPEN_SQUARE expr CLOSE_SQUARE)+;

  int_liter: int_sign? INTEGER;

  int_sign: PLUS
    | MINUS
    ;

  bool_liter: TRUE
    | FALSE
    ;

  char_liter: SINGLE_QUOTE CHARACTER SINGLE_QUOTE;

  array_liter: OPEN_SQUARE (expr (COMMA expr)*)? CLOSE_SQUARE;

  pair_liter: NULL;
