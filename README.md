	Ce dépôt doit contenir :
	
Le mode d'emloi du compilateur

Les dossiers intermédiare et final (PDF)

Les fichiers source

Attention à ne pas inclure les fichiers de configuration => .gitignore

En-tête fichier de grammaire : options 

options : *langage=java *output=ast *backtrace=false *k=1


	Etapes :
1° mise gramm dans ANTLR 

2° adapter la gramm : ambiguités, ll1

3° Plus tard :
Faudra créer un projet JAVA, puis récupération données ANTLR


	Notes Grammaire
Réc gauche :
	A->A*A|E
	
	A-> EC*


Priorités :
	
A->A+A

A->A*A
	
A->A(+|*)A | idf

A->idf(("*"|"+")idf)*

A1->A2(*A2)*

A2->idf(+idf)*






