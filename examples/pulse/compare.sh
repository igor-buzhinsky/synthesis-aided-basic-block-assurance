#!/bin/bash

for inst in instance_*_*.txt.*.smv; do
    echo "*** $inst"
    cat pulse_orig.smv ${inst} | grep -v "^LTLSPEC" > tmp.smv
    echo "VAR block_orig : PULSE(PULSE_INPUT_SIGN, PULSE_LENGTH, PULSE_MODE);" >> tmp.smv
    echo >> tmp.smv
    #echo "LTLSPEC G((block_orig.PULSE_OUTPUT_SIGN <-> PULSE_OUTPUT_SIGN) & (block_orig.PULSE_REMAINING_TIME = PULSE_REMAINING_TIME))" >> tmp.smv
    #echo "LTLSPEC G(block_orig.PULSE_OUTPUT_SIGN <-> PULSE_OUTPUT_SIGN)" >> tmp.smv
    #echo "LTLSPEC G((PULSE_MODE = 0 -> X(PULSE_MODE = 0)) & (PULSE_MODE = 1 -> X(PULSE_MODE = 1)) & (PULSE_MODE = 2 -> X(PULSE_MODE = 2)) & (PULSE_MODE = 3 -> X(PULSE_MODE = 3))) -> G((block_orig.PULSE_OUTPUT_SIGN <-> PULSE_OUTPUT_SIGN) & (block_orig.PULSE_REMAINING_TIME > 0 & PULSE_REMAINING_TIME > 0 -> block_orig.PULSE_REMAINING_TIME = PULSE_REMAINING_TIME) & (block_orig.PULSE_REMAINING_TIME <= 0 <-> PULSE_REMAINING_TIME <= 0))" >> tmp.smv
    #echo "LTLSPEC G((block_orig.PULSE_OUTPUT_SIGN <-> PULSE_OUTPUT_SIGN) & (block_orig.PULSE_REMAINING_TIME > 0 & PULSE_REMAINING_TIME > 0 -> block_orig.PULSE_REMAINING_TIME = PULSE_REMAINING_TIME) & (block_orig.PULSE_REMAINING_TIME <= 0 <-> PULSE_REMAINING_TIME <= 0))" >> tmp.smv
    bmc="-bmc -bmc_length 10"
    echo "LTLSPEC G((PULSE_MODE = 0 -> X(PULSE_MODE = 0)) & (PULSE_MODE = 1 -> X(PULSE_MODE = 1)) & (PULSE_MODE = 2 -> X(PULSE_MODE = 2)) & (PULSE_MODE = 3 -> X(PULSE_MODE = 3))) -> G((block_orig.PULSE_OUTPUT_SIGN <-> PULSE_OUTPUT_SIGN) & (block_orig.PULSE_REMAINING_TIME > 0 & PULSE_REMAINING_TIME > 0 -> block_orig.PULSE_REMAINING_TIME = PULSE_REMAINING_TIME) & (block_orig.PULSE_REMAINING_TIME <= 0 <-> PULSE_REMAINING_TIME <= 0))" >> tmp.smv
    bmc="-bmc -bmc_length 10"
    #NuSMV $bmc tmp.smv | grep -v "^\\*\\*\\*" | grep -v "^$"
    NuSMV tmp.smv | grep -v "^\\*\\*\\*" | grep -v "^$"
    rm tmp.smv
done

