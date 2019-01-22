# Tutorat 6


Deux types de contrôles :
- non exécutable : parcours le code ligne par ligne, lance contrôle sémantique exécutable quand rencontre _1+1_ par exemple.
- exécutables

### Prototype de contrôle sémantique
Pour "+", "-", "/", "*"
```
CS_eval(Node N) :

  CS_eval(N.g) == "int"
  CS-eval(N.d) == "int"


  default :
    "1,...N" -> "int"
    true false -> "bool"
    "[a..Z]*" -> "String"
    a (variable)-> search('a',TDS)
```

#### Différents contrôles sémantiques
Pour fonction :
- verif cohérence des paramètres

=> Etre verbeux dans les try, catch

Mettre les primitives (fonctions) dans une TDS 0 : Déjà prévu par Tristan

### Problèmes écartés

Imposer par contrôle sémantique que la taille des tableaux soit en dur à leur déclaration, pour gérer leur taille
Gestion de tableau :
- dans le tas

Pour le numéro de ligne : NOPE

### Déplacement :
- le premier truc est de déplacement nul
- arguments de fonctions : déplacement < 0



## En assembleur
=> faire le print en premier (sera probablement donné par les profs)
