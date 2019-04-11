# Projet de compilation du langage *Tiger*

## Présentation

Ce dépôt contient les sources du compilateur *Java* du langage *Tiger* réalisé par le groupe 6 dans le cadre du projet de compilation des langages.

## Membres du groupe

- Alexis DIEU
- David FORLEN
- Philippe GRAFF (chef de projet)
- Tristan LE GODAIS

## Structure du dépôt

Ce dépôt contient :

- la [grammaire](res/Tiger.g) *ANTLR3* du langage *Tiger*
- le [compilateur](src/Main.java) écrit en *Java*
- la [suite de tests](tst) portant sur l'analyse lexicale, l'analyse syntaxique, les contrôles sémantiques et l'exécution des programmes
- le [manuel d'utilisation](#manuel-dutilisation)
- les [notes](notes/meeting) prises lors des réunions
- les [notes](notes/tutoring) prises lors des séances de tutorat
- le [rapport](reports/report-1.pdf) de la première partie du projet

## Manuel d'utilisation

### Récupération du dépôt en local

Pour utiliser le compilateur, commencez par créer une copie locale du dépôt :

```shell
$ git clone https://gitlab.telecomnancy.univ-lorraine.fr/Philippe.Graff/graff24u
$ cd graff24u
```

### Configuration des dépendances

Ajoutez *ANTLR3* à la variable d'environnement `CLASSPATH` en remplaçant `<path>` par le chemin absolu du projet :

```shell
$ echo 'export CLASSPATH=$CLASSPATH:<path>/graff24u/lib/antlr-3.5.2-complete.jar' >> ~/.bashrc
```

Fermez le terminal puis rouvrez-en un nouveau pour que les changements s'appliquent.

Optionnellement, à partir de la racine du projet, installez *Graphviz* :

```shell
$ tar -xvf lib/graphviz.tar.gz -C lib
$ cd lib/graphviz-2.40.1
$ ./configure
$ make
$ make install
```

Aucune configuration n'est requise par *MicroPIUP*

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

### Options du compilateur:

Le compilateur *Java* dispose de plusieurs options permettant de contrôler sa sortie.

Pour moduler la sortie d'erreur :

- `--no-color` : les messages d'erreurs lexicales, syntaxiques et sémantiques seront monochromes
- `--color` (par défaut) : les messages d'erreurs lexicales, syntaxiques et sémantiques seront colorés

Pour moduler la sortie standard :

- `--no-output` : seuls l'analyse lexicale, l'analyse syntaxique et les contrôles sémantiques seront réalisés et rien n'est émis sur la sortie standard
- `--dot` : en plus de l'analyse lexicale, de l'analyse syntaxique et des contrôles sémantiques, du code *DOT* est émis sur la sortie standard pour permettre la génération du graphe des tables des symboles via *Graphviz*
- `--src` (par défaut) : en plus de l'analyse lexicale, de l'analyse syntaxique et des contrôles sémantiques, du code *SRC* est émis sur la sortie standard pour permettre la génération de code via *MicroPIUP*

### Lancement de la suite de tests avec *Bash*

Pour lancer l'intégralité des tests lexicaux, syntaxiques, sémantiques et sur l'exécution, exécutez la commande suivante à la racine du projet (la durée peut varier selon la machine) :

```shell
$ make test
```

Une étiquette associée à un code couleur est utilisée pour symboliser le niveau de réussite d'un test. La signification de ces étiquettes est définie ainsi :

- `[PASS]` : test qui a réussi ou échoué comme prévu
- `[FAIL]` : test qui a réussi ou échoué alors que le contraire était attendu
- `[EXIT]` : test produisant un plantage du compilateur
- `[WARN]` : test produisant une erreur à une étape de la compilation antérieure à celle testée
