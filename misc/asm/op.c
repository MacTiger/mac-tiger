/* Les fonctions commentées n'ont pas vraiment
 * de sens en C. Il faut les laisser commentées.
 */
int add(int, int);
int substract(int, int);
int multiply(int, int);
int divide(int, int);
int equal_str(char*, char*);
int equal_int(int, int);
// equal_arr
// equal_rec
int nequal_str(char*, char*);
int nequal_int(int, int);
// nequal_arr
// nequal_rec
int geq_str(char*, char*);
int geq_int(int, int);
int gneq_str(char*, char*);
int gneq_int(int, int);
int leq_str(char*, char*);
int leq_int(int, int);
int lneq_str(char*, char*);
int lneq_int(int, int);
int and(int, int);
int or(int, int);

int add(int x, int y) {
	return x + y;
}

int substract(int x, int y) {

}

int multiply(int x, int y) {

}

int divide(int x, int y) {

}

int equal_str(char* x, char* y) {
		int i = 0;    
		while (True) {
				if (x[i] == '\0' || y[i] == '\0') {
						if (x[i] == '\0' && y[i] == '\0') {
								return 1;
						} else {
								return 0;
						}
				} else {
						if (x[i] == y[i]) {
								i = i + 1;
						} else {
								return 0;
						}
				}
		}
}

int equal_int(int x, int y) {
	return (x == y);
}

int nequal_str(char x*, char y*) {

}

int nequal_int(int x, int y) {

}

int geq_str(char x*, char y*) {

}

int geq_int(int x, int y) {

}

int gneq_str(char x*, char y*) {

}

int gneq_int(int x, int y) {

}

int leq_str(char x*, char y*) {

}

int leq_int(int x, int y) {

}

int lneq_str(char* str1, char* str2) {
		int i = 0;	
		while (1) {
				if (str1[i] == '\0' && str2[i] == '\0') {
						return 0;
				} else if (str1[i] == '\0' && str2[i] != '\0') {
						return 1;
				} else if (str1[i] != '\0' && str2[i] == '\0') {
						return 0;
				} else if (str1[i] < str2[i]) {
						return 1;
				} else if (str1[i] > str2[i]) {
						return 0;
				} else {
						i = i + 1;
				}
		}
}

int lneq_int(int x, int y) {

}

int and(int x, int y) {

}

int or(int x, int y) {

}

