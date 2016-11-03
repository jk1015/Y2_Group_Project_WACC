lexer grammar WACCLexer;

//operators
PLUS: '+' ;
MINUS: '-' ;
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

//brackets
OPEN_PARENTHESES : '(' ;
CLOSE_PARENTHESES : ')' ;
OPEN_SQUARE : '[' ;
CLOSE_SQUARE : ']' ;
OPEN_CURLY : '{' ;
CLOSE_CURLY : '}' ;

SINGLE_QUOTE : '\'' ;
DOUBLE_QUOTE : '\"' ;

SEMICOLON : ';' ;

//blocks
BEGIN: 'begin' ;
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

//conditionals
IF: 'if' ;
THEN: 'then' ;
ELSE: 'else' ;
FI: 'fi' ;

//loops
WHILE: 'while' ;
DO: 'do' ;
DONE: 'done' ;

//bools
TRUE: 'true' ;
FALSE: 'false' ;


//numbers
fragment DIGIT : '0'..'9' ; 

INTEGER: DIGIT+ ;





