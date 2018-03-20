#!/bin/bash

for values in 2 3 4; do
    for trace_complexity in 0; do
        inst=instance_${values}_${trace_complexity}.txt
        echo "*** $inst"
        cp timer_header_${values}.txt $inst
        echo >> $inst
        cat timer_orig.smv timer_wrapper_${values}.smv > tmp.smv
        java -jar ../../jars/nusmv-trace-recorder.jar timer_header_${values}.txt --model tmp.smv --traceNum $trace_complexity --traceLen $trace_complexity >> $inst
    done
done
rm tmp.smv

