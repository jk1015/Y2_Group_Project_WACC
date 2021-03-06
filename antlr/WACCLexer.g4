lexer grammar WACCLexer;

//general operators (multiple uses)
PLUS: '+' ;
MINUS: '-' ;
MULTIPLY: '*' ;
AMP: '&' ;

//binary operators
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
STRUCT: 'struct';
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

//loops
WHILE: 'while' ;
DO: 'do' ;
DONE: 'done' ;
FOR: 'for' ;
TO: 'to' ;
FROM: 'from' ;
BY: 'by' ;
BREAK: 'break' ;
CONTINUE: 'continue' ;

//structs
DOT: '.';

//pairs
PAIR: 'pair' ;
FST: 'fst' ;
SND: 'snd' ;
NEWPAIR: 'newpair' ;

//arrays
NEWARRAY: 'newarray' ;

//types
INT_TYPE: 'int' ;
BOOL_TYPE: 'bool' ;
CHAR_TYPE: 'char' ;
STRING_TYPE: 'string' ;
FLOAT_TYPE: 'float' ;

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

//hexadecimals
fragment HEXADECIMAL : '0'..'9' | 'a'..'f' | 'A'..'F' ;

//octals
fragment OCTAL : '0'..'7' ;

//binary
fragment BINARY : '0' | '1' ;

INT: DIGIT+ | (HEXADECIMAL)+'h' | (OCTAL)+'o' | (BINARY)+'b' ;
BOOL_LITERAL: 'true' | 'false';
PAIR_LITERAL: NULL;
CHAR_LITERAL: '\'' CHARACTER '\'' ;
STRING_LITERAL: '"' CHARACTER* '"' ;
FLOAT_LITER: (DIGIT)+ DOT (DIGIT)+ 'f';

IDENTIFIER: ('_' | LETTER) ('_' | ALPHANUMERIC)* ;

//whitespace
COMMENT: ('#' ~('\n')* '\n') -> skip;
WS: (' ' | '\n' | '\t' | '\r') -> skip;
