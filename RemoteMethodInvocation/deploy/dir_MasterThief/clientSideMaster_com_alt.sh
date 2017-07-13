#!/usr/bin/env bash
java -cp .:\* -Djava.rmi.server.codebase="file://$(pwd)/"\
     -Djava.rmi.server.useCodebaseOnly=false\
     clientSide.masterThief.MasterThiefClient $1 $2
