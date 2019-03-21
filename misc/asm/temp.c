#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int lower_strict_str(char*, char*); 
	

int main(int argc, char** args) {
	printf("%s et %s: %d [%d]\n", "a", "b", lower_strict_str("a", "b"), 1);
	printf("%s et %s: %d [%d]\n", "b", "a", lower_strict_str("b", "a"), 0);
	printf("%s et %s: %d [%d]\n", "a", "ab", lower_strict_str("a", "ab"), 1);
	printf("%s et %s: %d [%d]\n", "ab", "a", lower_strict_str("ab", "a"), 0);
	printf("%s et %s: %d [%d]\n", "a", "a", lower_strict_str("a", "a"), 0);
	exit(0);
}


int lower_strict_str(char* str1, char* str2) {
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
