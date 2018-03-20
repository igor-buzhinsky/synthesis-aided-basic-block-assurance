#!/bin/bash

for inst in instance_*_*.txt.*.smv; do
    echo "*** $inst"
    cat binary_delay_orig.smv ${inst} | grep -v "^LTLSPEC" > tmp.smv
    echo "VAR block_orig : BINARY_DELAY(DELAY_INPUT_SIGN, DELAY_UP, DELAY_DOWN);" >> tmp.smv
    echo >> tmp.smv
    #echo "LTLSPEC G((DELAY_UP = 0 <-> X(DELAY_UP = 0)) & (DELAY_DOWN = 0 <-> X(DELAY_DOWN = 0)) & (DELAY_UP = 1 <-> X(DELAY_UP = 1)) & (DELAY_DOWN = 1 <-> X(DELAY_DOWN = 1)) & (DELAY_UP = 2 <-> X(DELAY_UP = 2)) & (DELAY_DOWN = 2 <-> X(DELAY_DOWN = 2))) -> G(block_orig.DELAY_OUTPUT_SIGN <-> DELAY_OUTPUT_SIGN)" >> tmp.smv
    echo "LTLSPEC G(block_orig.DELAY_OUTPUT_SIGN <-> DELAY_OUTPUT_SIGN)" >> tmp.smv
    NuSMV tmp.smv | grep -v "^\\*\\*\\*" | grep -v "^$"
    rm tmp.smv
done

