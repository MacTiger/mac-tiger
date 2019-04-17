#!/bin/bash
java -cp bin:lib/* Main --dot > graph.dot
status=$?
if [[ (($status == 0)) ]]
then
	err=$(dot -Tsvg graph.dot -o graph.svg 2>&1 1> /dev/null)
	if [[ $err == "" ]]
	then
		xdg-open graph.svg || open graph.svg
		status=$?
		if [[ (($status != 0)) ]]
		then
			status=$(($status + 4))
		fi
	else
		echo $err 1>&2
		status="1"
	fi
fi
exit $status
