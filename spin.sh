#!/bin/bash

trail="$1"
name="$2"

if   [[ "$trail" == trail ]]; then
    :
elif [[ "$trail" == notrail ]]; then
    :
else
    echo "Invalid argument! Expected trail or notrail."
    exit
fi

timeout="$3"

if [[ "$timeout" == "" ]]; then
    timeout=0
fi

line="=========================================================================="

format="\t%U user,\t%S system,\t%e elapsed,\t%Mk maxresident\n$line"

echo "$line"
echo "*** GENERATING PAN SOURCE ***"
/usr/bin/time -f "$format" $time spin -a $name 2>&1
echo "*** COMPILING PAN ***"
/usr/bin/time -f "$format" cc -O2 -o pan pan.c 2>&1

for prop in $(cat "$name" | grep "^ltl " | sed 's/^ltl //; s/ .*$//'); do
    echo "*** RUNNING PAN FOR $prop ***"
    timeout "$timeout"s /usr/bin/time -f "$format" ./pan -a -N $prop -m2000000 2>&1
    if [[ "$?" == 124 ]]; then
        echo "*** $prop : TIMEOUT ***"
    elif [ -f "$name.trail" ]; then
        if [[ "$trail" == trail ]]; then
            echo "*** $prop = FALSE, ERROR TRAIL ***";
            /usr/bin/time -f "$format" spin -k $name.trail -pglrs $name 2>&1
        else
            echo "*** $prop = FALSE ***";
            echo "$line";
        fi
    else
        echo "*** $prop = TRUE ***";
        echo "$line";
    fi
    rm -f $name.trail
done

rm -f pan.* pan
