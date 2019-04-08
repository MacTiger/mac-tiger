# Réunion 19

**Date et heure.** 8 avril 2019 à 9 heures

**Prochaine réunion. **Demain

**Ordre du jour.**

- Organisation de la semaine

Chaque jour, on fera une petite réunion le matin pour décider ce que l'on fera pendant la journée et l'avancement par rapport au planning.

## Organisation de la semaine

Les opérateurs de bases : il faut revoir le `DIV` qui calcule le reste et le quotient. La fonction permettant de récupérer le décalage d'une variable a été fait (il faudrait peut être améliorer la syntaxe). David remarque qu'il faudra que l'on consacre un jour aux tests.

L'accès à un champ n'est pas encore fait. D'après Tristan, le tas se fait en une ligne. Par contre, pendant l'exécution du code Tiger, on n'a plus accès aux types : pas moyen de savoir les types de champs dans les structures. On ne connaît donc pas la taille des champs dans une structure.

`print_i` nécessite que l'on puisse lancer des fonctions. Tristan pense qu'il faudrait d'abord faire le `print` (reprendre le code de M. Parodi et le mettre aujourd'hui.

Dans nos tests futurs, il ne faudra pas mettre de chaînes de caractères trop longues. En effet, elles risquent de polluer notre code assembleur car il faut une instruction par caractère.

David : on a maintenant une *hashmap* qui permet d'associer à chaquue nœud de l'AST le type renvoyé par le fonction `fillWithXXX`. On peut donc maintenant faire des tests sur le type d'un nœud dans les fonctions `translate`.

Tristan est repassé sur le `Writer`. `writeFunction` n'a pas changé à part qu'il y a trois versions. On outre, `writeHeader` permet d'écrire toutes les fonctions natives (`print_i`, etc.). `writeMain` permet d'écrire juste avant le début du code de la première TDS afin d'effectuer les allocations de tests.
