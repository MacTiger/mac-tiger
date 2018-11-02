#!/bin/bash
regexp='\.tiger$'
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
	for file in $(ls tests/$dir)
	do
		if [[ $file =~ $regexp ]]
		then
			file="tests/$dir/$file"
			output=$(cat $file | java -cp bin:antlr/* Test 2>&1 > /dev/null)
			status=$((! $?))
			if [[ $output != "" ]]
			then
				status="0"
			fi
			if [[ $status == $refstatus ]]
			then
				echo -e "[\033[0;32mPASS\033[0m] $file"
			else
				echo -e "[\033[0;31mFAIL\033[0m] $file"
			fi
		fi
	done
done
