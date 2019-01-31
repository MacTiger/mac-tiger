#!/bin/bash
time=$SECONDS
regexp='\.tiger$'
i="0"
j="0"
k="0"
for dir in $(ls tests)
do
	if [[ $dir == "lexical" ]]
	then
		refstatus="1"
	elif [[ $dir == "syntactic" ]]
	then
		refstatus="2"
	elif [[ $dir == "semantic" ]]
	then
		refstatus="3"
	else
		continue
	fi
	for subdir in $(ls tests/$dir)
	do
		for file in $(ls -A tests/$dir/$subdir)
		do
			if [[ $file =~ $regexp ]]
			then
				file="tests/$dir/$subdir/$file"
				if [[ (($refstatus > 2)) ]]
				then
					output=$(java -cp bin:lib/* Main 2>&1 > /dev/null < $file)
				else
					output=$(java -cp bin:lib/* Main --syntax-only 2>&1 > /dev/null < $file)
				fi
				status=$((($? + 4) % 5))
				if [[ (($status == 0)) ]]
				then
					echo -e "[\033[0;33mWARN\033[0m] $file"
					k=$(($k + 1))
				elif [[ $subdir == "pass" && (($status > $refstatus)) || $subdir == "fail" && (($status == $refstatus)) ]]
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
done
l=$(($i + $j + $k))
time=$(($SECONDS - $time))
echo "$l tests: $i passed, $j failed, $k warned ($time seconds)"
