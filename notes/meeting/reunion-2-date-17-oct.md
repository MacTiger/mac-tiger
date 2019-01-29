# Réunion 2

**Date et heure.** 17 octobre 2018 à 14h00 

**Prochaine réunion.** 24 octobre 2018 à 14h30

**Ordre du jour.**

1. Jalons
2. Répartition de la grammaire
3. Travail à faire

## 1. Jalons

On va voir comment se répartir les règles. On pourrait, d'après David, le faire par groupe de deux.

On commence par le rappel des objectifs. Slack n'apporterait rien en terme de gestion de projet vs. discord ou le drive.

En ce qui concerne le Gantt, David s'en est occupé. Il le mettera en format éditable sur le drive. On aura 10 jours pour effectuer la grammaire, et ensuite vingt-deux jours pour faire chaque tâches.

Il faut absolument penser à garder trace du temps de travail pour chaque tâches.

Il faudra mettre les comptes-rendus sur GIT. Tout le monde doit faire un `git clone`. Pour rédiger n'importe quel document texte, préférer le format markdown lisible directement depuis gitlab.

Bilan sur le cours envoyé pas Mme. Colin. Il y a certains détails qui n'apparaissent pas dans la documentation de Tiger. Tout le monde a lu le sujet.

## 2. Répartition de la grammaire

Nous allons répartir les tâches pour réaliser la grammaire. Notations de ANTLR.

- L'étoile en exposant signifie *zéro ou plus*
- Le plus en exposant signifie *une ou plus*
- Le `opt`en indice signifie *au plus un*
- `*;` en exposant signifie *zéro ou plus séparés par un point virgule*

Il faudra compléter la grammaire (les règles concernant les additions n'apparaissent pas dans la documentation, par exemple). Il faudra aussi s'occuper des expressions régulières pour reconnaître les tokens.

On devra, dans l'ordre :

1. Factoriser
2. Dérécursiver
3. Prioriser
4. Mettre sous format ANTLR

On verra les commentaires plus tard. On pourra, d'après David, éventuellement télécharger un compilateur Tiger pour l'utiliser comme contrôle.

## 3. Travail à faire

**Alexis.**

+ Terminer le premier TP de traduction
+ Mettre les réunion en ligne
+ Écrire les règles : tout ce qui est avant exp exclus plus letexp, et pour les terminaux, *tyid* et *id*

**Philippe**.

+ Écrire les règles : tout ce qui est avant exp exclus plus letexp, et pour les terminaux, *tyid* et *id*

**David.**

+ Écrire les règles : tout ce qui est après exp inclus, tous les autres terminaux (*infixop*, *intlit*, *stringlit*)

**Tristan.**

+ Chercher en ligne un compilateur *Tiger*
+ Écrire les règles : tout ce qui est après exp inclus, tous les autres terminaux (*infixop*, *intlit*, *stringlit*)