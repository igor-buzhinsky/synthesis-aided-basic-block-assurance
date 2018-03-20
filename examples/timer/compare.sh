#!/bin/bash

for inst in instance_*_*.txt.*.smv; do
    echo "*** $inst"
    cat timer_orig.smv ${inst} | grep -v "^LTLSPEC" > tmp.smv
    echo "VAR block_orig : TIMER(TI_STATE_S, TI_RESET_S, TI_MAX_TIME, TI_RESET_IF_MAX_REACHED, TI_RESET_IF_SWITCHED_OFF);" >> tmp.smv
    echo >> tmp.smv
    #echo "LTLSPEC G ((TI_MAX_TIME = 0 -> X(TI_MAX_TIME = 0)) & (TI_MAX_TIME = 1 -> X(TI_MAX_TIME = 1)) & (TI_RESET_IF_MAX_REACHED = 0 -> X(TI_RESET_IF_MAX_REACHED = 0)) & (TI_RESET_IF_MAX_REACHED = 1 -> X(TI_RESET_IF_MAX_REACHED = 1)) & (TI_RESET_IF_SWITCHED_OFF = 0 -> X(TI_RESET_IF_SWITCHED_OFF = 0)) & (TI_RESET_IF_SWITCHED_OFF = 1 -> X(TI_RESET_IF_SWITCHED_OFF = 1))) -> G((block_orig.TI_TIME_SIGN = TI_TIME_SIGN) & (block_orig.TI_REMAINING_TIME_SIGN = TI_REMAINING_TIME_SIGN) & (block_orig.TI_MAX_TIME_REACHED_SIGN <-> TI_MAX_TIME_REACHED_SIGN))" >> tmp.smv
    #echo "LTLSPEC G ((TI_MAX_TIME = 0 -> X(TI_MAX_TIME = 0)) & (TI_MAX_TIME = 1 -> X(TI_MAX_TIME = 1)) & (TI_RESET_IF_MAX_REACHED = 0 -> X(TI_RESET_IF_MAX_REACHED = 0)) & (TI_RESET_IF_MAX_REACHED = 1 -> X(TI_RESET_IF_MAX_REACHED = 1)) & (TI_RESET_IF_SWITCHED_OFF = 0 -> X(TI_RESET_IF_SWITCHED_OFF = 0)) & (TI_RESET_IF_SWITCHED_OFF = 1 -> X(TI_RESET_IF_SWITCHED_OFF = 1))) -> G((block_orig.TI_TIME_SIGN = TI_TIME_SIGN) & (block_orig.TI_REMAINING_TIME_SIGN = TI_REMAINING_TIME_SIGN))" >> tmp.smv
    #echo "LTLSPEC G((TI_MAX_TIME = 0 -> X(TI_MAX_TIME = 0)) & (TI_MAX_TIME = 1 -> X(TI_MAX_TIME = 1)) & (TI_MAX_TIME = 2 -> X(TI_MAX_TIME = 2)) & (TI_RESET_IF_MAX_REACHED = 0 -> X(TI_RESET_IF_MAX_REACHED = 0)) & (TI_RESET_IF_MAX_REACHED = 1 -> X(TI_RESET_IF_MAX_REACHED = 1)) & (TI_RESET_IF_SWITCHED_OFF = 0 -> X(TI_RESET_IF_SWITCHED_OFF = 0)) & (TI_RESET_IF_SWITCHED_OFF = 1 -> X(TI_RESET_IF_SWITCHED_OFF = 1)) & !(TI_RESET_IF_SWITCHED_OFF = 1 & !TI_STATE_S & !TI_RESET_S & X(TI_STATE_S & !TI_RESET_S))) -> G((block_orig.TI_TIME_SIGN = TI_TIME_SIGN) & (block_orig.TI_REMAINING_TIME_SIGN = TI_REMAINING_TIME_SIGN))" >> tmp.smv
    echo "LTLSPEC G((TI_MAX_TIME = 0 -> X(TI_MAX_TIME = 0)) & (TI_MAX_TIME = 1 -> X(TI_MAX_TIME = 1)) & (TI_MAX_TIME = 2 -> X(TI_MAX_TIME = 2)) & (TI_MAX_TIME = 3 -> X(TI_MAX_TIME = 3)) & (TI_RESET_IF_MAX_REACHED = 0 -> X(TI_RESET_IF_MAX_REACHED = 0)) & (TI_RESET_IF_MAX_REACHED = 1 -> X(TI_RESET_IF_MAX_REACHED = 1)) & (TI_RESET_IF_SWITCHED_OFF = 0 -> X(TI_RESET_IF_SWITCHED_OFF = 0)) & (TI_RESET_IF_SWITCHED_OFF = 1 -> X(TI_RESET_IF_SWITCHED_OFF = 1)) & !(TI_RESET_IF_SWITCHED_OFF = 1 & !TI_STATE_S & !TI_RESET_S & X(TI_STATE_S & !TI_RESET_S)) & !(block_orig.clock = TI_MAX_TIME)) -> G((block_orig.TI_TIME_SIGN = TI_TIME_SIGN) & (block_orig.TI_REMAINING_TIME_SIGN = TI_REMAINING_TIME_SIGN))" >> tmp.smv
    bmc="-bmc -bmc_length 10"
    NuSMV tmp.smv | grep -v "^\\*\\*\\*" | grep -v "^$"
    #NuSMV $bmc tmp.smv | grep -v "^\\*\\*\\*" | grep -v "^$"
    rm tmp.smv
done

