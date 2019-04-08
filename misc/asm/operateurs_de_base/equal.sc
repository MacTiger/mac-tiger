//	str1 = str2
//	oct2 : octet de poids fort

EXIT_EXC EQU 64
READ_EXC EQU 65
WRITE_EXC EQU 66
STACK_ADRS EQU 0X1000
LOAD_ADRS EQU 0xF000
NIL EQU 0

SP EQU R15
WR EQU R14
BP EQU R13

ORG LOAD_ADRS
START main_

LDW SP, #STACK_ADRS
//En tête fichier src


STRING1 string "Cababab" 
STRING2 string "Cababab"




falseEx_
	LDW R0,#0
	BMP fin_-$-2

vraiEx_
	LDW R0,#1
	BMP fin_-$-2
	
oct1Nul_
	AND R2,R3,R0
	BEQ vraiEx_-$-2//Octet de pds fort nul chez str2 : égalité et fin 
	BMP falseEx_-$-2//Octet de pds fort non nul chez str2 : str1<str2





main_
	LDW SP,#STACK_ADRS
	LDW R4,#0 //deplacement dans les deux strings
	

deb_
	//charge adresse 
	LDW R1,#STRING1 
	LDW R2,#STRING2
	
	//décale de deux octets s1 et s2
	ADD R1,R4,R1 
	ADD R2,R4,R2

	//Charge R1 et R2 avec les deux premiers octets de ce sur quoi ils pointent
	LDW R1,(R1)
	LDW R2,(R2)


	//Voir si oct2 nul chez str1
	LDW R3,#65280
	AND R1,R3,R0 //Retire 1er octet str1
	BEQ oct1Nul_-$-2 //Octet de pds fort est nul chez str1 


	//On sait ici que octet pds fort non nul chez str1
	SUB R1,R2,R0
	BLW falseEx_-$-2 //str1<str2 : Fin
	BGT falseEx_-$-2 //str2<str1 : Fin
	
	//On sait ici que les deux octets octets sont égaux chez str1 et str2
	//voir si oct1 est nul chez str1
	LDW R3,#255
	AND R1,R3,R0 //Retire 2e octet str1
	BEQ vraiEx_-$-2  //Valait nul : str1=str2
	
	//on sait ici que octet pds faible non nul chez les deux strings
	ADQ 2,R4
	BMP deb_-$-2
	
fin_ 
	//LDQ WRITE_EXC,WR
	//TRP WR//Affiche R0
	TRP #EXIT_EXC

	