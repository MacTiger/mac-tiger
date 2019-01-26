init:
	java -cp res:lib/* -jar lib/antlr-3.5.2-complete.jar res/Tiger.g
	mv Tiger.tokens res/Tiger.tokens
	mv res/TigerLexer.java src/TigerLexer.java
	mv res/TigerParser.java src/TigerParser.java
	mkdir -p bin

build:
	javac -d bin -cp src:lib/* src/Main.java

prompt:
	bash prompt.sh

test:
	bash test.sh

clean:
	rm -r -f bin/*
	rm -f res/Tiger.tokens
	rm -f src/TigerLexer.java
	rm -f src/TigerParser.java

antlrworks:
	java -jar lib/antlrworks-1.5.1.jar

microPIUP:
	java -jar lib/microPIUPK.jar -sim

.PHONY: init build prompt test clean antlrworks microPIUP
