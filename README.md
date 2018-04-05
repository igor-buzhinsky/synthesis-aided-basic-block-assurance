# Tools to support the framework of synthesis-aided basic block model reliability assurance

This toolset implements a wrapper for several model synthesis tools. It is used in the following paper:

* Igor Buzhinsky, Antti Pakonen, Valeriy Vyatkin. Synthesis-aided reliability assurance of basic block models for model checking purposes. Accepted to 27th International Symposium on Industrial Electronics (ISIE 2018)

## Prerequisites

To build the toolset, you need JDK 1.8 (or greater) and [ant](https://ant.apache.org/). The toolset is intended to work on Linux. Work in Windows may also be possible to some extent.

To run some shell scripts, the *timeout* tool is required.

Some scripts/jars also call the [NuSMV](http://nusmv.fbk.eu/) and [SPIN](spinroot.com/) model checkers.

## Build

To build the toolset, move to the root of the project and run:

> ant

For your convenience, the built version is already provided in [jars](/jars/).

## Used synthesis tools

* EFSM-Tools: the required *jar* binary is supplied: [dependencies/fast-automaton-generator.jar](/dependencies/fast-automaton-generator.jar). The path in [dependencies/efsmtools-path.txt](dependencies/efsmtools-path.txt) is already configured accordingly. You may also wish to replace the supplied *jar* file with a different version obtained from [https://github.com/ulyantsev/EFSM-tools](https://github.com/ulyantsev/EFSM-tools/).

* BoSy: specify the directory where [BoSy](https://www.react.uni-saarland.de/tools/bosy/) is installed in [dependencies/bosy-path.txt](/dependencies/bosy-path.txt).

* Unbeast: specify the directory where [Unbeast](https://www.react.uni-saarland.de/tools/unbeast/) is installed in [dependencies/unbeast-path.txt](/dependencies/unbeast-path.txt).

* G4LTL-ST: we use our own modification of [G4LTL-ST](https://sourceforge.net/projects/g4ltl/) which replaces the graphical interface with a command line one. If you wish to use it, either request it personally or implement it yourself.

## Examples

Examples of use are located in [examples](/examples). Run shell scripts from the directories where they are located. The original NuSMV basic block models are not public, so only the prepared LTL specifications are available. Thus, scripts named *compare.sh* will only work if you prepare such models yourself according to the provided interfaces.

## Bugs

Encoding of integer variables may work incorrectly. This was noticed to happen sometimes when the bounds of integer variables are not of the form 0..(2^k - 1), e.g. -1..1 or 0..2. Possibly, the bug does not happen in a more general situation: when the *total* number of possible values is the power of 2. So, if the total number of possible values of some integer variable is not a power of 2 and you notice something strange, consider extending the range in INPUT and OUTPUT declarations (e.g. from 0..5 to 0..7) to get rid of the error. Unfortunately, you will also need to modify the specification to treat such cases as dummy ones. 

## Troubleshooting, bugs, questions, research collaboration, etc.

Please note that this code was not tested in different environments, and hence there may be some (even quite a lot of) issues.

Feel free to contact Igor Buzhinsky (igor.buzhinsky@gmail.com).
