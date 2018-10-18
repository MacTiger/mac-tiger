# PCL :: Tutorat 1

**Date et heure.** 11 octobre 2018 à 12 heures

## I. Installation et mise en route de ANTLR

### Java

Avant de faire quoi que ce soit, vérifier que *Java* est installé sur votre ordinateur.

```bash
$ which java
/usr/bin/java
```

En outre, *Java* doit être en version 1.8. Pour vérifier que *Java* possède la bonne version.

```bash
$ java -version
java version "1.8.0_181"
...
```

Sinon, vous pouvez télécharger une autre version de java à l'adresse

https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html

Je ne sais pas comment on désinstalle l'ancienne par contre...

### Antlr

Antlr peut être téléchargé à l'adresse suivante.

https://github.com/pmonnin/compilation-lab

Vous ne devez *pas* faire "git clone", mais scroller en bas de la page et cliquer sur les deux liens "antlr-3.5.2-complete.jar" et "antlrworks-1.5.1.jar".

## II. Grammaire Tiger

**1. Factoriser**

On réécrit

$$
B\to AD \\
B\to AC
$$

en

$$
B\to A(C||D)
$$

**2. Dérecursiver**

On réécrit

$$
A\to AC|E
$$

en

$$
A\to EC^*
$$

**3. Priorisation**

Il faut gérer les règles de priorité. Par exemple, un `else` porte toujours sur le dernier `if`.

if if if <u>if</u> **if** **else** <u>else</u> 

**4. Options**

L'en tête contient des options : le $k$ du LL(k). Il y a quatre options java

```antl
OPTIONS {
    language = JAVA;
    output = AST;
    backtrack = false;
    k = 1;
}

tokens {
    // Ça peut attendre, rendre d'abord la grammaire LL(1)
}
```

Au lieu d'écrire $A\to EC$ on écrit $A:EC$  dans ANTLR et en mettant un point virgule *après un retour à la ligne.*

```
A:BC
 |D
 ;
```

Pour tester : ```Generate``` puis ```Generate Code```.

Mieux vaut trop de tests que pas assez : et il faut les garder.