grammar Tiger;

options {
    language = Java;
    output = AST;
    backtrack = false;
    k = 1;
}

tokens { // Tokens imaginaires
    ARR;
    REC;
    CALLEXP;
    SUBSCRIPT;
    FIELDEXP;
    SEQARG;
    INDEXARG;
    FIELDARG;
    LET;
    ARRAY;
    IF;
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
    ')' -> exp*
;

negExp
:   '-'^
    unaryExp
;

valueExp // Gère l'ancien lValue, callExp, arrCreate et recCreate
:   ID
    (   seqArg
        (-> ^(CALLEXP ID seqArg)) // Cas "callExp"
    |   '['
        exp
        ']'
        (   (-> ^(SUBSCRIPT ID)) // Cas "subscript"
            (-> ^(INDEXARG exp))
            (   indexArg
                (-> ^(SUBSCRIPT ID indexArg)) // Cas "subscript"
            |   fieldArg
                (-> ^(FIELDEXP ID fieldArg)) // Cas "fieldExp"
            )*
        |   'of'
            unaryExp
            (-> ^(ARR ID exp unaryExp)) // Cas "arrayCreate"
        )
    |   fieldArg
        (-> ^(FIELDEXP ID fieldArg)) // Cas "fieldExp"
        (   indexArg
            (-> ^(SUBSCRIPT ID indexArg)) // Cas "subscript"
        |   fieldArg
            (-> ^(FIELDEXP ID fieldArg)) // Cas "fieldExp"
        )*
    |   '{'
        (   ID
            '='
            exp
            (   ','
                ID
                '='
                exp
            )*
        )?
        '}'
        (-> ^(REC ID (ID exp)*)) // Cas "recCreate"
    )?
;

seqArg
:   '('
    (   exp
        (   ','
            exp
        )*
    )?
    ')' -> ^(SEQARG exp*)
;

indexArg
:   '['
    exp
    ']' -> ^(INDEXARG exp)
;

fieldArg
:   '.'
    ID -> ^(FIELDARG ID)
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
|   funDec -> ^(funDec)
|   varDec -> ^(varDec)
;

tyDec
:   'type'
    ID
    '='
    ty -> ^(LET ID ty)
;

ty
:   ID
    arrTy
    recTy -> ^(ID arrTy recTy)
;

arrTy
:   'array'
    'of'
    ID -> ^(ARRAY ID)
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
    ID
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
        ID
    )?
    '='
    exp
;

varDec
:   'var'
    ID
    (   ':'
        ID
    )?
    ':='
    exp -> ^(LET ID ID? exp)
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
    )? -> ^(IF exp exp exp?)
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

ID
:   (   'A'..'Z'
    |   'a'..'z'
    )
    (   '0'..'9'
    |   'A'..'Z'
    |   '_'
    |   'a'..'z'
    )*
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
