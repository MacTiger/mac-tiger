#!/bin/bash
time=$SECONDS
regexp='\.tiger$'
a="0"
b="0"
c="0"
d="0"
test() {
	local dir=$1
	local refstatus=$2
	mkdir -p "logs/$dir"
	local sign=$(($refstatus < 0))
	if [[ (($sign == 1)) ]]
	then
		refstatus=$((-$refstatus))
	fi
	for file in $(ls -A tests/$dir)
	do
		if [[ $file =~ $regexp ]]
		then
			local stdin="tests/$dir/$file"
			local stderr="logs/$dir/$file.txt"
			java -cp bin:lib/* Main --no-color 2> $stderr 1> /dev/null < $stdin
			local status=$((($? + 4) % 5))
			if [[ (($status == 0)) ]]
			then
				echo -e "[\033[0;34mEXIT\033[0m] $stdin"
				d=$(($d + 1))
			elif [[ (($status < $refstatus)) ]]
			then
				echo -e "[\033[0;33mWARN\033[0m] $stdin"
				c=$(($c + 1))
			else
				if [[ (($refstatus == 0)) ]]
				then
					status=$(($status < 4))
				elif [[ (($sign == 1)) ]]
				then
					status=$(($status > $refstatus))
				else
					status=$(($status == $refstatus))
				fi
				if [[ (($status == 1)) ]]
				then
					echo -e "[\033[0;31mFAIL\033[0m] $stdin"
					b=$(($b + 1))
				else
					echo -e "[\033[0;32mPASS\033[0m] $stdin"
					a=$(($a + 1))
				fi
			fi
		fi
	done
}
for dir in $(ls tests)
do
	if [[ $dir == "prgm" ]]
	then
		test $dir "0"
	else
		if [[ $dir == "lexical" ]]
		then
			status="1"
		elif [[ $dir == "syntactic" ]]
		then
			status="2"
		elif [[ $dir == "semantic" ]]
		then
			status="3"
		else
			continue
		fi
		for subdir in $(ls tests/$dir)
		do
			if [[ $subdir == "fail" ]]
			then
				refstatus=$((-$status))
			elif [[ $subdir == "pass" ]]
			then
				refstatus=$status
			else
				continue
			fi
			test "$dir/$subdir" $refstatus
		done
	fi
done
e=$(($a + $b + $c + $d))
time=$(($SECONDS - $time))
echo "$e tests: $a passed, $b failed, $c warned, $d exited ($time seconds)"
