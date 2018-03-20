#!/bin/bash

rm -f instance_*.unbeast.smv instance_*.unbeast.pml log.txt
d=$(pwd)/
cd ../..
for values in 4; do
    for trace_complexity in 0; do
        inst=instance_${values}_${trace_complexity}.txt
        echo "*** $inst"
        inst="$d$inst"
        timeout 5m java -jar jars/unbeast-generator.jar $inst --path "$(cat ../../dependencies/unbeast-path.txt)" 2>&1 > log.txt
        RETVAL=$?
        if [[ $RETVAL == 0 ]]; then
            cp result.smv ${inst}.unbeast.smv
            cp result.pml ${inst}.unbeast.pml
            cat log.txt | grep "\\(Total execution time\\|TRACES\\|NuSMV\\|SPIN\\): "
        else
            echo TIMEOUT
        fi
    done
done

