grammar Tiger;

options {
    language = Java;
    //  output = AST;
    backtrack = false;
    k = 1;
}

program
:   exp
;

exp
:   infixExp
;

infixExp
:   orExp
;

orExp
:   andExp
    (   '|'
        orExp
    )?
;

andExp
:   compExp
    (   '&'
        andExp
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
        )
        addExp
    )?
;

addExp
:   multExp
    (   (   '+'
        |   '-'
        )
        addExp
    )?
;

multExp
:   unaryExp
    (   (   '*'
        |   '/'
        )
        multExp
    )?
;

unaryExp
:   seqExp
|   negExp
|   valueExp
|   objCreate
|   ifExp
|   whileExp
|   forExp
//  |   letExp
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
    ')'
;

negExp
:   '-'
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
:   'if' exp 'then' unaryExp if2
;

if2
:   'else' unaryExp      // Cas "if exp then exp else exp"
|                   // Cas "if exp then exp"
;


whileExp
:   'while'
    exp
    'do'
    unaryExp
;

forExp
:   'for'
    ID
    ':='
    exp
    'to'
    exp
    'do'
    unaryExp
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
    '='
    exp
|   'function'
    ID
    '('
    (   fieldDec
        (   ','
            fieldDec
        )*
    )?
    ')'
    ':'
    TYID
    '='
    exp
;

varDec
:   'var'
    ID
    ':='
    exp
|   'var'
    ID
    ':'
    TYID
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

WS
:   (   ' '
    |   '\t'
    |   '\n'
    |   '\r'
    |   '\f'
    )+ {$channel = HIDDEN;}
;
