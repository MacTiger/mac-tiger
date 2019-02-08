# Projet de compilation du langage *Tiger*

## Présentation

Ce dépôt contient les sources du compilateur *Java* du langage *Tiger* en code assembleur *microPIUP/ASM* réalisé par le groupe 6 dans le cadre du projet de compilation des langages.

## Membres du groupe

- Alexis DIEU
- David FORLEN
- Philippe GRAFF (chef de projet)
- Tristan LE GODAIS

## Structure du dépôt

Ce dépôt contient :

- la [grammaire](res/Tiger.g) *ANTLR3* du langage *Tiger*
- le [compilateur](src/Main.java) écrit en *Java*
- la [suite de tests](tests) de la grammaire et de la génération de l'arbre syntaxique abstrait
- le [manuel d'utilisation](#manuel-dutilisation)
- les [notes](notes/meeting) prises lors des réunions
- les [notes](notes/tutoring) prises lors des séances de tutorat
- le [rapport](reports/report-1.pdf) de la première partie du projet

## Manuel d'utilisation

### Récupération du dépôt en local

Pour utiliser le compilateur, commencez par récupérer une version locale du dépôt :

```shell
$ git clone https://gitlab.telecomnancy.univ-lorraine.fr/Philippe.Graff/graff24u
$ cd graff24u
```

### Construction du compilateur *Java*

Ajoutez *ANTLR3* à la variable d'environnement `CLASSPATH` en remplaçant `<path>` par le chemin absolu du projet :

```shell
$ echo 'export CLASSPATH=$CLASSPATH:<path>/graff24u/lib/antlr-3.5.2-complete.jar' >> ~/.bashrc
```

Fermez le terminal puis rouvrez-en un nouveau.

Retournez dans le projet, puis générez les analyseurs lexical (`/src/lexical/TigerLexer.java`) et syntaxique (`/src/syntactic/TigerParser.java`) avec la commande suivante :

```shell
$ make init
```

Enfin, construisez le compilateur *Java* :

```shell
$ make build
```

### Test d'un programme *Tiger*

Pour tester si un programme *Tiger* en particulier est valide, exécutez les commandes suivantes à la racine du projet en remplaçant `<file>` par le chemin vers le fichier le contenant :

```shell
$ java -cp bin:lib/* Main > /dev/null < <file>
```

Pour tester un programme *Tiger* directement rédigé au sein du terminal, utilisez le raccourci suivant :

```shell
$ make prompt
```

Dans les deux cas, si le programme est invalide, des messages d'erreurs seront affichés sur la sortie d'erreur.

### Lancement de la suite de tests avec *Bash*

Pour lancer l'intégralité des tests lexicaux, syntaxiques et sémantiques, exécutez la commande suivante (la durée peut varier selon la machine) :

```shell
$ make test
```

Un fichier marqué `[PASS]` est un test qui a réussi ou échoué comme prévu, tandis qu'un fichier marqué `[FAIL]` est un test qui a réussi ou échoué alors que le contraire était attendu. Plus rarement, il est possible de rencontrer un fichier marqué `[EXIT]` qui dénote un plantage du compilateur ou un fichier marqué `[WARN]` qui indique qu'une erreur a été trouvée à une étape de la compilation antérieure à celle testée.
