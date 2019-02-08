# Réunion 13

**Date et heure.** 8 février 2019 à 14 heures

**Prochaine réunion.** 18 février 2019 à 17 heures

**Ordre du jour.**

- Point sur les contrôles sémantiques qui ont été faits et implémentés.
- Standardisation des messages d'erreurs
- Visualisation de la TDS
- Lancement de la suite des tests
- Travail à faire

## 1. Point sur les contrôles sémantiques

Il ne faut pas systématiquement retourner `null` après une erreur afin d'analyser tout le code.

L'inférence de type à la déclaration des variable a été faite.

Philippe était chargé de faire les contrôles sémantiques pour `FIELD` et `ITEM`. Il ne faut pas utiliser la fonction `getSymbol()`. `ITEM` n'est pas encore tout à faire terminé.

Pour que deux types soient compatibles, il ne suffit pas qu'ils soient égaux. Par exemple, `nil` doit pouvoir être donné en champ d'un *record*.

Travail de Tristan. les contrôles sémantiques sur *while* et *if* ont été réalisés. Pour le while, il s'agit de vérifier que la condition est un entier et que le type de la boucle est un `void`. `null` dans `checktype` signifie `void`. Pour le if, on vérifie que la condition est un entier. S'il n'y a qu'un `then`, il faut que son type soit `void`. S'il y a un `then` et un `else`, il doit y avoir compatibilité entre les types.

Le `nil` nécessite un nouveau type "record vide". C'est une valeur spéciale de type et le checkType a été adapté en conséquence. Le checktype ne prend plus le dernier argument qui était inutile. Il vérifie maintenant si un des deux types sont `nil`. Tristan a fait une quinzaine de tests sur `nil`. David a également fait des tests, mais ne passent pas pour l'instant.

Vérifier qu'on affecte pas l'indice d'une boucle dans une boucle : à faire.

## 2. Standardisation des messages d'erreurs

Tous les messages d'erreurs doivent être en anglais. Pas de majuscule ni de points, les phrases doivent être au présent.

## 3. Visualisation de la TDS

Le sujet impose une visualisation de la TDS. On pourrait utiliser GraphViz. Pour l'instant, seule la TDS est capable de connaître son contenu. 

## 4. Lancement de la suite des tests

`make prompt`  permet de tester un programme à la voler. Se référer au manuel pour voir comment tester un fichier en particulier.

## 5. Travail à faire

**Alexis.**

- Revoir `CALL`
- Tester ses contrôles sémantiques
- Revoir le code de `ARR` : on ne doit pas retourner `null` juste après un contrôle sémantique et même chose pour `REC`
- Ne pas comparer deux types avec `==`
- Mettre à jour le fichier des contrôles sémantiques
- Écrire des tests (en veillant à ce qu'il n'y ait pas de WARN)
- Chercher d'autres contrôles sémantiques éventuellements oubliés
- Commencer le rapport

**Philippe**.

- Terminer le test sémantique sur `ITEM`
- Mettre à jour le fichier des contrôles sémantiques
- Écrire des tests (en veillant à ce qu'il n'y ait pas de WARN)
- Chercher d'autres contrôles sémantiques éventuellements oubliés
- Commencer le rapport

**David.**

- Mettre à jour le fichier des contrôles sémantiques
- Écrire des tests (en veillant à ce qu'il n'y ait pas de WARN)
- Visualisation de la TDS

**Tristan.**

- Commenter
- Indice de boucle (contrôle sémantique)
- Mettre à jour le fichier des contrôles sémantiques
- Écrire des tests (en veillant à ce qu'il n'y ait pas de WARN)
- Corriger la sortie des couleurs
- Visualisation de la TDS

