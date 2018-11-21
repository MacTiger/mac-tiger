# Ordre du jour

1. Point sur la grammaire
2. Faire le point sur ce qui avait été mentionné par Tristan sur Discord
3. Faire le point sur l'avancement des AST

# Script

Philippe a corrigés quelques bugs avec for. Les tests automatiques ne sont pas encore opérationnels, mais on peut faire les tests depuis antlr. Pas mal de choses ne passaient pas.

S'il y a un problème dans la reconnaissance, il n'y a apparemment pas de code d'erreur. Il faudra tester la sortie standard.

La grammaire n'est pas encore fonctionnelle, il faudra voir quelles règles ne sont pas fonctionnelles. Tristan a décrit les règles qui ne sont pas opérationnelles. tyId est apparemment problèmatique.

Il faudra fusionner tyId et Id. Il s'agit d'un travail assez important, d'autant qu'on a un autre problème : les affectations. Une affectation commence aussi par un Id/typeId.

David propose de faire beaucoup de tests.

Il faut tester les AST sur nos tests. Il n'est pas nécessaire de réécrire toutes les règles. David rappelle qu'il y a un manuel Antlr sur le arche. Il y a tout un chapitre sur les réécritures (chapitre 8, page 191).

Il faudrait commencer la table des symboles, et comment récupérer l'arbre abstrait en Java pour faire les contrôles sémantiques.

# Travail à faire

Chacun doit écrire au moins deux tests par règle. Il doivent être les plus petits possibles. On reprend les mêmes groupes qu'avant en ce qui concerne les règles de grammaires. Chacun doit poursuivre l'AST et la grammaire.

Tout le monde doit faire des tests.

## Alexis

Tester les réécritures.

## Tristan

Expliquer aux autres les problèmes de la grammaire.

## David

Mettre en ligne le PDF de Antlr.

## Philippe

Étudier la table des symboles.
