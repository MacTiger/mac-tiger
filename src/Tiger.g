grammar Tiger;

options {
    language = Java;
    output = AST;
    backtrack = false;
    k = 1;
}

tokens {	 //Tokens imaginaires
	EXP;
	OR;
	AND;
	CALLEXP;
	SUBSCRIPT;
	FIELDEXP;
	IF;
	ARRAY;
	LET;
}

program
:   exp EOF!
;

exp
:   assignmentExp
|   ifExp
|   whileExp
|   forExp
;

assignmentExp
:   orExp
    (   ':='^
        exp
    )?
;

orExp
:   andExp
    (   '|'^
        andExp
        (   '|'!
            andExp
        )*
    )?
;

andExp
:   compExp
    (   '&'^
        compExp
        (   '&'!
            compExp
        )*
    )?
;

compExp
:   addExp
    (   (   '='
        |   '<>'
        |   '>'
        |   '<'
        |   '>='
        |   '<='
        )^
        addExp
    )?
;

addExp
:   multExp
    (   '+'^
        multExp
        (   '+'!
            multExp
        )*
    )?
    (   '-'^
        multExp
        (   '+'^
            multExp
            (   '+'!
                multExp
            )*
        )?
    )*
;

multExp
:   unaryExp
    (   '*'^
        unaryExp
        (   '*'!
            unaryExp
        )*
    )?
    (   '/'^
        unaryExp
        (   '*'^
            unaryExp
            (   '*'!
                unaryExp
            )*
        )?
    )*
;

unaryExp
:   seqExp
|   negExp
|   valueExp
|   objCreate
|   letExp
|   STRINGLIT
|   INTLIT
|   'nil'
|   'break'
;

seqExp
:   '('
    (   exp
        (   ';'
            exp
        )*
    )?
    ')' -> exp+
;

negExp
:   '-'^
    unaryExp
;

valueExp    // Gère l'ancien lValue, callExp et assignment
:   ID
    (   seqArg        	-> ^(CALLEXP ID seqArg)  // Cas "callExp"
    |   (   indexArg	-> ^(SUBSCRIPT ID indexArg)  // Cas "subscript"
        |   fieldArg	-> ^(FIELDEXP ID fieldArg)    // Cas "fieldExp"
        )*
    )
;

seqArg
:   '('
    (   exp
        (   ','
            exp
        )*
    )?
    ')' -> exp+
;

indexArg
:   '['
    exp
    ']'
;

fieldArg
:   '.'
    ID
;

objCreate   // Gère l'ancien arrCreate et recCreate
:   TYID
    (   '['
        exp
        ']'
        'of'
        unaryExp
    |   '{'
        (   fieldCreate
            (   ','
                fieldCreate
            )*
        )?
        '}'
    )
;

fieldCreate
:   ID
    '='
    exp
;

ifExp
:   'if'
    exp
    'then'
    exp
    (options {
        greedy = true;
    }:  'else'
        exp
    )?	-> ^(IF exp exp exp?)
;
// En ascendante, conflit lecture/reduction : lire 'else' ou reduire ? Normalement lecture.

whileExp
:   'while'
    exp
    'do'
    exp
;

forExp
:   'for'
    ID
    ':='
    exp
    'to'
    exp
    'do'
    exp
;

letExp
:   'let'
    dec+
    'in'
    (   exp
        (   ';'
            exp
        )*
    )?
    'end'
;

dec
:   tyDec -> ^(tyDec)
|   varDec -> ^(varDec)
|   funDec -> ^(funDec)
;

tyDec
:   'type'
    TYID
    '='
    ty -> ^(LET TYID ty)
;

ty
:   TYID
    arrTy
    recTy -> ^(TYID arrTy recTy)
;

arrTy
:   'array'
    'of'
    TYID -> ^(ARRAY TYID)
;

recTy
:   '{'
    (   fieldDec
        (   ','
            fieldDec
        )*
    )?
    '}'
;

fieldDec
:   ID
    ':'
    TYID
;

funDec
:   'function'
    ID
    '('
    (   fieldDec
        (   ','
            fieldDec
        )*
    )?
    ')'
    (   ':'
        TYID
    )?
    '='
    exp
;

varDec
:   'var'
    ID
    (   ':'
        TYID
    )?
    ':='
    exp -> ^(LET ID TYID? exp)
;

ID
:   ('a'..'z')+
;

TYID
:   ('A'..'Z')+
;

STRINGLIT
:   '"'
    (   ' '..'!'
    |   '#'..'['
    |   ']'..'~'
    |   '\\'
        (   'n'
        |   't'
        |   '"'
        |   '\\'
        |   '^'
            '@'..'_'
        |   '0' // Nombre ASCII partie 1 (commencant par 0)
            '0'..'9'
            '0'..'9'
        |   '1' // Nombre ASCII partie 2 (commencant par 1)
            (   '0'..'1'
                '0'..'9'
            |   '2'
                '0'..'7'
            )
        |   (   ' ' // Echappement de caractères blancs
            |   '\t'
            |   '\n'
            |   '\r'
            |   '\f'
            )+
            '\\'
        )
    )*
    '"'
;

INTLIT
:   '0'..'9'+
;

COMMENT
:   (
        '/*'
        .*
        (   COMMENT
            .*
        )*
        '*/'
    ) {$channel = HIDDEN;}
;

WS
:   (   ' '
    |   '\t'
    |   '\n'
    |   '\r'
    |   '\f'
    )+ {$channel = HIDDEN;}
;
