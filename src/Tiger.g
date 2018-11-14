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
    FUNDEC;
    REC;
    CALL;
    ITEM;
    FIELD;
    TYPEARRAY;
    TYPEDEC;
    VARDEC;
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
    -> ^('let' dec+ exp*)
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
    ty -> ^(TYPEDEC ID ty)
;

ty
:   ID
|   arrTy
|   recTy
;

arrTy
:   'array'
    'of'
    ID -> ^(TYPEARRAY ID)
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
    exp -> ^(FUNDEC ID fieldDec* ID? exp)
;

varDec
:   'var'
    ID
    (   ':'
        ID
    )?
    ':='
    exp 
    -> ^(VARDEC ID ID? exp)
;

ifExp
:   'if'^
    exp
    'then'!
    exp
    (options {
        greedy = true;
    }:  'else'!
        exp
    )?
;
// En ascendante, conflit lecture/reduction : lire 'else' ou reduire ? Normalement lecture.

whileExp
:   'while'^
    exp
    'do'!
    exp
;

forExp
:   'for'^
    ID
    ':='!
    exp
    'to'!
    exp
    'do'!
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
