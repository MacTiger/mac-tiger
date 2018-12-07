# Réunion 7

**Date et heure.** 21 novembre 2018 à 14h30

**Prochaine réunion.** 28 novembre 2018 à 14h00

**Ordre du jour.**

1. Jalons
2. Table des symboles
3. Travail à faire

## 1. Jalons

La partie explication de l'AST n'est pas complètement pertinente. Il faut faire une ou deux lignes pour les règles très simples, et expliquer plus en détails les règles de l'AST qui sont plus compliquées.

Philippe a bien réalisé les règles de l'AST qu'il manquait.

David a cherché dans la documention d'Antlr des notes liées à l'AST, mais il n'y a pas grand chose. Antlr ne propose pas de gestion immédiate de la table des symboles. Il va falloir réfléchir à ce que l'on met dans la TDS, et comment on organise le travail.

Tristan a revu ce qu'avait fait Philippe et a remarqué qu'il manquait l'AST de la règle *recTy*. Ensuite, il a raccourci un peu la grammaire en enlevant les non-terminaux inutiles. David ne comprend pas.

Tristan n'a pas encore touché au rapport mais a lu. Il a changé les noms des sous-arbres que Philippe a utilisé.

## 2. Table des symboles

Tristan est allé au tutorat qui n'a pas été très long. La TDS a été évoqué. Chacun la fait à sa manière. Tout le monde produira des TDS différentes, que ce soit le nombre de tableaux, le nombre de lignes, etc. Il est possible de réaliser une TDS par bloc, *ie.* par boucle, fonction et let.

Tristan se demande où doivent apparaîtres les fonctions *built-in*, c'est-à-dire les fonctions comme *print*, et les types *int*, *string*...

On pourrait créer une table des symboles *globale* qui pourrait contenir toutes ces fonctions. Le parcours de l'arbre ne peut pas être linéaire.

David remarque qu'il nous reste trois semaines, et qu'il va falloir bosser la TDS. Tout le monde fait Anim'est sauf Tristan qui pourra miner le terrain pour la TDS. Un noeud de notre AST, ses fils sont les colonnes de la TDS.

## 3. Travail à faire

**Alexis.**

- Coder un programme Tiger complexe
- Effectuer cinq tests sémantiques

**Philippe**.

- Coder un programme Tiger complexe
- Effectuer cinq tests sémantiques

**David.**

- Coder un programme Tiger complexe
- Effectuer cinq tests sémantiques

**Tristan.**

- Coder un programme Tiger complexe
- Effectuer cinq tests sémantiques
- Se renseigner d'avantage sur la TDS

