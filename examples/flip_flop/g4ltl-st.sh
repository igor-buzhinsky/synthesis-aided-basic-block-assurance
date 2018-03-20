#!/bin/bash

d=$(pwd)/
cd ../..
inst=flip_flop.txt
inst="$d$inst"
timeout 5m java -jar jars/g4ltl-st-generator.jar $inst --path "$(cat dependencies/g4ltlst-path.txt)" 2>&1 > log.txt
RETVAL=$?
if [[ $RETVAL == 0 ]]; then
    cp result.smv ${inst}.g4ltlst.smv
    cp result.pml ${inst}.g4ltlst.pml
    cat log.txt | grep "\\(Total execution time\\|TRACES\\|NuSMV\\|SPIN\\): "
else
    echo TIMEOUT
fi
