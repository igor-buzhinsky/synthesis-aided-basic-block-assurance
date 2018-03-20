#!/bin/bash

d=$(pwd)/
cd ../..
inst=flip_flop.txt
inst="$d$inst"
timeout 5m java -jar jars/efsmtools-generator.jar $inst -s 3 --path "$(cat dependencies/efsmtools-path.txt)" #2>&1 > log.txt
RETVAL=$?
if [[ $RETVAL == 0 ]]; then
    cp result.smv ${inst}.efsmtools.smv
    cp result.pml ${inst}.efsmtools.pml
    cat log.txt | grep "\\(TRACES\\|NuSMV\\|SPIN\\): "
else
    echo TIMEOUT
fi

