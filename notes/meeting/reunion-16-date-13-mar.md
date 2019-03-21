# Réunion 15

**Date et heure.** 13 mars 2018 à 16 heures

**Prochaine réunion.** 22 mars 2018 à 16 heures

**Ordre du jour.**

- Travail effectué
- Problèmes à régler
- Travail à faire

## Travail effectué

David a travaillé sur le pseudo remplissage. Lorsque le `fillWithLet` sera remplacé, le reste sera facile à réalisé. Une classe `TigerTranslator` a été codée. Celle-ci a exactement la même structure que la TDS, mais sert à générer le code à partir de la TDS.

David propose d'utiliser plusieurs `TigerTranslator` afin de pouvoir gérer le sens des enfants. 

En ce qui concerne les tests, Tristan avait rangé les tests pour la soutenance. Il a rangé l'intégralité des tests qui sont dans `semantic/fail`. Il a également réalisé le programme de concaténation, et a corrigé un bug présent dans `let`. On n'avait pas vérifié que le code d'une fonction était cohérent avec son type de retour.

Le nouveau système de test est en cours mais est compliqué car micropiupk ne donne pas de code d'erreur. L'exécution du code machine peut aboutir à une erreur, mais le code est toujours le même.

David propose que l'on se concentre sur la génération de code pour le moment. On verra le rapport un peu plus tard.

## Problèmes à régler

Comment on gère les registres ? Ceux-ci peuvent être libres ou non. On peut utiliser un système de tableau comme évoqué en tutorat. On a aussi la possibilité d'utiliser une pile des registres libres.

Proposition de Tristan : les fonctions `fillWithXXX` doivent renvoyer le nombre de registres à utiliser.

## Travail à faire

**Alexis.**

- Finir de lire la documentation MicroPIUPK
- Écrire le code assembleur des opérations de bases (arithmétiques et comparaisons) 

**Philippe**.

- Finir de lire la documentation MicroPIUPK
- Écrire le code assembleur des opérations de bases (arithmétiques et comparaisons) 

**David.**

- Pseudo remplissage

**Tristan.**

- Mettre à jour le fichier des contrôles sémantiques 
- Commenter son code !important
- Mettre à jour le système de tests pour prendre en compte les tests à l'exécution
- Programmer le `print_i` en assembleur

