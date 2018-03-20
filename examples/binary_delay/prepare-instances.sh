#!/bin/bash

for values in 2 3; do
    for trace_complexity in 0; do
        inst=instance_${values}_${trace_complexity}.txt
        echo "*** $inst"
        cp binary_delay_header_${values}.txt $inst
        echo >> $inst
        cat binary_delay_orig.smv binary_delay_wrapper_${values}.smv > tmp.smv
        java -jar ../../jars/nusmv-trace-recorder.jar binary_delay_header_${values}.txt --model tmp.smv --traceNum $trace_complexity --traceLen $trace_complexity >> $inst
    done
done
rm tmp.smv

