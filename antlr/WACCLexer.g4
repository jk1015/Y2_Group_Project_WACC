lexer grammar WACCLexer;

//general operators (multiple uses)
PLUS: '+' ;
MINUS: '-' ;

//binary operators
MULTIPLY: '*' ;
DIVIDE: '/' ;
MOD: '%' ;
GT: '>' ;
GEQ: '>=' ;
LT: '<' ;
LEQ: '<=' ;
EQ: '==' ;
NEQ: '!=' ;
AND: '&&' ;
OR: '||' ;

//unary operators
NOT: '!';
LEN: 'len';
ORD: 'ord';
CHR: 'chr';

ASSIGN: '=';

//brackets
OPEN_PARENTHESES : '(' ;
CLOSE_PARENTHESES : ')' ;
OPEN_SQUARE : '[' ;
CLOSE_SQUARE : ']' ;
OPEN_CURLY : '{' ;
CLOSE_CURLY : '}' ;

SINGLE_QUOTE : '\'' ;
DOUBLE_QUOTE : '"' ;

SEMICOLON : ';' ;

COMMA : ',' ;

//blocks
BEGIN: 'begin';
END: 'end' ;
IS: 'is' ;

//functions
SKIPPER: 'skip' ;
READ: 'read' ;
FREE: 'free' ;
RETURN: 'return' ;
EXIT: 'exit';
PRINT: 'print';
PRINTLN: 'println';
CALL: 'call' ;

//conditionals
IF: 'if' ;
THEN: 'then' ;
ELSE: 'else' ;
FI: 'fi' ;
ELIF: 'elif';

//loops
WHILE: 'while' ;
DO: 'do' ;
DONE: 'done' ;

//pairs
PAIR: 'pair' ;
FST: 'fst' ;
SND: 'snd' ;
NEWPAIR: 'newpair' ;

//types
INT_TYPE: 'int' ;
BOOL_TYPE: 'bool' ;
CHAR_TYPE: 'char' ;
STRING_TYPE: 'string' ;

//null
fragment NULL: 'null' ;

//letters
fragment LETTER: 'a'..'z' | 'A'..'Z';

fragment ESCAPED_CHARACTER: '0' | 'b' | 't' | 'n' | 'f' | '"' | '\'' | '\\' ;

fragment CHARACTER: ~('\\' | '\'' | '"' ) | ('\\' ESCAPED_CHARACTER);

//alphanumeric
fragment ALPHANUMERIC: (LETTER | DIGIT);

//numbers
fragment DIGIT : '0'..'9' ;


INT: DIGIT+;
BOOL_LITERAL: 'true' | 'false';
PAIR_LITERAL: NULL;
CHAR_LITERAL: '\'' CHARACTER '\'' ;
STRING_LITERAL: '"' CHARACTER* '"' ;

IDENTIFIER: ('_' | LETTER) ('_' | ALPHANUMERIC)* ;

//whitespace
COMMENT: ('#' ~('\n')* '\n') -> skip;
WS: (' ' | '\n' | '\t' | '\r') -> skip;
