test:
	java -cp src:antlr/* -jar antlr/antlr-3.5.2-complete.jar src/Tiger.g
	mkdir -p bin
	javac -d bin src/TigerLexer.java src/TigerParser.java src/Test.java
	java -cp bin:antlr/* Test
