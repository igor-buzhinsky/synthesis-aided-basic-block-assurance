#!/bin/bash

for inst in flip_flop.txt.*.smv; do
    echo "*** $inst"
    cat flip_flop_orig.smv ${inst} | grep -v "^LTLSPEC" > tmp.smv
    echo "VAR block_orig : FLIP_FLOP(FF_RESET_SIGN, FF_SET_SIGN, FF_OUTPUT_VALUE, FF_RESET_DOMINATES);" >> tmp.smv
    echo >> tmp.smv
    echo "LTLSPEC G ((block_orig.FF_OUTPUT_SIGN <-> FF_OUTPUT_SIGN) & (block_orig.FF_OUTINV_SIGN <-> FF_OUTINV_SIGN))" >> tmp.smv
    NuSMV tmp.smv | grep -v "^\\*\\*\\*" | grep -v "^$"
    rm tmp.smv
done

