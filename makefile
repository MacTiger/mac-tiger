init:
	bash init.sh

build:
	bash build.sh

graph:
	bash graph.sh

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

micropiup:
	java -jar lib/microPIUPK.jar -sim

.PHONY: init build graph prompt test torture clean antlrworks micropiup
