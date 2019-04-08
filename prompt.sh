#!/bin/bash
stdout="prompt.src"
prgm="prompt.iup"
java -cp bin:lib/* Main --src > $stdout
status=$?
if [[ $status == 0 ]]
then
	err=$(java -jar lib/microPIUPK.jar -ass $stdout 2>&1 1> /dev/null)
	echo $err 1>&2
	if [[ $err == "" ]]
	then
		java -jar lib/microPIUPK.jar -batch $prgm
	else
		status="5"
	fi
fi
exit $status
