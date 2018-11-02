build:
	java -cp src:antlr/* -jar antlr/antlr-3.5.2-complete.jar src/Tiger.g
	mv Tiger.tokens src/Tiger.tokens

test:
	mkdir -p bin
	javac -d bin src/TigerLexer.java src/TigerParser.java src/Test.java
	bash test.sh

clean:
	rm -r -f bin/*
	rm -f src/Tiger.tokens
	rm -f src/TigerLexer.java
	rm -f src/TigerParser.java

.PHONY: build test clean
