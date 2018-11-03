#!/bin/bash
regexp='\.tiger$'
i="0"
j="0"
for dir in $(ls tests)
do
	if [[ $dir == "pass" ]]
	then
		refstatus="1"
	elif [[ $dir == "fail" ]]
	then
		refstatus="0"
	else
		continue
	fi
	for file in $(ls -A tests/$dir)
	do
		if [[ $file =~ $regexp ]]
		then
			file="tests/$dir/$file"
			output=$(java -cp bin:antlr/* Test 2>&1 > /dev/null < $file)
			status=$((! $?))
			if [[ $output != "" ]]
			then
				status="0"
			fi
			if [[ $status == $refstatus ]]
			then
				echo -e "[\033[0;32mPASS\033[0m] $file"
				i=$(($i + 1))
			else
				echo -e "[\033[0;31mFAIL\033[0m] $file"
				j=$(($j + 1))
			fi
		fi
	done
done
l=$(($i + $j))
echo "$l tests, $i passed, $j failed"
