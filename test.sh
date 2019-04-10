#!/bin/bash
time=$SECONDS
a="0"
b="0"
c="0"
d="0"
test() {
	local dir=$1
	local refstatus=$2
	local sign=$(($refstatus < 0))
	if [[ (($sign == 1)) ]]
	then
		refstatus=$((-$refstatus))
	fi
	if [[ (($refstatus == 4)) ]]
	then
		mkdir -p "asm/$dir"
	fi
	mkdir -p "log/$dir"
	for file in $(ls -A tests/$dir)
	do
		if [[ $file =~ \.tiger$ ]]
		then
			local stdin="tests/$dir/$file"
			local stdout
			local stderr="log/$dir/$file.log"
			local status
			if [[ (($refstatus == 4)) ]]
			then
				stdout="asm/$dir/$file.src"
				local refin="tests/$dir/$file.in"
				local refout="tests/$dir/$file.out"
				touch $refin
				touch $refout
				java -cp bin:lib/* Main --no-color --src 2> $stderr 1> $stdout < $stdin
				status=$((($? + 4) % 5))
				if [[ (($status == 4)) ]]
				then
					local prgm="asm/$dir/$file.iup"
					local err=$(java -jar lib/microPIUPK.jar -ass $stdout 2>&1 1>> $stderr)
					if [[ $err == "" ]]
					then
						err=$(diff <(java -jar lib/microPIUPK.jar -batch $prgm < $refin | sed '$d' | sed '$d' | sed '$s/\(.\+\)Simulation terminée ----------------------------------$/\1\n/' | sed '$d') <(cat $refout))
						if [[ $err == "" ]]
						then
							status="5"
						fi
					else
						status="0"
					fi
					if [[ $err != "" ]]
					then
						echo $err >> $stderr
					fi
				fi
			else
				stdout="/dev/null"
				java -cp bin:lib/* Main --no-color --no-output 2> $stderr 1> $stdout < $stdin
				status=$((($? + 4) % 5))
			fi
			if [[ (($status == 0)) ]]
			then
				echo -e "[\033[0;34mEXIT\033[0m] $stdin"
				d=$(($d + 1))
			elif [[ (($status < $refstatus)) ]]
			then
				echo -e "[\033[0;33mWARN\033[0m] $stdin"
				c=$(($c + 1))
			else
				if [[ (($sign == 1)) ]]
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
	if [[ $dir == "lexical" ]]
	then
		status="1"
	elif [[ $dir == "syntactic" ]]
	then
		status="2"
	elif [[ $dir == "semantic" ]]
	then
		status="3"
	elif [[ $dir == "runtime" ]]
	then
		status="4"
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
done
e=$(($a + $b + $c + $d))
time=$(($SECONDS - $time))
echo "$e tests: $a passed, $b failed, $c warned, $d exited ($time seconds)"
