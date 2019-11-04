# Compilateur du langage *Tiger*

## Tl;dr: démonstration rapide

Jouez au *Puissance 4* !

```shell
$ git clone https://github.com/MacTiger/mac-tiger
$ cd mac-tiger
$ make init build prompt < ansi-connect-four.tiger
```

## Présentation

Ce dépôt contient les sources du compilateur *Java* du langage *Tiger* réalisé par le groupe 6 dans le cadre du projet de compilation des langages 2018-2019 à *TELECOM Nancy*.

## Membres du groupe

-   Alexis DIEU (@Alexithub)
-   David FORLEN (@DavidForlen)
-   Philippe GRAFF (@PhilippsTelecom), chef de projet
-   Tristan LE GODAIS (@TrisTOON)

## Structure du dépôt

Ce dépôt contient :

-   la [grammaire](res/Tiger.g) *ANTLR3* du langage *Tiger*
-   le [compilateur](src/Main.java) écrit en *Java*
-   la [suite de tests](tst) portant sur l'analyse lexicale, l'analyse syntaxique, les contrôles sémantiques et l'exécution des programmes
-   le [manuel d'utilisation](#manuel-dutilisation)
-   les [notes](doc/notes/meetings) prises lors des réunions
-   les [notes](doc/notes/tutorings) prises lors des séances de tutorat
-   le [rapport](doc/reports/report-1.pdf) de la première partie du projet
-   le [rapport](doc/reports/report-2.pdf) de la deuxième partie du projet

## Manuel d'utilisation

### Récupération du dépôt en local

Pour utiliser le compilateur, commencez par créer une copie locale du dépôt :

```shell
$ git clone https://github.com/MacTiger/mac-tiger
$ cd mac-tiger
```

### Configuration des dépendances

Optionnellement, installez *Graphviz*.

Aucune configuration n'est requise par *MicroPIUP*.

### Construction du compilateur *Java*

À la racine du projet, générez les analyseurs lexical (`/src/lexical/TigerLexer.java`) et syntaxique (`/src/syntactic/TigerParser.java`) avec la commande suivante :

```shell
$ make init
```

Enfin, toujours à la racine du projet, construisez le compilateur *Java* :

```shell
$ make build
```

### Test d'un programme *Tiger*

Pour compiler un programme *Tiger* en particulier et afficher son graphe des tables des symboles, exécutez la commande suivante à la racine du projet en remplaçant `<file>` par le chemin vers le fichier le contenant :

```shell
$ make graph < <file>
```

Si le programme est invalide, des messages d'erreurs seront affichés sur la sortie d'erreur ; sinon le graphe des tables des symboles sera affiché.

Pour compiler et exécuter un programme *Tiger* en particulier, exécutez la commande suivante à la racine du projet en remplaçant `<file>` par le chemin vers le fichier le contenant :

```shell
$ make prompt < <file>
```

Si le programme est invalide, des messages d'erreurs seront affichés sur la sortie d'erreur ; sinon le code sera exécuté.

### Options du compilateur *Java*

Le compilateur *Java* dispose de plusieurs options permettant de contrôler sa sortie.

Pour moduler la sortie d'erreur :

-   `--no-color` : les messages d'erreurs lexicales, syntaxiques et sémantiques seront monochromes
-   `--color` (par défaut) : les messages d'erreurs lexicales, syntaxiques et sémantiques seront colorés

Pour moduler la sortie standard :

-   `--no-output` : seuls l'analyse lexicale, l'analyse syntaxique et les contrôles sémantiques seront réalisés et rien n'est émis sur la sortie standard
-   `--dot` : en plus de l'analyse lexicale, de l'analyse syntaxique et des contrôles sémantiques, du code *DOT* est émis sur la sortie standard pour permettre la génération du graphe des tables des symboles via *Graphviz*
-   `--src` (par défaut) : en plus de l'analyse lexicale, de l'analyse syntaxique et des contrôles sémantiques, du code *SRC* est émis sur la sortie standard pour permettre la génération de code via *MicroPIUP*

### Lancement de la suite de tests avec *Bash*

Pour lancer l'intégralité des tests lexicaux, syntaxiques, sémantiques et sur l'exécution, exécutez la commande suivante à la racine du projet (la durée peut varier selon la machine) :

```shell
$ make test
```

Une étiquette associée à un code couleur est utilisée pour symboliser le niveau de réussite d'un test. La signification de ces étiquettes est définie ainsi :

-   `[PASS]` : test qui a réussi ou échoué comme prévu
-   `[FAIL]` : test qui a réussi ou échoué alors que le contraire était attendu
-   `[EXIT]` : test produisant un plantage du compilateur
-   `[WARN]` : test produisant une erreur à une étape de la compilation antérieure à celle testée
