#!/usr/bin/env bash
java -cp .:\* -Djava.rmi.server.codebase="file://$(pwd)/"\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy\
     serverSide.assaultParty.AssaultPartyManagerServer $1 $2