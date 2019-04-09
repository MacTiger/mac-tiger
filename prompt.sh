#!/bin/bash
stdout=""
prgm=""
java -cp bin:lib/* Main --src > prompt.src
status=$?
if [[ $status == 0 ]]
then
	err=$(java -jar lib/microPIUPK.jar -ass prompt.src 2>&1 1> /dev/null)
	echo $err 1>&2
	if [[ $err == "" ]]
	then
		java -jar lib/microPIUPK.jar -batch prompt.iup | sed '$d' | sed '$d' | sed '$s/\(.\+\)Simulation terminée ----------------------------------$/\1\n/' | sed '$d'
	else
		status="5"
	fi
fi
exit $status
