# Réunion 6

## 1. Informations

- Date et heure : 14 novembre 2018
- Lieu : médiathèque

## 2. Ordre du jour

- Point sur les tâches effectuées
- Remarques de Tristan
- Commencer à parler de la TDS
- Jalons pour la prochaine réunion

## 3. Point sur les tâches effectuées

Alexis a testé toutes ses réécritures et elles sont fonctionnelles. Philippe a ajouté quelques tests ainsi que les exemples envoyés par les enseignants. Philippe a réécrit sa règle `fieldExp` et va écrire `funDec` qui n'avait pas été faite.

David a débloqué Tristan et a fini l'AST du `letExp`, et a ajouté quelques tests pour voir si ça marchait bien.

## 4. Remarques de Tristan

### 4.1. Retour sur le tutorat

Ce tutorat a été utile car il a permet de mettre en lumière les problèmes qui n'étaient pas très clair. Philippe remarque que l'AST était particulièrement éclairant.

Mais les problèmes évoqués ont déjà été résolus. Par contre, tout ce qui a été évoqué, que ce soit des problèmes ou des exemples évoqués, sont bien en correspondance avec ce que nous avons fait. Exemple : les arbres n-aires pour les séquences.

Cela valide la liberté que l'on a prise d'autoriser n'importe quelle expression à gauche d'une affectation et pas seulement un identifiant, et de faire le test dans le contrôle sémantique. La structure proposée pendant le tutorat correspond à ce que nous avons fait.

### 4.2. Remarque sur les crochets et les points

Dans ce que Tristan avait à faire : résolution d'un problème d'AST de David lié aux suites de points et de crochets qui n'incluaient que le dernier. Lorsque l'on faisait `a.b.c`, l'AST n'affichait que `a.c`. David avait déjà rencontré ce problème avant l'AST, mais tout a été résolu.

Tristan a factorisé mercredi dernier les types et les identifiants.

### 4.3. Tests

Tristan a mit une vingtaine de tests qui échouent, et ce volontairement.

### 4.4. Coloration syntaxique

Tristan a créé un paquet Atom pour faire de la coloration syntaxique sur Tiger.

On peut l'installer depuis Atom directement.

### 4.5. Priorisation des tests et des boucles

Soit les tests suivants, écrits en Tiger (un par ligne) :

```
(1) a + if b then c else d + e
(2) a + (if b then c else d := e)
(3) a + if b then c else (d + e)
(4) (a + if b then c else d) + e
(5) if a then b := c else d := f
(6) if a then (b := c) else d := f
(7) if a then (b := c) else (d := f)
```

Comme vu dans le tutorat toute à l'heure, on a deux possibilités de priorité dans le premier tests. Le `if` est-il l'opération la plus prioritaire, ou la moins prioritaire ? Le premier test possède deux interprétations possibles :

1. `if ... (d+e)`
2. `(if ... d) + e`

Dans la première interprétation, seuls les tests (2), (5), (6) et (7) passent. Dans la deuxième interprétation, tous les tests passent.

Le `if` renvoit apparemment bien quelque chose. Exemple.

```
a := if F() then (
	a := y // ici, if renvoit y
) else (
	a := z // ici, if renvoit z
)
```

La question est de savoir : est-ce qu'on doit considérer que le `a := ` au début de ce bloc de code est valide ou non ? Il est possible que cela ait du sens.

Pour l'instant, le premier test ne passe pas et provoque une erreur syntaxique, car on ne peut pas faire `a+if`. Il pourrait être intéressant d'utiliser la deuxième interprétation, c'est à dire celle ou après un `else`, on attend un `unaryExp`. Par contre, on a un petit problème, car on accepte des choses qu'on ne devrait pas accepter, en particulier :

```
a + if b then c else d := e
```

Ce test n'a aucun sens mais passe dans le deuxième cas.

En somme, qu'est-ce qu'on fait ? Pour l'instant, tout ce passe comme si `if` ne renvoyait rien.

### 4.6. Pour  le rapport

- Priorisation du `if`
- Priorisation de l'affectation
- Arbres n-aires pour |, &, +, et *

## 5. Table des symboles

Il va falloir se lancer dessus pour la prochaine réunion. David remarque qu'il nous reste trois semaines. Il faut qu'on fasse :

- Le rapport
- Table des symbole
- Finir la grammaire (déjà presque finie)

## 6. Jalons pour la prochaine réunion

**Alexis.**

- Écrire l'AST de la règle `funDec` et `fieldDec`
- Expliquer la priorisation du `if` dans le rapport
- Expliquer la partie de l'AST conçue dans le rapport
- Expliquer des arbres n-aires pour |, &, +, * et , (virgule)

**Philippe**.

- Écrire l'AST de la règle `funDec` et `fieldDec`
- Expliquer l'options *greedy* dans le rapport
- Expliquer la partie de l'AST conçue dans le rapport
- S'occuper du template LaTeX

**David.**

- Commencer la TDS
- Expliquer la priorisation de opérations arithmétiques dans le rapport
- Expliquer la partie de l'AST conçue dans le rapport

**Tristan.**

- Commencer la TDS
- Expliquer la partie de l'AST conçue dans le rapport
- Expliquer son package Atom dans le rapport
- Expliquer le EOF dans le rapport
- Priorisation de l'affectation
- Expliquer la factorisation de ID et TYID

