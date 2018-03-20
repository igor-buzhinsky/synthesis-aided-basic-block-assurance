#!/bin/bash

d=$(pwd)/
cd ../..
inst=flip_flop.txt
inst="$d$inst"
timeout 5m java -jar jars/unbeast-generator.jar $inst --path "$(cat dependencies/unbeast-path.txt)" 2>&1 > log.txt
RETVAL=$?
if [[ $RETVAL == 0 ]]; then
    cp result.smv ${inst}.unbeast.smv
    cp result.pml ${inst}.unbeast.pml
    cat log.txt | grep "\\(Total execution time\\|TRACES\\|NuSMV\\|SPIN\\): "
else
    echo TIMEOUT
fi

