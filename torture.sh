#!/bin/bash
dir="tests/semantic/fail"
stdin="torture.tiger"
> $stdin
for file in $(ls $dir)
do
	echo ";" >> $stdin
	echo "/* $file */" >> $stdin
	cat "$dir/$file" >> $stdin
done
echo ")" >> $stdin
sed -i '1s/^;/\(/' $stdin
java -cp bin:lib/* Main --no-output 1> /dev/null < $stdin
