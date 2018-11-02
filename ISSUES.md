# Grammaire

## Priorisation de l'affectation

### Description du problème

La grammaire de référence donnée dans [la spécification](doc/tiger-specification.pdf) décrit à l'origine la règle de réécriture d'un affectation comme suit :

```antlr
assignment
:   lValue
    ':='
    exp
;
```

Dans notre grammaire, la partie droite d'une affectation est représentée par le non-terminal `assignmentArg`, donc la règle de réécriture est la suivante :

```antlr
assignmentArg
:   ':='
    unaryExp
;
```

La principale différence entre ces deux règles de réécriture se trouve dans le non-terminal en fin de réécriture : `exp` dans le cas de la grammaire de référence et `unaryExp` dans le cas de notre grammaire. Le non-terminal `unaryExp` représente toute expression qui n'est pas une opération infixe, une boucle, ou un test. Autrement dit, ce genre d'expression n'est pas accepté par notre grammaire en l'état actuel.

Ainsi, tandis que le programme `abc := 012 | 345` est valide syntaxiquement dans la grammaire de référence, il ne l'est pas dans la nôtre (ici la partie droite de l'affectation est effectivement une opération infixe).

Remplacer l'occurence du non-terminal `unaryExp` par `exp` ne suffit pas car cela rend la grammaire ambiguë (comment interpréter `abc := 012 | 345` ? (`abc := 012`)` | 345` ou `abc := `(`012 | 345`) ?).

### Tentative de résolution

Il s'agit donc ici d'un problème de priorisation. En effet la spécification indique que l'opérateur `:=` a la plus faible précédence (juste derrière l'opérateur `|`). Pour résoudre le problème, une solution consiste à remonter l'affectation - qui apparaît actuellement dans la règle de réécriture `unaryExp` - au niveau de `exp`.

Cependant, l'affectation `assignment` a déjà été factorisée avec `lValue` en un non-terminal `valueExp` :

```antlr
valueExp
:   ID
    (   seqArg
    |   (   indexArg
        |   fieldArg
        )*
        assignmentArg?
    )
;
```

Or, dans le cas où le non-terminal `valueExp` se réécrit en autre chose qu'une affectation (accès à une variable, appel de fonction, accès à un item d'un tableau ou accès à un champ d'un enregistrement), il ne doit pas devenir prioritaire sur les autres opérations, n'en comportant pas lui-même. Pour séparer les deux comportements tout en gardant la factorisation actuelle, on doit connaître à l'avance s'il va s'agir d'une affectation ou non : le langage *Tiger* n'est vraisemblablement pas *LL(1)*. Le créateur d'*ANTLR* indiquait d'ailleurs en 1995 en réponse à [une question](https://groups.google.com/forum/#!topic/comp.compilers/_HYhcfpRctg) que le langage mêlant expressions et affectations n'était sans doute même pas *LL(k)*.

### Proposition de résolution

Une solution envisageable est de faire de ce problème syntaxique un problème sémantique, c'est-à-dire en redéfinissant la règle de réécriture d'origine de la grammaire de référence en :

```antlr
assignment
:   exp
    ':='
    exp
;
```

Il suffit sans doute juste de vérifier si la partie de gauche de l'affectation est bien une cible d'affectation sémantiquement valide (ni une opération infixe ni une boucle, un test, un appel de fonction ou une autre affectation). C'est d'ailleurs ce qu'ont l'air de proposer certains manuels de compilation d'après l'auteur de la question sur les langages mêlant expressions et affectations.
