# Tutorat 5

## 1. Table des symboles

Un programme exécute des opérations sur des objets (des variables, des fonctions, des structures...). Le terme *variable* est assez large et peut désigner aussi bien des structures.

La table des symbole (abrégé *TDS*) permet de localiser ces différents objets.

Une TDS est un ensemble de tables qui contient ces objets. Les variables doivent être dans une table. Il n'y a pas du tout de contrôle sémantique à faire pour le moment.

## 2. Importer l'AST dans Java

```java
String path = <votre fichier>;
InputStream file = new FileInputStream(new File(path));
ANTLRInputStream input = new ANTLRInputStream(file);

GramLexer lexer = new GramLexer(input);
CommonTokenStream tokens = new CommonTokenStream(lexer);
GramParser.fichier_return result = parser.fichier();
// Penser à supprimer le root après cette ligne
Tree t = (Tree) result.getTree().getChild(0);
```

Le programme doit toujours commencer par un nœud `root`.

Après avoir importé l'arbre, on doit le parcourir et effectuer certaines actions (par exemple, écrire une nouvelle variable dans une TDS).

