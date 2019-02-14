# Tutorat 7

## Tableau *Tiger*

Définition d'un tableau : juste un pointeur
Tableau de `N` elts : c'est une liste de `N` elts dans la pile, un pointeur pointe vers le premier élément du tableau.
On peut réaffecter une liste à un pointeur.
Pour gérer la taille des tableaux, on peut mettre un élément de fin de chaine dans la pile.
La tds ne peut connaitre la taile d'un tableau => un tableau sans taille est juste un pointeur
On peut imposer de connaitre la taille pour avoir un deplacement.
Quand on passe un tableau en argument de fonction, passer par les pointeurs.
Pour un string, il fait une taille `N`, la TDS connait le nombre de caracteres. Taille = `N` * taille caractere.
Ceci nous fait le deplacement.
On pourrait utiliser le tas pour stocker des éléments en mémoire, mais nous devrions écrire un "ramasse-miettes".
De plus, on ne serait pas capables de décrémenter notre tas.


## TDS ordre 0
Elle devrait implémenter les fonctions natives et types primitifs.


## Registres
Nombre de registres est fixe et assez faible.
On pourra utiliser de 9 à 10 registres. Toucher le moins possible au registre R0 pour passer des valeurs entre les zones mémoire.
On pourra écrire une fonction qui réserve un registre, puis le libère quand on n'en aura plus besoin
=> Utiliser un tableau de bools pour les registres. Dans le code java en global. Il gère les registres.
`[ 0 0 0 1 0 0 ]` avec un `1` pour un registre occupé, resp. `0` pour un inoccupé.
Un test à faire `x+x+x+x+x+x+x+x+x` ; voir si pose probleme au niveau des registres.
`a+b` => stocke `a` dans R1, `b` dans R2, `a+b` dans R1 puis free R2 ; etc.
Rien de mieux que les tests unitaires pour débeugger.


## Appels de fonctions
Arguments des fonctions : déplacement négatif.
Appel à l'assembleur : sauvegarder tous les registres avant de faire l'appel de fonction.
Une fonction est sur une zone mémoire à part => il faut vider tous les registres avant de faire l'appel.
On met les registres au sommet de la pile pour les sauvegarder.
Stocker le résultat de la fonction dans le R0 : communication entre les zones mémoire.
Quand fct° terminée : dépiler puis restocker ce qui était dans R0 autre part pour qu'il soit disponible.


## Soutenance
On lance un seul test le jour de l'exam. => Faire un "gros" truc avec tout dedans ?
Conseil : faire un print à la fin de chaque test unitaire => Dépilage oublié ?
