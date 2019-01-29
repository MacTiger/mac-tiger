# Réunion 3

**Date et heure.** 24 octobre 2018 à 14h30

**Prochaine réunion.** 2 novembre 2018 à 16h00

**Ordre du jour.**

1. Jalons
2. Grammaire
3. Travail à faire

## 1. Jalons

Tristan a beaucoup réorganisé la grammaire afin que la priorisation soit faite. Tristan nous explique comment il a fait. Il a remit les terminaux à la fin. Les affectations n'ont pas été faites. Pour ce qui est des `if else`, il y a encore des ambigüités.

## 2. Grammaire

D'après David, on devrait pouvoir lancer une grammaire, malgré l'ambigüité, car ANTLR prends la première solution qu'il voit (on a toujours des warning, par contre).

Problèmes avec `unaryExp`. On a rendu `expression` synonyme de `infixExp`, qui lui même est synonyme de `orExp`. 

Tout le monde ira au tutorat. Il reste trois jours pour faire la grammaire. Après, il faudra commencer à faire l'AST.

## 3. Travail à faire

**Alexis.**

+ Terminer les règles : tout ce qui est avant exp exclus plus letexp, et pour les terminaux, *tyid* et *id*

**Philippe**.

+ Terminer les règles : tout ce qui est avant exp exclus plus letexp, et pour les terminaux, *tyid* et *id*

**David.**

+ Terminer les règles : tout ce qui est après exp inclus, tous les autres terminaux (*infixop*, *intlit*, *stringlit*)

**Tristan.**

+ Terminer les règles : tout ce qui est après exp inclus, tous les autres terminaux (*infixop*, *intlit*, *stringlit*)