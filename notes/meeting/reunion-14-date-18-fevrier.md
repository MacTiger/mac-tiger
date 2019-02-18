# Réunion 14

**Date et heure.** 18 février 2019 à 17 heures

**Prochaine réunion.** 28 février 2019 à 17 heures

**Ordre du jour.**

- Point sur ce qui était à faire pour aujourd'hui
- Où nous en sommes en terme de planning
- Répartition rédaction du rapport

# 1. Point sur ce qui était à faire pour aujourd'hui

Alexis a corrigé certain tests sémantiques qui étaient faux sémantiquement. Après un `pull` de nombreux `EXIT` apparaissent. Tristan propose que dans nos fichiers `main.java`, on comment la ligne numéro 62. David remarque alors que tout ce passe beaucoup mieux malgré quelques fails. David finira la TDS ce soir. 

Philippe a retravaillé le test sémantique sur `ITEM`. 

David rappelle qu'on avait, à la base, la liste de tous les contrôles sémantiques qu'on avait identifiés. Il va falloir orgraniser ce fichier dans l'optique de classer ces contrôles. Tristan pense qu'il suffit de chercher dans la table des symboles (`SymbolTable.java`) les endroits où des erreurs sémantiques sont levés et de les reporter dans ce fichier. On va remettre ce point pour tout le monde.

David a réalisé la visualisation de la TDS, mais ce n'est pas tout à fait fini. La visualisation devrait être terminée d'ici ce soir. Cela aidera à voir la hiérarchie des programmes que nous avons écrits. 

Tristan rappelle qu'il faut installer GraphViz. Il faudrait que l'on puisse faire la génération d'images depuis java. Quand on lance le main, le programme génère la TDS, mais en console. On peut le mettre dans un fichier .gv, et générer un visuel de la TDS. Mais ce n'est pas très pratique.

Tristan a réalisé le contrôle d'indice de boucle. Il n'a pas touché au fichier des contrôles sémantiques, et n'a pas écrit de tests à part pour l'indice de boucle que l'on n'a pas le droit d'affecté. Il faut encore un peu toucher aux tests. Quand on va voir dans le dossier logs, on n'a plus des caractères spéciaux qui s'affichent. On peut *lire* dans le fichier log. 

## 2. Où nous en sommes en terme de planning

David remarque qu'il faudrait commencer la génération de code. Tout le debogage sera basé sur les prints. On aura effectivement à faire des tests unitaire sur l'affichage. Il faudra également voir comment implémenter les fonctions qui nous ont été données. Mme Collin ne nous a donné que le print. Tristan propose qu'on fasse un nouveau dossier *runtime* pour les tests avec la même structure qu'ailleurs (fail et pass).

Pour cette génération de code, il va falloir qu'on se la répartisse. Logiquement, on ne devrait pas retoucher à la table des symboles. David propose, pour le système de tests, de mettre la sortie attendue en commentaire en début de fichier `.tiger`. Tristan propose, pour les fonctions de la bibliothèque standard, de réfléchir à un pseudocode.

## 3. Répartition des tâches pour le rapport

Il va surtout falloir entamer la partie sur les contrôles sémantiques. Chacun doit remplir sa partie sur les contrôles sémantiques.

## 4. Travail à faire

**Alexis.**

- Mettre à jour le fichier des contrôles sémantiques 
- Lire la documentation sur MicroPIUPK
- Écrire le code assembleur des opérations de bases (arithmétiques et comparaisons) 
- Remplir sa partie sur les contrôles sémantiques

**Philippe**.

- Mettre à jour le fichier des contrôles sémantiques 
- Lire la documentation sur MicroPIUPK
- Écrire le code assembleur des opérations de bases (arithmétiques et comparaisons)
- Remplir sa partie sur les contrôles sémantiques
- Revoir le diagramme de classes de la TDS

**David.**

- Mettre à jour le fichier des contrôles sémantiques 
- Rendre optionnel la génération de la visualisation de la TDS
- Lire la documentation sur MicroPIUPK
- Programmer le `print_i` en assembleur
- Ajouter le GANTT au rapport
- Remplir sa partie sur les contrôles sémantiques


**Tristan.**

- Mettre à jour le fichier des contrôles sémantiques 
- Commenter son code
- Ajouter la documentation de MicroPIUPK sur le dépôt
- Mettre à jour le système de tests pour prendre en compte les tests à l'exécution
- Lire la documentation sur MicroPIUPK
- Programmer le `print_i` en assembleur
- Remplir sa partie sur les contrôles sémantiques
- Remplir la partie sur les tests

