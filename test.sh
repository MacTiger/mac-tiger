#!/bin/bash
regexp=".*\.tiger"
for dir in `ls tests`
do
	if [[ $dir == "pass" ]]
	then
		refstatus="1"
	elif [[ $dir == "fail" ]]
	then
		refstatus="0"
	else
		refstatus="-1"
	fi
	if [[ $refstatus != "-1" ]]
	then
		for file in `ls tests/$dir`
		do
			if [[ $file =~ $regexp ]]
			then
				file="tests/$dir/$file"
				cat $file | java -cp bin:antlr/* Test 2> /dev/null
				status=$((! $?))
				if [[ $status == $refstatus ]]
				then
					echo -e "[\033[0;32mPASS\033[0m] $file"
				else
					echo -e "[\033[0;31mFAIL\033[0m] $file"
				fi
			fi
		done
	fi
done
