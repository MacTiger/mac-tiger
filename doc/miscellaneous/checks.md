# Contrôles sémantiques

- On ne doit pas diviser par zéro en clair
- L'index d'un subscript (Array) doit être entier à sa définition et pour tout accès à ses valeurs.
- On ne doit pas utiliser une variable en dehors de sa portée de déclaration
- On ne peut pas définir deux fois la même variable (ou type, ou fonction) dans le même bloc
- Une négation porte obligatoirement sur un int
- Lors de l'appel d'une fonction, le nombre d'arguments utilisés pour l'appel doit correspondre à celui de la définition de la fonction
- Lors de l'appel d'une fonction, les types des paramètres doivent correspondre à ceux utilisés lors de la définition de la fonction
- Si on a défini un type de retour d'une fonction, il faut que l'on retourne un objet de même type. En tenir compte pour une éventuelle affectation.
- Si on définit un type contenant des champs, on ne peut pas chercher à modifier un champ n'ayant pas été déclaré. Cf tests/fail/structureFiled
- Si une variable est de type record et que l'on souhaite accéder à l'un de ses champs, il faut que ce champ existe
- "+", "-", "\*", "/" : les opérandes (et le résultat) sont de type INT
- "=","<>" : les opérandes sont de même type, le résultat est un INT
- ">","<","<=",">=" : les opérandes sont de même type, le résultat est un INT
- On ne peut pas déclarer deux types en les assignant l'un à l'autre
- Si une variable est de type `record`, soit elle vaut `nil`, soit ses champs sont exactement ceux utilisés lors de la déclaration du `record`
- On ne peut pas déclarer une variable en lui assignant une variable déclarée après
- Une fonction ne peut pas avoir deux paramètres ayant le même nom
- Un record ne peut avoir deux champs portant le même nom
