# Génération de code

Code d'extension \*.src => fichier généré \*.iup
Assembler	  : java -jar microPIUP.jar -ass file.src
Exécuter	  : java -jar microPIUP.jar -batch file.iup
Lancer simulateur : java -jar microPIUP.jar -sim

## Instructions

LDW R1,(R2)	: charge ce qui est pointé par R2 dans R1 <=
STW R1,(R5)	: on met ce qui est dans R1 à l'@ pointée par R5 =>
MPC R4		: charge le PC dans R4
ADQ 4,R1	: Met dans R1 4+R1
JEA (R2)	: Jump To Effective Address
BMP LOOP-$-2	: Inconditionnel : Va à l'instruction d'addresse LOOP
BEQ/BNE/BGE/BLE/ LOOP-$-2 : Conditionnel
BGT/BLW/BAE/BAB
BBL/BBE/BVS/BVC
TRP #EXIT_EXC	: arrête le programme
JEA @main_	: nécessaire, car sinon le programme ne s'arrêterait pas

## Registres

>R0 utilisé pour les résultats de fonctions.
>R1 -> R10 disponibles
>R11 et R12 réservés
>R13 pour BP BasePointer <=> Pointeur de pile
>R14 pour WR WorkRegister <=> Registre de travail
>R15 pour SP StackPointer <=> Pointeur de pile

## Chaînage statique & dynamique

>Le chaînage statique contient la base de la fonction englobante
>Le chaînage dynamique contient la base de la fonction appelante "chaînage BP"

## Fonctions

>Passage des arguments :
	=>Mettre (BP)-deplctVar dans R1 puis empiler R1 à -(SP)
	=> etc.

>Appel :
	Dans l'environnement de la fonction appelante on a les params + @ de retour fonct° apelée
	Sommet de pile SP sur @ de retour.
	=>Décrémenter SP : ADQ -2,SP ;
	=>Mettre BP pour "chaînage BP" : chaînage dynamique.
	=>Changer la valeur de BP : base de la nouvelle fonction en "chaînage BP"
	=>Réserver de la place pour les variables de la nouvelle fonction => SUB SP,R1,SP
		dans R1 se situe la place à réserver
		SP pointera sur la variable la plus "haute" de la pile

>Sortie :
	=>Mettre SP sur BP : chaînage dynamique
	=>Mettre BP sur "chaînage BP" de la fonction appelante
	=>Incrémenter SP : sera sur @ retour de la fonction appelée
	=>Résultat dans R0 puis décrémenter SP

>Nettoyage par fonction appelante :
	=>Ajouter à SP ce qui est nécessaire pour ne plus avoir les arguments ADQ 2*n,SP

Avancement : 9/19, 4.3 Exemple de code généré avec environnement pile classique et jeu d'instruction RISC strict
