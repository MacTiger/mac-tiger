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
    FIELDEC;
    REC;
    CALL;
    ITEM;
    FIELD;
    TYPEARRAY;
    TYPEDEC;
    VARDEC;
    LET;
    IN;
    LETEXP;
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
        (   ATTR
            '='
            exp
            (   ','
                ATTR
                '='
                exp
            )*
        )?
        '}'
        (-> ^(REC $valueExp (ATTR exp)*)) // Cas "recCreate"
    )?
;

indexArg
:   '['!
    exp
    ']'!
;

fieldArg
:   '.'!
    ATTR
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
    -> ^(LETEXP ^(LET dec+) ^(IN exp*))
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
:   ATTR
    ':'
    ID -> ATTR ID
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
    exp -> ^(FUNDEC ID ^(FIELDEC fieldDec*) ID? exp)
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

ID
:   'a'
    (   'r'
        (   'r'
            (   'a'
                (   'y'
                    IDORATTRSUFFIX+
                |   (   '0'..'9'
                    |   'A'..'Z'
                    |   '_'
                    |   'a'..'x'
                    |   'z'
                    )
                    IDORATTRSUFFIX*
                )?
            |   (   '0'..'9'
                |   'A'..'Z'
                |   '_'
                |   'b'..'z'
                )
                IDORATTRSUFFIX*
            )?
        |   (   '0'..'9'
            |   'A'..'Z'
            |   '_'
            |   'a'..'q'
            |   's'..'z'
            )
            IDORATTRSUFFIX*
        )?
    |   (   '0'..'9'
        |   'A'..'Z'
        |   '_'
        |   'a'..'q'
        |   's'..'z'
        )
        IDORATTRSUFFIX*
    )?
|   'b'
    (   'r'
        (   'e'
            (   'a'
                (   'k'
                    IDORATTRSUFFIX+
                |   (   '0'..'9'
                    |   'A'..'Z'
                    |   '_'
                    |   'a'..'j'
                    |   'l'..'z'
                    )
                    IDORATTRSUFFIX*
                )?
            |   (   '0'..'9'
                |   'A'..'Z'
                |   '_'
                |   'b'..'z'
                )
                IDORATTRSUFFIX*
            )?
        |   (   '0'..'9'
            |   'A'..'Z'
            |   '_'
            |   'a'..'d'
            |   'f'..'z'
            )
            IDORATTRSUFFIX*
        )?
    |   (   '0'..'9'
        |   'A'..'Z'
        |   '_'
        |   'a'..'q'
        |   's'..'z'
        )
        IDORATTRSUFFIX*
    )?
|   'd'
    (   'o'
        IDORATTRSUFFIX+
    |   (   '0'..'9'
        |   'A'..'Z'
        |   '_'
        |   'a'..'n'
        |   'p'..'z'
        )
        IDORATTRSUFFIX*
    )?
|   'e'
    (   'l'
        (   's'
            (   'e'
                IDORATTRSUFFIX+
            |   (   '0'..'9'
                |   'A'..'Z'
                |   '_'
                |   'a'..'d'
                |   'f'..'z'
                )
                IDORATTRSUFFIX*
            )?
        |   (   '0'..'9'
            |   'A'..'Z'
            |   '_'
            |   'a'..'r'
            |   't'..'z'
            )
            IDORATTRSUFFIX*
        )?
    |   'n'
        (   'd'
            IDORATTRSUFFIX+
        |   (   '0'..'9'
            |   'A'..'Z'
            |   '_'
            |   'a'..'c'
            |   'e'..'z'
            )
            IDORATTRSUFFIX*
        )?
    |   (   '0'..'9'
        |   'A'..'Z'
        |   '_'
        |   'a'..'k'
        |   'm'
        |   'o'..'z'
        )
        IDORATTRSUFFIX*
    )?
|   'f'
    (   'o'
        (   'r'
            IDORATTRSUFFIX+
        |   (   '0'..'9'
            |   'A'..'Z'
            |   '_'
            |   'a'..'q'
            |   's'..'z'
            )
            IDORATTRSUFFIX*
        )?
    |   'u'
        (   'n'
            (   'c'
                (   't'
                    (   'i'
                        (   'o'
                            (   'n'
                                IDORATTRSUFFIX+
                            |   (   '0'..'9'
                                |   'A'..'Z'
                                |   '_'
                                |   'a'..'m'
                                |   'o'..'z'
                                )
                                IDORATTRSUFFIX*
                            )?
                        |   (   '0'..'9'
                            |   'A'..'Z'
                            |   '_'
                            |   'a'..'n'
                            |   'p'..'z'
                            )
                            IDORATTRSUFFIX*
                        )?
                    |   (   '0'..'9'
                        |   'A'..'Z'
                        |   '_'
                        |   'a'..'h'
                        |   'j'..'z'
                        )
                        IDORATTRSUFFIX*
                    )?
                |   (   '0'..'9'
                    |   'A'..'Z'
                    |   '_'
                    |   'a'..'s'
                    |   'u'..'z'
                    )
                    IDORATTRSUFFIX*
                )?
            |   (   '0'..'9'
                |   'A'..'Z'
                |   '_'
                |   'a'..'b'
                |   'd'..'z'
                )
                IDORATTRSUFFIX*
            )?
        |   (   '0'..'9'
            |   'A'..'Z'
            |   '_'
            |   'a'..'m'
            |   'o'..'z'
            )
            IDORATTRSUFFIX*
        )?
    |   (   '0'..'9'
        |   'A'..'Z'
        |   '_'
        |   'a'..'n'
        |   'p'..'t'
        |   'v'..'z'
        )
        IDORATTRSUFFIX*
    )?
|   'i'
    (   'f'
        IDORATTRSUFFIX+
    |   'n'
        IDORATTRSUFFIX+
    |   (   '0'..'9'
        |   'A'..'Z'
        |   '_'
        |   'a'..'e'
        |   'g'..'m'
        |   'o'..'z'
        )
        IDORATTRSUFFIX*
    )?
|   'l'
    (   'e'
        (   't'
            IDORATTRSUFFIX+
        |   (   '0'..'9'
            |   'A'..'Z'
            |   '_'
            |   'a'..'s'
            |   'u'..'z'
            )
            IDORATTRSUFFIX*
        )?
    |   (   '0'..'9'
        |   'A'..'Z'
        |   '_'
        |   'a'..'d'
        |   'f'..'z'
        )
        IDORATTRSUFFIX*
    )?
|   'n'
    (   'i'
        (   'l'
            IDORATTRSUFFIX+
        |   (   '0'..'9'
            |   'A'..'Z'
            |   '_'
            |   'a'..'k'
            |   'm'..'z'
            )
            IDORATTRSUFFIX*
        )?
    |   (   '0'..'9'
        |   'A'..'Z'
        |   '_'
        |   'a'..'h'
        |   'j'..'z'
        )
        IDORATTRSUFFIX*
    )?
|   'o'
    (   'f'
        IDORATTRSUFFIX+
    |   (   '0'..'9'
        |   'A'..'Z'
        |   '_'
        |   'a'..'e'
        |   'g'..'z'
        )
        IDORATTRSUFFIX*
    )?
|   't'
    (   'h'
        (   'e'
            (   'n'
                IDORATTRSUFFIX+
            |   (   '0'..'9'
                |   'A'..'Z'
                |   '_'
                |   'a'..'m'
                |   'o'..'z'
                )
                IDORATTRSUFFIX*
            )?
        |   (   '0'..'9'
            |   'A'..'Z'
            |   '_'
            |   'a'..'d'
            |   'f'..'z'
            )
            IDORATTRSUFFIX*
        )?
    |   'o'
        IDORATTRSUFFIX+
    |   'y'
        (   'p'
            (   'e'
                IDORATTRSUFFIX+
            |   (   '0'..'9'
                |   'A'..'Z'
                |   '_'
                |   'a'..'d'
                |   'f'..'z'
                )
                IDORATTRSUFFIX*
            )?
        |   (   '0'..'9'
            |   'A'..'Z'
            |   '_'
            |   'a'..'o'
            |   'q'..'z'
            )
            IDORATTRSUFFIX*
        )?
    |   (   '0'..'9'
        |   'A'..'Z'
        |   '_'
        |   'a'..'g'
        |   'i'..'n'
        |   'p'..'x'
        |   'z'
        )
        IDORATTRSUFFIX*
    )?
|   'v'
    (   'a'
        (   'r'
            IDORATTRSUFFIX+
        |   (   '0'..'9'
            |   'A'..'Z'
            |   '_'
            |   'a'..'q'
            |   's'..'z'
            )
            IDORATTRSUFFIX*
        )?
    |   (   '0'..'9'
        |   'A'..'Z'
        |   '_'
        |   'b'..'z'
        )
        IDORATTRSUFFIX*
    )?
|   'w'
    (   'h'
        (   'i'
            (   'l'
                (   'e'
                    IDORATTRSUFFIX+
                |   (   '0'..'9'
                    |   'A'..'Z'
                    |   '_'
                    |   'a'..'d'
                    |   'f'..'z'
                    )
                    IDORATTRSUFFIX*
                )?
            |   (   '0'..'9'
                |   'A'..'Z'
                |   '_'
                |   'a'..'k'
                |   'm'..'z'
                )
                IDORATTRSUFFIX*
            )?
        |   (   '0'..'9'
            |   'A'..'Z'
            |   '_'
            |   'a'..'h'
            |   'j'..'z'
            )
            IDORATTRSUFFIX*
        )?
    |   (   '0'..'9'
        |   'A'..'Z'
        |   '_'
        |   'a'..'g'
        |   'i'..'z'
        )
        IDORATTRSUFFIX*
    )?
|   (   'A'..'Z'
    |   'c'
    |   'g'..'h'
    |   'j'..'k'
    |   'm'
    |   'p'..'s'
    |   'u'
    |   'x'..'z'
    )
    IDORATTRSUFFIX*
;

ATTR
:   (   'A'..'Z'
    |   'a'..'z'
    )
    IDORATTRSUFFIX*
;

IDORATTRSUFFIX
:   '0'..'9'
|   'A'..'Z'
|   '_'
|   'a'..'z'
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
