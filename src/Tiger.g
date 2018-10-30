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
}

program
:   exp
;

exp
:   infixExp ->^(EXP infixExp)
|   ifExp
|   whileExp
|   forExp
;

infixExp
:   orExp
;

orExp
:   andExp
    ((   '|'
        andExp
    )+ -> ^(OR andExp +)	// Cas où il y a plusieurs "addExp" de reconnus
    |	-> andExp			// Cas où il n'y a qu'un "addExp" de reconnu
    )
;

andExp
:   compExp
    ((   '&'
        compExp
    )+ -> ^(AND compExp+) // Cas où il y a plusieurs "comExp" de reconnus
    |	-> compExp	// Cas où il n'y a qu'un "comExp" de reconnu
    )
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
    (   (   '+'
        |   '-'
        )^
        addExp
    )?
;

multExp
:   unaryExp
    (   (   '*'
        |   '/'
        )^
        multExp
    )?
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
    (   seqArg          // Cas "callExp"
    |   (   indexArg    // Cas "subscript"
        |   fieldArg    // Cas "fieldExp"
        )*
        assignmentArg?
    )
;

seqArg
:   '('
    (   exp
        (   ','
            exp
        )*
    )?
    ')'
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

assignmentArg
:   ':='
    unaryExp
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
    )?
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
:   tyDec
|   varDec
|   funDec
;

tyDec
:   'type'
    TYID
    '='
    ty
;

ty
:   TYID
    arrTy
    recTy
;

arrTy
:   'array'
    'of'
    TYID
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
    exp
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
