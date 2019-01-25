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
        orExp
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

valueExp // Gère les anciens "lValue", "callExp", "arrCreate" et "recCreate"
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
    |   '['
        exp
        ']'
        (   (-> ^(ITEM $valueExp exp)) // Cas "subscript"
            (   '['
                exp
                ']'
                (-> ^(ITEM $valueExp exp)) // Cas "subscript"
            |   '.'
                ID
                (-> ^(FIELD $valueExp ID)) // Cas "fieldExp"
            )*
        |   'of'
            unaryExp
            (-> ^(ARR $valueExp exp unaryExp)) // Cas "arrayCreate"
        )
    |   '.'
        ID
        (-> ^(FIELD $valueExp ID)) // Cas "fieldExp"
        (   '['
            exp
            ']'
            (-> ^(ITEM $valueExp exp)) // Cas "subscript"
        |   '.'
            ID
            (-> ^(FIELD $valueExp ID)) // Cas "fieldExp"
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
    (   ID
        (-> ^('type' ID ID)) // Cas "ty"
    |   'array'
        'of'
        ID
        (-> ^('type' ID ^(ARRTYPE ID))) // Cas "arrTy"
    |   '{'
        (   ID
            ':'
            ID
            (   ','
                ID
                ':'
                ID
            )*
        )?
        '}'
        (-> ^('type' ID ^(RECTYPE (ID ID)*))) // Cas "recTy"
    )
;

funDec
:   'function'
    ID
    '('
    (   i += ID
        ':'
        j += ID
        (   ','
            i += ID
            ':'
            j += ID
        )*
    )?
    ')'
    (   ':'
        k = ID
    )?
    '='
    exp
    (-> ^('function' ID ^(CALLTYPE ($i $j)*) $k? exp))
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
        |   (   ' ' // Échappement de caractères blancs
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
