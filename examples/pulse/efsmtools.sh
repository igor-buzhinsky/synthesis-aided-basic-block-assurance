#!/bin/bash

rm -f instance_*.efsmtools.smv instance_*.efsmtools.pml log.txt
d=$(pwd)/
cd ../..
for values in 4; do
    if [[ $values == 2 ]]; then
        states=3
    elif [[ $values == 3 ]]; then
        states=4
    elif [[ $values == 4 ]]; then
        states=7
    fi
    for trace_complexity in 0; do
        inst=instance_${values}_${trace_complexity}.txt
        echo "*** $inst"
        inst="$d$inst"
        timeout 5m java -jar jars/efsmtools-generator.jar $inst -s $states --path "$(cat dependencies/efsmtools-path.txt)" #2>&1 > log.txt
        RETVAL=$?
        if [[ $RETVAL == 0 ]]; then
            cp result.smv ${inst}.efsmtools.smv
            cp result.pml ${inst}.efsmtools.pml
            cat log.txt | grep "\\(TRACES\\|NuSMV\\|SPIN\\): "
        else
            echo TIMEOUT
        fi
    done
done

