parser grammar WACCParser;

options {
  tokenVocab=WACCLexer;
}

program: BEGIN func* stat END;

func: type ident OPEN_PARENTHESES param-list? CLOSE_PARENTHESES IS stat END;

param-list: param (COMMA param)*;

param: type ident;

stat: SKIPPER
  | type ident EQUALS assign-rhs
  | assign-lhs EQUALS assign-rhs
  | READ assign-lhs
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

assign-lhs: ident
  | array-elem
  | pair-elem
  ;

assign-rhs: expr
  | array-liter
  | NEWPAIR OPEN_PARENTHESES expr COMMA expr CLOSE_PARENTHESES
  | pair-elem
  | CALL ident OPEN_PARENTHESES (arg-list)? CLOSE_PARENTHESES
  ;
