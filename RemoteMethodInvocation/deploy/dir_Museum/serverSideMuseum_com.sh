#!/usr/bin/env bash
java -Djava.rmi.server.codebase=$3\
     -Djava.rmi.server.useCodebaseOnly=true\
     -Djava.security.policy=java.policy\
     serverSide.museum.MuseumServer $1 $2