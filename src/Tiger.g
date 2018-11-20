grammar Tiger;

options {
    language = Java;
    output = AST;
    backtrack = false;
    k = 1;
}

tokens { // Tokens imaginaires
    SEQ;
    ARR;
    REC;
    CALL;
    ITEM;
    FIELD;
    DEC;
    ARRTYPE;
    RECTYPE;
    CALLTYPE;
}

program
:   exp EOF!
;

exp
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
:   mulExp
    (   '+'^
        mulExp
        (   '+'!
            mulExp
        )*
    )?
    (   '-'^
        mulExp
        (   '+'^
            mulExp
            (   '+'!
                mulExp
            )*
        )?
    )*
;

mulExp
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
|   ifExp
|   whileExp
|   forExp
|   letExp
|   STR
|   INT
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
    (-> ^(SEQ exp*))
;

negExp
:   '-'^
    unaryExp
;

valueExp // Gère l'ancien lValue, callExp, arrCreate et recCreate
:   ID
    (-> ID)
    (   '('
        (   exp
            (   ','
                exp
            )*
        )?
        ')'
        (-> ^(CALL $valueExp exp*)) // Cas "callExp"
    |   indexArg
        (   (-> ^(ITEM $valueExp indexArg)) // Cas "subscript"
            (   indexArg
                (-> ^(ITEM $valueExp indexArg)) // Cas "subscript"
            |   fieldArg
                (-> ^(FIELD $valueExp fieldArg)) // Cas "fieldExp"
            )*
        |   'of'
            unaryExp
            (-> ^(ARR $valueExp indexArg unaryExp)) // Cas "arrayCreate"
        )
    |   fieldArg
        (-> ^(FIELD $valueExp fieldArg)) // Cas "fieldExp"
        (   indexArg
            (-> ^(ITEM $valueExp indexArg)) // Cas "subscript"
        |   fieldArg
            (-> ^(FIELD $valueExp fieldArg)) // Cas "fieldExp"
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
        (-> ^(REC $valueExp (ID exp)*)) // Cas "recCreate"
    )?
;

indexArg
:   '['!
    exp
    ']'!
;

fieldArg
:   '.'!
    ID
;

ifExp
:   'if'^
    exp
    'then'!
    unaryExp
    (options {
        greedy = true;
    }:  'else'!
        unaryExp
    )?
;
// En ascendante, conflit lecture/reduction : lire 'else' ou reduire ? Normalement lecture.

whileExp
:   'while'^
    exp
    'do'!
    unaryExp
;

forExp
:   'for'^
    ID
    ':='!
    exp
    'to'!
    exp
    'do'!
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
    (-> ^('let' ^(DEC dec+) ^(SEQ exp*)))
;

dec
:   tyDec
|   funDec
|   varDec
;

tyDec
:   'type'
    ID
    '='
    ty
    (-> ^('type' ID ty))
;

ty
:   ID
    (-> ID)
|   arrTy
|   recTy
;

arrTy
:   'array'
    'of'
    ID
    (-> ^(ARRTYPE ID))
;

recTy
:   '{'
    (   fieldDec
        (   ','
            fieldDec
        )*
    )?
    '}'
    (-> ^(RECTYPE fieldDec*))
;

fieldDec
:   ID
    ':'!
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
    (-> ^('function' ID ^(CALLTYPE fieldDec*) ID? exp))
;

varDec
:   'var'
    ID
    (   ':'
        ID
    )?
    ':='
    exp
    (-> ^('var' ID ID? exp))
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

STR
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

INT
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
    )
    {$channel = HIDDEN;}
;

WS
:   (   ' '
    |   '\t'
    |   '\n'
    |   '\r'
    |   '\f'
    )+
    {$channel = HIDDEN;}
;
