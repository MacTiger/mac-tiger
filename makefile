init:
	bash init.sh

build:
	bash build.sh

prompt:
	bash prompt.sh

test:
	bash test.sh

torture:
	bash torture.sh

clean:
	bash clean.sh

antlrworks:
	java -jar lib/antlrworks-1.5.1.jar

microPIUP:
	java -jar lib/microPIUPK.jar -sim

graphviz:
	tar -xvf lib/graphviz.tar.gz -C lib/

.PHONY: init build prompt test torture clean antlrworks microPIUP graphviz
