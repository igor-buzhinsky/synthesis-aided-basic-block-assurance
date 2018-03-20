#!/bin/bash

rm -f instance_*.bosy.smv instance_*.bosy.pml log.txt
d=$(pwd)/
cd ../..
for values in 4; do
    for trace_complexity in 0; do
        inst=instance_${values}_${trace_complexity}.txt
        echo "*** $inst"
        inst="$d$inst"
        timeout 5m java -jar jars/bosy-generator.jar "$inst" --path "$(cat dependencies/bosy-path.txt)" 2>&1 > log.txt
        RETVAL=$?
        if [[ $RETVAL == 0 ]]; then
            cp result.smv ${inst}.bosy.smv
            cat log.txt | grep "\\(Total execution time\\|TRACES\\|NuSMV\\|SPIN\\): "
        else
            echo TIMEOUT
        fi
    done
done
