grammar Tiger;

options {
    language = Java;
    //  output = AST;
    backtrack = false;
    k = 1;
}

//  prog
//  :   exp
//  ;

STRINGLIT
:   '"'
    (   ' '..'~'
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

letexp
:   'let' dec+ 'in' (exp(';'exp)*)? 'end'
;

dec
:   (   tydec
    |   vardec
    |   fundec
    )
;

tydec
:
    'type' TYID '=' ty
;

ty
:
    (   TYID
    |   arrty
    |   recty
    )
;

arrty
:
    'array' 'of' TYID
;

recty
:   
    '{' (fielddec(','fielddec)*)? '}'
;

fieldrec
:
     ID ':' TYID
;

fundrec
:    (  'function' ID '(' (fielddec(','fielddec)*)? ')' = exp
     |  'function' ID '(' (fielddec(','fielddec)*)? ')' : TYID = exp
     )
;

vardec
:    (  'var' ID ':=' exp
     |  'var' ID ':' TYID ':=' exp
     )
;

lvalue
:    (   ID
     |   subscript
     |   fieldexp
;

subscript
:    lvalue '[' exp ']'
;

fieldexp
:    lvalue '.' ID
;