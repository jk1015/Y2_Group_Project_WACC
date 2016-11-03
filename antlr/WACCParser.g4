parser grammar WACCParser;

options {
  tokenVocab=WACCLexer;
}

program: BEGIN func* stat END;

func: type IDENTIFIER OPEN_PARENTHESES param_list? CLOSE_PARENTHESES IS stat END;

param_list: param (COMMA param)*;

param: type IDENTIFIER;

stat: SKIPPER
  | type IDENTIFIER ASSIGN assign_rhs
  | assign_lhs ASSIGN assign_rhs
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

assign_lhs: IDENTIFIER
  | array_elem
  | pair_elem
  ;

assign_rhs: expr
  | array_liter
  | NEWPAIR OPEN_PARENTHESES expr COMMA expr CLOSE_PARENTHESES
  | pair_elem
  | CALL IDENTIFIER OPEN_PARENTHESES (arg_list)? CLOSE_PARENTHESES
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

  expr: INT_LITERAL
    | BOOL_LITERAL
    | CHAR_LITERAL
    | STRING_LITERAL
    | PAIR_LITERAL
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

  array_liter: OPEN_SQUARE (expr (COMMA expr)*)? CLOSE_SQUARE;
