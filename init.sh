#!/bin/bash
java -jar lib/antlr-3.5.2-complete.jar res/Tiger.g
mv Tiger.tokens res/Tiger.tokens
mkdir -p src/lexical
mkdir -p src/syntactic
sed -i '1s/^/package lexical;\n/' res/TigerLexer.java
sed -i '1s/^/package syntactic;\n/' res/TigerParser.java
mv res/TigerLexer.java src/lexical/TigerLexer.java
mv res/TigerParser.java src/syntactic/TigerParser.java
mkdir -p asm
mkdir -p bin
mkdir -p log
