# Projet de compilation du langage *Tiger*

## Présentation

Ce dépôt contient les sources du compilateur du langage *Tiger* en code assembleur *microPIUP/ASM* réalisé par le groupe 6 dans le cadre du projet de compilation des langages.

## Membres du groupe

- Alexis DIEU
- David FORLEN
- Philippe GRAFF (chef de projet)
- Tristan LE GODAIS

## Structure du dépôt

Ce dépôt contient :

- la [grammaire](src/Tiger.g) *ANTLR3* du langage *Tiger*
- le début d'un [analyseur sémantique](src/Main.java) écrit en *Java*
- la [suite de tests](tests) de la grammaire et de la génération de l'arbre syntaxique abstrait
- le [manuel d'utilisation](#manuel-dutilisation)
- les [notes](meeting-notes) prises lors des réunions
- les [notes](tutorats-notes) prises lors des séances de tutorat
- le [rapport](reports/report-1.pdf) de la première partie du projet

## Manuel d'utilisation

### Récupération du dépôt en local

Pour utiliser le compilateur, commencez par récupérer une version locale du dépôt :

```shell
$ git clone https://gitlab.telecomnancy.univ-lorraine.fr/Philippe.Graff/graff24u
$ cd graff24u
```

Ajoutez *ANTLR3* à la variable d'environnement `CLASSPATH` en remplaçant `<path>` par le chemin absolu du projet :

```shell
echo 'export CLASSPATH=$CLASSPATH:<path>/graff24u/antlr/antlr-3.5.2-complete.jar' >> ~/.bashrc
```

Fermez le terminal puis rouvrez-en un nouveau.

Retournez dans le projet, puis générez les analyseurs lexical (`/src/TigerLexer.java`) et syntaxique (`/src/TigerParser.java`) avec la commande suivante :

```shell
$ make build
```

### Test d'un programme *Tiger*

Pour tester si un programme *Tiger* en particulier est valide syntaxiquement, exécutez les commandes suivantes à la racine du projet en remplaçant `<file>` par le chemin vers le fichier contenant le programme :

```shell
$ mkdir -p bin
$ javac -d bin src/TigerLexer.java src/TigerParser.java src/Test.java
$ java -cp bin:antlr/* Test 2>&1 > /dev/null < <file>
```

Si le programme est invalide, des messages d'erreurs seront affichés sur la sortie standard.

### Lancement de la suite de tests syntaxiques avec *Bash*

Pour lancer l'intégralité des tests syntaxiques, exécutez la commande suivante (la durée peut varier selon la machine) :

```shell
$ make test
```

Un fichier marqué `[PASS]` est un test qui a réussi ou échoué comme prévu, tandis qu'un fichier marqué `[FAIL]` est un test qui a réussi ou échoué alors que le contraire était attendu.
