init:
	java -cp res:lib/* -jar lib/antlr-3.5.2-complete.jar res/Tiger.g
	mv Tiger.tokens res/Tiger.tokens
	mkdir -p src/lexical
	mkdir -p src/syntactic
	sed -i '1s/^/package lexical;\n/' res/TigerLexer.java
	sed -i '1s/^/package syntactic;\n/' res/TigerParser.java
	mv res/TigerLexer.java src/lexical/TigerLexer.java
	mv res/TigerParser.java src/syntactic/TigerParser.java
	mkdir -p bin
	mkdir -p logs

build:
	javac -d bin -cp src:lib/* src/Main.java

prompt:
	bash prompt.sh

test:
	bash test.sh

clean:
	rm -r -f bin/*
	rm -r -f logs/*
	rm -f res/Tiger.tokens
	rm -f src/lexical/TigerLexer.java
	rm -f src/syntactic/TigerParser.java

antlrworks:
	java -jar lib/antlrworks-1.5.1.jar

microPIUP:
	java -jar lib/microPIUPK.jar -sim

graphviz:
	tar -xvf lib/graphviz.tar.gz -C lib/


.PHONY: init build prompt test clean antlrworks microPIUP
